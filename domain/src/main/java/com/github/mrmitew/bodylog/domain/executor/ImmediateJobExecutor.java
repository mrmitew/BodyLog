package com.github.mrmitew.bodylog.domain.executor;

public class ImmediateJobExecutor implements ThreadExecutor {
    @Override
    public void execute(final Runnable runnable) {
        runnable.run();
    }
}