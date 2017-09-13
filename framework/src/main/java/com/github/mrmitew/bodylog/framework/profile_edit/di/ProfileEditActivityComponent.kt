package com.github.mrmitew.bodylog.framework.profile_edit.di

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.CheckRequiredFieldsInteractor
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor
import com.github.mrmitew.bodylog.adapter.profile_edit.main.interactor.SaveProfileInteractor
import com.github.mrmitew.bodylog.adapter.profile_edit.main.presenter.ProfileEditPresenter
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponent
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponentBuilder
import com.github.mrmitew.bodylog.framework.di.activity.ActivityModule
import com.github.mrmitew.bodylog.framework.di.activity.ActivityScope
import com.github.mrmitew.bodylog.framework.profile_edit.main.view.ProfileEditActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@ActivityScope
@Subcomponent(modules = arrayOf(ProfileEditActivityComponent.ProfileEditActivityModule::class))
interface ProfileEditActivityComponent : ActivityComponent<ProfileEditActivity> {
    @Singleton
    @Module
    class PresenterModule {
        @Provides
        internal fun providesProfileEditPresenter(loadProfileInteractor: LoadProfileInteractor,
                                                  checkRequiredFieldsInteractor: CheckRequiredFieldsInteractor,
                                                  saveProfileInteractor: SaveProfileInteractor): ProfileEditPresenter {
            return ProfileEditPresenter(loadProfileInteractor = loadProfileInteractor,
                    saveProfileInteractor = saveProfileInteractor,
                    checkRequiredFieldsInteractor = checkRequiredFieldsInteractor,
                    profileResultStateRelay = BehaviorRelay.create<ResultState>())
        }
    }

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<ProfileEditActivityComponent.ProfileEditActivityModule, ProfileEditActivityComponent>

    @ActivityScope
    @Module
    class ProfileEditActivityModule(activity: ProfileEditActivity) : ActivityModule<ProfileEditActivity>(activity)
}