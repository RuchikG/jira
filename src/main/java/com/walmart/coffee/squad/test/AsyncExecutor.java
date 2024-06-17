package com.walmart.coffee.squad.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.*;

@Slf4j
public class AsyncExecutor {

    private static final int NUM_OF_THREADS = 10;

    private static final int CORE_POOL_SIZE = 10;

    private static final int MAX_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME_IN_HOUR = 1;

    private static final int QUEUE_SIZE = 10;

    private static ExecutorService asyncExecutor;

    private ThreadPoolExecutor asyncExecutorForCCExclusionZipCode;

    public ExecutorService getAsyncExecutor() {
        if (asyncExecutor == null) {
            asyncExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);
        }
        return asyncExecutor;
    }

//    public ThreadPoolExecutor getAsyncExecutorForCCExclusionZipCode() {
//        if (asyncExecutorForCCExclusionZipCode == null) {
//            LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
//            asyncExecutorForCCExclusionZipCode = new ThreadPoolExecutor(
//                    CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME_IN_HOUR, TimeUnit.HOURS, linkedBlockingQueue,
//                    Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
//        }
//        log.info("BaseExecutor: getAsyncExecutorForCCExclusionZipCode:: "
//                + "Current Queue size: " + asyncExecutorForCCExclusionZipCode.getQueue().size()
//                + " Max Queue Size: " + );
//        return asyncExecutorForCCExclusionZipCode;
//    }

    public void executeRunnableTask(Runnable task) {
        Assert.notNull(task, "task cannot be null");
        getAsyncExecutor().submit(task);
    }
}
