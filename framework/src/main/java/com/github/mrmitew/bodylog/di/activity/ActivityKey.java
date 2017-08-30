package com.github.mrmitew.bodylog.di.activity;

import android.app.Activity;

import dagger.MapKey;

@MapKey
public @interface ActivityKey {
    Class<? extends Activity> value();
}
