package io.uhha.coin.common.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;


/**
 * 注册ShutdownHook优雅关闭dubbo并防止spring容器关闭
 * 
 * @author Jonathan 2018年5月30日 下午2:14:54
 * @since 1.0.0
 */
public class AwaitingNonWebApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger          logger                 = LoggerFactory
        .getLogger(AwaitingNonWebApplicationListener.class);

    private static final ExecutorService executorService        = Executors
        .newSingleThreadExecutor(new ThreadFactory() {

                                                                        @Override
                                                                        public Thread newThread(Runnable r) {
                                                                            return new Thread(r, "SpringBoot-Await");
                                                                        }
                                                                    });

    private static final AtomicBoolean   shutdownHookRegistered = new AtomicBoolean(false);

    private static final AtomicBoolean   awaited                = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        final SpringApplication springApplication = event.getSpringApplication();
        //TODO
//        if (springApplication.isWebEnvironment()) {
//            return;
//        }
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                synchronized (springApplication) {
                    if (logger.isInfoEnabled()) {
                        logger.info(" [Dubbo] Current Spring Boot Application is await...");
                    }
                    while (!awaited.get()) {
                        try {
                            springApplication.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        if (shutdownHookRegistered.compareAndSet(false, true)) {
            registerShutdownHook(new Thread(new Runnable() {

                @Override
                public void run() {
                    synchronized (springApplication) {

                        if (awaited.compareAndSet(false, true)) {
                            if (logger.isInfoEnabled()) {
                                logger.info(" [Dubbo] Current Spring Boot Application is about to shutdown...");
                            }
                            springApplication.notifyAll();
                            // 关闭dubbo相关资源
//                            ProtocolConfig.destroyAll();
                            // 关闭连接池
                            executorService.shutdown();

                        }
                    }

                }
            }, "ShutdownHookThread"));

        }
    }

    private void registerShutdownHook(Thread thread) {
        Runtime.getRuntime().addShutdownHook(thread);
    }

}
