package com.github.mrmitew.bodylog.framework.di.application;

import com.github.mrmitew.bodylog.framework.AndroidApplication;
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector;
import com.github.mrmitew.bodylog.framework.profile_details.di.ProfileDetailsActivityComponent;
import com.github.mrmitew.bodylog.framework.profile_edit.di.ProfileEditActivityComponent;
import com.github.mrmitew.bodylog.framework.repository.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ActivityBindingModule.class,
                RepositoryModule.class,
                ProfileDetailsActivityComponent.PresenterModule.class,
                ProfileEditActivityComponent.PresenterModule.class,
        }
)
public interface ApplicationComponent extends PresenterHolderInjector {
    AndroidApplication inject(AndroidApplication application);
}