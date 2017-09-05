package com.github.mrmitew.bodylog.framework.di.activity;

import android.app.Activity;

public interface HasActivitySubcomponentBuilders {
    ActivityComponentBuilder getActivityComponentBuilder(Class<? extends Activity> activityClass);
}