package com.github.mrmitew.bodylog.di.application;

import com.github.mrmitew.bodylog.di.activity.ActivityComponentBuilder;
import com.github.mrmitew.bodylog.di.activity.ActivityKey;
import com.github.mrmitew.bodylog.profile_details.di.ProfileDetailsActivityComponent;
import com.github.mrmitew.bodylog.profile_details.view.ProfileDetailsActivity;
import com.github.mrmitew.bodylog.profile_edit.di.ProfileEditActivityComponent;
import com.github.mrmitew.bodylog.profile_edit.view.ProfileEditActivity;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * All components defined here will become subcomponents of the parent component - ApplicationComponent
 */
@Module(
        subcomponents = {
                ProfileDetailsActivityComponent.class,
                ProfileEditActivityComponent.class,
        })
abstract class ActivityBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(ProfileDetailsActivity.class)
    public abstract ActivityComponentBuilder profileDetailsActivityComponentBuilder(ProfileDetailsActivityComponent.Builder impl);

    @Binds
    @IntoMap
    @ActivityKey(ProfileEditActivity.class)
    public abstract ActivityComponentBuilder profileEditActivityComponentBuilder(ProfileEditActivityComponent.Builder impl);
}
