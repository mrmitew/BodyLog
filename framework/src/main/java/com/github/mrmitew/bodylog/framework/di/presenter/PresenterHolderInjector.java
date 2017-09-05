package com.github.mrmitew.bodylog.framework.di.presenter;

import com.github.mrmitew.bodylog.framework.profile_details.view.ProfileDetailsActivity;
import com.github.mrmitew.bodylog.framework.profile_edit.view.ProfileEditActivity;

public interface PresenterHolderInjector {
    void inject(ProfileDetailsActivity.PresenterHolder profileDetailsPresenterHolder);

    void inject(ProfileEditActivity.PresenterHolder profileEditPresenterHolder);
}
