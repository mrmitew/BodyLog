package com.github.mrmitew.bodylog.framework.di.presenter;

import com.github.mrmitew.bodylog.framework.profile_details.last_updated.view.LastUpdatedTextView;
import com.github.mrmitew.bodylog.framework.profile_details.main.view.ProfileDetailsActivity;
import com.github.mrmitew.bodylog.framework.profile_edit.main.view.ProfileEditActivity;

public interface PresenterHolderInjector {
    void inject(ProfileDetailsActivity.PresenterHolder target);

    void inject(ProfileEditActivity.PresenterHolder target);

    void inject(LastUpdatedTextView.PresenterHolder target);
}
