package com.github.mrmitew.bodylog.di.activity;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ActivityModule<T extends FragmentActivity> {
    protected final T activity;

    public ActivityModule(T activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public T provideActivity() {
        return activity;
    }
}
