/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mrmitew.bodylog.domain.executor

import java.util.concurrent.*

/**
 * Decorated {@link ThreadPoolExecutor}
 */
class JobExecutor(initialPoolSize: Int = 3,
                  maxPoolSize: Int = 5,
                  // Sets the amount of time an idle thread waits before terminating
                  keepAliveTime: Pair<Long, TimeUnit> = Pair(10, TimeUnit.SECONDS)) : ThreadExecutor {

    private val workQueue: BlockingQueue<Runnable>
    private val threadPoolExecutor: ThreadPoolExecutor
    private val threadFactory: ThreadFactory

    init {
        workQueue = LinkedBlockingQueue()
        threadFactory = JobThreadFactory()
        threadPoolExecutor = ThreadPoolExecutor(initialPoolSize, maxPoolSize,
                keepAliveTime.first, keepAliveTime.second, workQueue, threadFactory)
    }

    override fun execute(runnable: Runnable) {
        checkNotNull(runnable)
        threadPoolExecutor.execute(runnable)
    }

    private class JobThreadFactory : ThreadFactory {
        companion object {
            const val THREAD_NAME = "android_"
        }

        private var counter = 0

        override fun newThread(runnable: Runnable): Thread =
                Thread(runnable, THREAD_NAME + counter++)

    }
}