package com.github.mrmitew.bodylog.di.application;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.di.presenter.PresenterHolderInjector;
import com.github.mrmitew.bodylog.profile_details.di.ProfileDetailsActivityComponent;
import com.github.mrmitew.bodylog.profile_edit.di.ProfileEditActivityComponent;
import com.github.mrmitew.bodylog.repository.di.RepositoryModule;

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