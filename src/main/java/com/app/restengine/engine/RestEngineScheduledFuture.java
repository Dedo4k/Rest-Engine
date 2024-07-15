package com.app.restengine.engine;

import com.app.restengine.domain.ServiceConfiguration;

import java.util.concurrent.*;

public class RestEngineScheduledFuture<V> implements RunnableScheduledFuture<V> {
    private Runnable runnable;
    private RunnableScheduledFuture<V> scheduledFuture;
    private ServiceConfiguration configuration;

    public RestEngineScheduledFuture(Runnable runnable, RunnableScheduledFuture<V> scheduledFuture) {
        this.runnable = runnable;
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public boolean isPeriodic() {
        return scheduledFuture.isPeriodic();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return scheduledFuture.getDelay(unit);
    }

    @Override
    public int compareTo(Delayed o) {
        return scheduledFuture.compareTo(o);
    }

    @Override
    public void run() {
        scheduledFuture.run();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return scheduledFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return scheduledFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return scheduledFuture.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return scheduledFuture.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return scheduledFuture.get(timeout, unit);
    }

    public ServiceConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ServiceConfiguration configuration) {
        this.configuration = configuration;
    }
}
