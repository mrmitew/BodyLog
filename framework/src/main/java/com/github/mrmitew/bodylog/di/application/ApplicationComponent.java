package com.github.mrmitew.bodylog.di.application;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.repository.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ActivityBindingModule.class,
                RepositoryModule.class,
        }
)
public interface ApplicationComponent {
    AndroidApplication inject(AndroidApplication application);
}