package com.github.mrmitew.bodylog;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.github.mrmitew.bodylog.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.di.activity.HasActivitySubcomponentBuilders;
import com.github.mrmitew.bodylog.di.application.ApplicationComponent;
import com.github.mrmitew.bodylog.di.application.ApplicationModule;
import com.github.mrmitew.bodylog.di.application.DaggerApplicationComponent;
import com.github.mrmitew.bodylog.di.presenter.PresenterInjector;
import com.squareup.leakcanary.LeakCanary;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class AndroidApplication extends /*MultiDex*/Application implements HasActivitySubcomponentBuilders {
    private static final String TAG = AndroidApplication.class.getSimpleName();

    @Inject
    Map<Class<? extends Activity>, Provider<ActivityComponentBuilder>> mActivityComponentBuilders;

    private ApplicationComponent mApplicationComponent;

    public static HasActivitySubcomponentBuilders get(Context context) {
        return ((HasActivitySubcomponentBuilders) context.getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!setupLeakCanary()) {
            return;
        }

        setupDagger();
    }

    @Override
    public ActivityComponentBuilder getActivityComponentBuilder(
            Class<? extends Activity> activityClass) {
        return mActivityComponentBuilders.get(activityClass).get();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void setupDagger() {
        mApplicationComponent =
                DaggerApplicationComponent
                        .builder()
                        .applicationModule(new ApplicationModule(this))
                        .build();
        mApplicationComponent.inject(this);
    }

    private boolean setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return false;
        }

        LeakCanary.install(this);
        return true;
    }

    public PresenterInjector getViewModelInjector() {
        return mApplicationComponent;
    }
}
