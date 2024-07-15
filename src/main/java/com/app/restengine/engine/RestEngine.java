package com.app.restengine.engine;

import com.app.restengine.domain.ServiceConfiguration;
import com.app.restengine.exception.RestEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RestEngine extends ScheduledThreadPoolExecutor {

    private final Logger logger = LoggerFactory.getLogger(RestEngine.class);

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
        ServiceConfiguration configuration = command.getConfiguration();
        restEngineScheduledFuture.setConfiguration(configuration);
        logger.info("Service was started. Service configuration ID: " + configuration.getId());
        return restEngineScheduledFuture;
    }

    public int cancelTask(int taskId) {
        Runnable task = this.getQueue().stream()
                .filter(runnable -> ((RestEngineScheduledFuture<?>) runnable).getConfiguration().getId() == taskId)
                .findFirst()
                .orElseThrow(() -> new RestEngineException("Running task not found with id:" + taskId));
        boolean remove = this.remove(task);
        if (remove) {
            logger.info("Service was stopped. Service configuration ID: " + taskId);
        }
        return Boolean.compare(remove, false);
    }
}
