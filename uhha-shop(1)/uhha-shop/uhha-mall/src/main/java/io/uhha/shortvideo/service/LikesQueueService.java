package io.uhha.shortvideo.service;

import com.google.common.collect.Queues;
import io.uhha.shortvideo.domain.UmsMemberLikes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LikesQueueService implements InitializingBean {

    //创建一个可重用固定线程数的线程池
    private ExecutorService pool = Executors.newFixedThreadPool(1);

    private static final BlockingQueue<UmsMemberLikes> likesBlockingQueue = new ArrayBlockingQueue<UmsMemberLikes>(100000);
    private static final BlockingQueue<UmsMemberLikes> unlikesBlockingQueue = new ArrayBlockingQueue<UmsMemberLikes>(100000);

    @Autowired
    private IUmsMemberLikesService likesService;

    //线程活动
    private volatile boolean threadActivity = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool.execute(new Runnable(){
            @Override
            public void run() {
                while (threadActivity) { //如果系统关闭，则不再运行
                    try {
                        List<UmsMemberLikes> likeData = new ArrayList<>();
                        List<UmsMemberLikes> unlikeData = new ArrayList<>();
                        //每次到100条数据才进行入库，或者等待1秒，没达到100条也继续入库
                        Queues.drain(likesBlockingQueue, likeData, 100, 1, TimeUnit.SECONDS);//第三个参数：数量; 第四个参数：时间; 第五个参数：时间单位
                        //每次到100条数据才进行入库，或者等待1分钟，没达到100条也继续入库
                        Queues.drain(unlikesBlockingQueue, unlikeData, 100, 1, TimeUnit.SECONDS);//第三个参数：数量; 第四个参数：时间; 第五个参数：时间单位
                        if(CollectionUtils.isNotEmpty(likeData)){
                            likesService.saveBatch(likeData);
                            likesService.calculateAndUpdateLikesQty(likeData);
                        }
                        if(CollectionUtils.isNotEmpty(unlikeData)){
                            List<Long> ids = unlikeData.stream().map(UmsMemberLikes::getId).collect(Collectors.toList());
                            likesService.removeByIds(ids);
                            //重新算一次
                            likesService.calculateAndUpdateLikesQty(unlikeData);
                        }
                    } catch (InterruptedException e) {
                        if (log.isErrorEnabled()) {
                            log.error("访问量消费队列错误",e);
                        }
                    }
                }
            }});
    }

    public boolean pushLikeQueue(UmsMemberLikes like){
        //add(anObject):添加元素到队列里，添加成功返回true，容量满了添加失败会抛出IllegalStateException异常
        //offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
        //offer(E o, long timeout, TimeUnit unit),可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。
        //put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
        return likesBlockingQueue.offer(like);//添加一个元素并返回true 如果队列已满，则返回false
    }

    public boolean pushUnlikeQueue(UmsMemberLikes like){
        //add(anObject):添加元素到队列里，添加成功返回true，容量满了添加失败会抛出IllegalStateException异常
        //offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.（本方法不阻塞当前执行方法的线程）
        //offer(E o, long timeout, TimeUnit unit),可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。
        //put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
        return unlikesBlockingQueue.offer(like);//添加一个元素并返回true 如果队列已满，则返回false
    }

    @PreDestroy
    public void destroy() {
        threadActivity = false;
        pool.shutdownNow();
    }

}
