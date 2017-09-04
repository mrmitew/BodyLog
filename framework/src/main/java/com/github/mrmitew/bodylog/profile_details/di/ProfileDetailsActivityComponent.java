package com.github.mrmitew.bodylog.profile_details.di;

import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_details.presenter.ProfileDetailsPresenter;
import com.github.mrmitew.bodylog.di.activity.ActivityComponent;
import com.github.mrmitew.bodylog.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.di.activity.ActivityModule;
import com.github.mrmitew.bodylog.di.activity.ActivityScope;
import com.github.mrmitew.bodylog.profile_details.view.ProfileDetailsActivity;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {
                ProfileDetailsActivityComponent.ProfileDetailsActivityModule.class,
        }
)
public interface ProfileDetailsActivityComponent extends ActivityComponent<ProfileDetailsActivity> {
    @Singleton
    @Module
    class PresenterModule {
        @Provides
        ProfileDetailsPresenter providesProfileDetailsPresenter(LoadProfileInteractor loadProfileInteractor) {
            return new ProfileDetailsPresenter(loadProfileInteractor, BehaviorRelay.create());
        }
    }

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ProfileDetailsActivityModule, ProfileDetailsActivityComponent> {
    }

    @ActivityScope
    @Module
    class ProfileDetailsActivityModule extends ActivityModule<ProfileDetailsActivity> {
        public ProfileDetailsActivityModule(ProfileDetailsActivity activity) {
            super(activity);
        }
    }
}