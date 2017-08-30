package com.github.mrmitew.bodylog.di.activity;

public interface ActivityComponentBuilder<M extends ActivityModule, C extends ActivityComponent> {
    ActivityComponentBuilder<M, C> activityModule(M activityModule);
    C build();
}