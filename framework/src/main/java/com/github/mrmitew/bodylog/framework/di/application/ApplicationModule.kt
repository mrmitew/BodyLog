package com.github.mrmitew.bodylog.framework.di.application

import android.content.Context
import com.github.mrmitew.bodylog.domain.executor.JobExecutor
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor
import com.github.mrmitew.bodylog.framework.AndroidApplication
import com.github.mrmitew.bodylog.framework.executor.UiThread
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val mApplication: AndroidApplication) {
    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context = mApplication

    @Provides
    @Singleton
    internal fun provideUiThread(): UiThread = UiThread()

    @Provides
    @Singleton
    internal fun provideJobExecutor(): JobExecutor = JobExecutor()

    @Provides
    @Singleton
    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    internal fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread
}