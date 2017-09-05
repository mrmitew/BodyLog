package com.github.mrmitew.bodylog.framework.di.activity;

public interface ActivityComponentBuilder<M extends ActivityModule, C extends ActivityComponent> {
    ActivityComponentBuilder<M, C> activityModule(M activityModule);
    C build();
}