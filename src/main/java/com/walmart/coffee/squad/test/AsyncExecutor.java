package com.walmart.coffee.squad.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.*;

@Component
@Slf4j
public class AsyncExecutor {

    private static final int NUM_OF_THREADS = 10;

    private static final int CORE_POOL_SIZE = 10;

    private static final int MAX_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME_IN_HOUR = 1;

    private static final int QUEUE_SIZE = 10;

    private static ExecutorService asyncExecutor;

    public ExecutorService getAsyncExecutor() {
        if (asyncExecutor == null) {
            asyncExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);
        }
        return asyncExecutor;
    }

    public void executeRunnableTask(Runnable task) {
        Assert.notNull(task, "task cannot be null");
        getAsyncExecutor().submit(task);
    }
}
