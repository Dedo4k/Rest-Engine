package com.app.restengine.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RestEngine extends ScheduledThreadPoolExecutor {

    public RestEngine(@Value("${rest-engine.threads}") int corePoolSize) {
        super(corePoolSize);
        this.setRemoveOnCancelPolicy(true);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        return new RestEngineScheduledFuture<>(runnable, task);
    }

    public RestEngineScheduledFuture<?> scheduleAtFixedRate(RestEngineTask command,
                                                            long initialDelay,
                                                            long period,
                                                            TimeUnit unit) {
        RestEngineScheduledFuture<?> restEngineScheduledFuture = (RestEngineScheduledFuture<?>) super.scheduleAtFixedRate(command, initialDelay, period, unit);
        restEngineScheduledFuture.setConfiguration(command.getConfiguration());
        return restEngineScheduledFuture;
    }
}
