package com.github.mrmitew.bodylog.framework.di.activity

import android.support.v4.app.FragmentActivity

import dagger.Module
import dagger.Provides

@Module
abstract class ActivityModule<out T : FragmentActivity>(protected val activity: T) {

    @Provides
    @ActivityScope
    fun provideActivity(): T {
        return activity
    }
}
