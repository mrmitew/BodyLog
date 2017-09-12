package com.github.mrmitew.bodylog.framework.di.application

import com.github.mrmitew.bodylog.framework.di.activity.ActivityComponentBuilder
import com.github.mrmitew.bodylog.framework.di.activity.ActivityKey
import com.github.mrmitew.bodylog.framework.profile_details.di.ProfileDetailsActivityComponent
import com.github.mrmitew.bodylog.framework.profile_details.main.view.ProfileDetailsActivity
import com.github.mrmitew.bodylog.framework.profile_edit.di.ProfileEditActivityComponent
import com.github.mrmitew.bodylog.framework.profile_edit.main.view.ProfileEditActivity

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * All components defined here will become subcomponents of the parent component - ApplicationComponent
 */
@Module(subcomponents = arrayOf(ProfileDetailsActivityComponent::class, ProfileEditActivityComponent::class))
internal abstract class ActivityBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(ProfileDetailsActivity::class)
    abstract fun profileDetailsActivityComponentBuilder(impl: ProfileDetailsActivityComponent.Builder): ActivityComponentBuilder<*, *>

    @Binds
    @IntoMap
    @ActivityKey(ProfileEditActivity::class)
    abstract fun profileEditActivityComponentBuilder(impl: ProfileEditActivityComponent.Builder): ActivityComponentBuilder<*, *>
}
