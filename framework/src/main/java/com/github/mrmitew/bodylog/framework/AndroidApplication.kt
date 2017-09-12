package com.github.mrmitew.bodylog.framework

import android.app.Activity
import android.app.Application
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponentBuilder
import com.github.mrmitew.bodylog.framework.di.activity.HasActivitySubcomponentBuilders
import com.github.mrmitew.bodylog.framework.di.application.ApplicationComponent
import com.github.mrmitew.bodylog.framework.di.application.ApplicationModule
import com.github.mrmitew.bodylog.framework.di.application.DaggerApplicationComponent
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector
import com.squareup.leakcanary.LeakCanary
import javax.inject.Inject
import javax.inject.Provider

class AndroidApplication : Application(), HasActivitySubcomponentBuilders {
    @Inject protected lateinit var activityComponentBuilders: Map<Class<out Activity>, @JvmSuppressWildcards Provider<ActivityComponentBuilder<*, *>>>

    private lateinit var applicationComponent: ApplicationComponent

    companion object {
        fun getActivitySubcomponentBuilders(applicationContext: Application): HasActivitySubcomponentBuilders =
                applicationContext as HasActivitySubcomponentBuilders

        fun getPresenterHolderInjector(applicationContext: Application): PresenterHolderInjector =
                (applicationContext as AndroidApplication).applicationComponent
    }

    override fun getActivityComponentBuilder(activityClass: Class<out Activity>): ActivityComponentBuilder<*, *>? =
            activityComponentBuilders[activityClass]?.get()


    override fun onCreate() {
        super.onCreate()

        if (!setupLeakCanary())
            return

        setupDagger()
    }

    private fun setupDagger() {
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent.inject(this)
    }

    private fun setupLeakCanary(): Boolean {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return false
        }

        LeakCanary.install(this)
        return true
    }
}