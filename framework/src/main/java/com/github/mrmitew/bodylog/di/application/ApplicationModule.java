package com.github.mrmitew.bodylog.di.application;

import android.content.Context;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.domain.executor.JobExecutor;
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread;
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor;
import com.github.mrmitew.bodylog.executor.UiThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private AndroidApplication mApplication;

    public ApplicationModule(AndroidApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    UiThread provideUiThread() {
        return new UiThread();
    }

    @Provides
    @Singleton
    JobExecutor provideJobExecutor() {
        return new JobExecutor();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UiThread uiThread) {
        return uiThread;
    }
}