package com.github.mrmitew.bodylog.adapter.profile_details.main.view;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_details.main.model.ProfileDetailsState;

import io.reactivex.Observable;

public interface ProfileDetailsView extends BaseView<ProfileDetailsState> {
    Observable<LoadProfileIntent> getLoadProfileIntent();
}
