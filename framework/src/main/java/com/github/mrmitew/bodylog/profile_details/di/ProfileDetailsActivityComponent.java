package com.github.mrmitew.bodylog.profile_details.di;

import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_details.presenter.ProfileDetailsPresenter;
import com.github.mrmitew.bodylog.di.activity.ActivityComponent;
import com.github.mrmitew.bodylog.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.di.activity.ActivityModule;
import com.github.mrmitew.bodylog.di.activity.ActivityScope;
import com.github.mrmitew.bodylog.profile_details.view.ProfileDetailsActivity;
import com.jakewharton.rxrelay2.BehaviorRelay;

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

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ProfileDetailsActivityModule, ProfileDetailsActivityComponent> {
    }

    @Module
    class ProfileDetailsActivityModule extends ActivityModule<ProfileDetailsActivity> {
        public ProfileDetailsActivityModule(ProfileDetailsActivity activity) {
            super(activity);
        }

        @Provides
        @ActivityScope
        public ProfileDetailsPresenter providesProfileDetailsPresenter(LoadProfileInteractor loadProfileInteractor) {
            return new ProfileDetailsPresenter(provideActivity(), loadProfileInteractor, BehaviorRelay.create());
        }
    }
}