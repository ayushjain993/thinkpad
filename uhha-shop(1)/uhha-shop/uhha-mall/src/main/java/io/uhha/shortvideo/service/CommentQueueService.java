package io.uhha.shortvideo.service;

import com.google.common.collect.Queues;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.shortvideo.domain.ShortVideoComment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Slf4j
public class CommentQueueService implements InitializingBean {

    //创建一个可重用固定线程数的线程池
    private ExecutorService pool = Executors.newFixedThreadPool(1);

    private static final BlockingQueue<ShortVideoComment> commentBlockingQueue = new ArrayBlockingQueue<ShortVideoComment>(100000);

    @Autowired
    private IShortVideoCommentService commentService;

    @Autowired
    private IShortVideoService shortVideoService;

    //线程活动
    private volatile boolean threadActivity = true;

    @Autowired
    private MallNotifyHelper mallNotifyHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool.execute(new Runnable(){
            @Override
            public void run() {
                while (threadActivity) { //如果系统关闭，则不再运行
                    try {
                        List<ShortVideoComment> data = new ArrayList<>();
                        //每次到100条数据才进行入库，或者等待1秒，没达到100条也继续入库
                        Queues.drain(commentBlockingQueue, data, 100, 1, TimeUnit.SECONDS);//第三个参数：数量; 第四个参数：时间; 第五个参数：时间单位
                        if(CollectionUtils.isNotEmpty(data)){
                            shortVideoService.calculateAndUpdateRepliedqty(data);
                            commentService.saveBatch(data);
                            //批量发送通知
                            mallNotifyHelper.sendVideoCommentNotify(data);
                        }
                    } catch (InterruptedException e) {
                        if (log.isErrorEnabled()) {
                            log.error("访问量消费队列错误",e);
                        }
                    }
                }
            }});
    }

    public boolean pushQueue(ShortVideoComment comment){
        //add(anObject):添加元素到队列里，添加成功返回true，容量满了添加失败会抛出IllegalStateException异常
        //offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
        //offer(E o, long timeout, TimeUnit unit),可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。
        //put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
        return commentBlockingQueue.offer(comment);//添加一个元素并返回true 如果队列已满，则返回false
    }

    @PreDestroy
    public void destroy() {
        threadActivity = false;
        pool.shutdownNow();
    }

}
