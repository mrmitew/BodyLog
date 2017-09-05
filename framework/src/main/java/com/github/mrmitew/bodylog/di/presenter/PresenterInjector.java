package com.github.mrmitew.bodylog.di.presenter;

import com.github.mrmitew.bodylog.profile_details.view.ProfileDetailsActivity;
import com.github.mrmitew.bodylog.profile_edit.view.ProfileEditActivity;

public interface PresenterInjector {
    void inject(ProfileDetailsActivity.PresenterHolder profileDetailsPresenterHolder);

    void inject(ProfileEditActivity.PresenterHolder profileEditPresenterHolder);
}
