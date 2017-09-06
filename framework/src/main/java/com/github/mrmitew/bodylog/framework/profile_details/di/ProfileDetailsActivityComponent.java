package com.github.mrmitew.bodylog.framework.profile_details.di;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.presenter.LastUpdatedPresenter;
import com.github.mrmitew.bodylog.adapter.profile_details.main.presenter.ProfileDetailsPresenter;
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponent;
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.framework.di.activity.ActivityModule;
import com.github.mrmitew.bodylog.framework.di.activity.ActivityScope;
import com.github.mrmitew.bodylog.framework.profile_details.main.view.ProfileDetailsActivity;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Named;
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
        @Named("loadProfileInteractorRelay")
        BehaviorRelay<ResultState> providesLoadProfileInteractorRelay() {
            return BehaviorRelay.create();
        }

        @Provides
        ProfileDetailsPresenter providesProfileDetailsPresenter(LoadProfileInteractor loadProfileInteractor, @Named("loadProfileInteractorRelay") BehaviorRelay<ResultState> resultStateBehaviorRelay) {
            return new ProfileDetailsPresenter(loadProfileInteractor, resultStateBehaviorRelay);
        }

        @Provides
        LastUpdatedPresenter providesLastUpdatedPresenter(LoadProfileInteractor loadProfileInteractor, @Named("loadProfileInteractorRelay") BehaviorRelay<ResultState> resultStateBehaviorRelay) {
            return new LastUpdatedPresenter(loadProfileInteractor, resultStateBehaviorRelay);
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