package com.github.mrmitew.bodylog.adapter.profile_details.last_updated.view;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.intent.GetProfileLastUpdatedIntent;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.model.LastUpdatedTextState;

import io.reactivex.Observable;

public interface LastUpdatedView extends BaseView<LastUpdatedTextState> {
    Observable<GetProfileLastUpdatedIntent> getProfileLastUpdatedIntent();
}
