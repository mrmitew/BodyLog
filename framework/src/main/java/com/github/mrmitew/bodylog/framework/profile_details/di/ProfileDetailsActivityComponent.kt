package com.github.mrmitew.bodylog.framework.profile_details.di

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.presenter.LastUpdatedPresenter
import com.github.mrmitew.bodylog.adapter.profile_details.main.presenter.ProfileDetailsPresenter
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponent
import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponentBuilder
import com.github.mrmitew.bodylog.framework.di.activity.ActivityModule
import com.github.mrmitew.bodylog.framework.di.activity.ActivityScope
import com.github.mrmitew.bodylog.framework.profile_details.main.view.ProfileDetailsActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import javax.inject.Singleton

@ActivityScope
@Subcomponent(modules = arrayOf(ProfileDetailsActivityComponent.ProfileDetailsActivityModule::class))
interface ProfileDetailsActivityComponent : ActivityComponent<ProfileDetailsActivity> {
    @Singleton
    @Module
    class PresenterModule {
        @Provides
        @Named("loadProfileInteractorRelay")
        internal fun providesLoadProfileInteractorRelay(): BehaviorRelay<ResultState> {
            return BehaviorRelay.create()
        }

        @Provides
        internal fun providesProfileDetailsPresenter(loadProfileInteractor: LoadProfileInteractor, @Named("loadProfileInteractorRelay") resultStateBehaviorRelay: BehaviorRelay<ResultState>): ProfileDetailsPresenter {
            return ProfileDetailsPresenter(loadProfileInteractor, resultStateBehaviorRelay)
        }

        @Provides
        internal fun providesLastUpdatedPresenter(loadProfileInteractor: LoadProfileInteractor, @Named("loadProfileInteractorRelay") resultStateBehaviorRelay: BehaviorRelay<ResultState>): LastUpdatedPresenter {
            return LastUpdatedPresenter(loadProfileInteractor, resultStateBehaviorRelay)
        }
    }

    @Subcomponent.Builder
    interface Builder : ActivityComponentBuilder<ProfileDetailsActivityModule, ProfileDetailsActivityComponent>

    @ActivityScope
    @Module
    class ProfileDetailsActivityModule(activity: ProfileDetailsActivity) : ActivityModule<ProfileDetailsActivity>(activity)
}