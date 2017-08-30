package com.github.mrmitew.bodylog.profile_edit.di;

import com.github.mrmitew.bodylog.adapter.profile_common.interactor.CheckRequiredFieldsInteractor;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.interactor.SaveProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.presenter.ProfileEditPresenter;
import com.github.mrmitew.bodylog.di.activity.ActivityComponent;
import com.github.mrmitew.bodylog.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.di.activity.ActivityModule;
import com.github.mrmitew.bodylog.di.activity.ActivityScope;
import com.github.mrmitew.bodylog.profile_edit.view.ProfileEditActivity;
import com.jakewharton.rxrelay2.BehaviorRelay;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {
                ProfileEditActivityComponent.ProfileEditActivityModule.class,
        }
)
public interface ProfileEditActivityComponent extends ActivityComponent<ProfileEditActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ProfileEditActivityComponent.ProfileEditActivityModule, ProfileEditActivityComponent> {
    }

    @Module
    class ProfileEditActivityModule extends ActivityModule<ProfileEditActivity> {
        public ProfileEditActivityModule(ProfileEditActivity activity) {
            super(activity);
        }

        @Provides
        @ActivityScope
        ProfileEditPresenter providesProfileEditPresenter(LoadProfileInteractor loadProfileInteractor,
                                                          CheckRequiredFieldsInteractor checkRequiredFieldsInteractor,
                                                          SaveProfileInteractor saveProfileInteractor) {
            return new ProfileEditPresenter(provideActivity(), loadProfileInteractor, checkRequiredFieldsInteractor, saveProfileInteractor, BehaviorRelay.create());
        }
    }
}