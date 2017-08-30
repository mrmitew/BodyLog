package com.github.mrmitew.bodylog.di.activity;

import android.app.Activity;

public interface HasActivitySubcomponentBuilders {
    ActivityComponentBuilder getActivityComponentBuilder(Class<? extends Activity> activityClass);
}