package com.github.mrmitew.bodylog.domain.executor

class ImmediateJobExecutor : ThreadExecutor {
    override fun execute(runnable: Runnable) = runnable.run()
}