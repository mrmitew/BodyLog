package com.github.mrmitew.bodylog.adapter.profile_details.last_updated.view

import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.intent.GetProfileLastUpdatedIntent
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.model.LastUpdatedTextState

import io.reactivex.Observable

interface LastUpdatedView : BaseView<LastUpdatedTextState> {
    fun getProfileLastUpdatedIntent(): Observable<GetProfileLastUpdatedIntent>

    class Empty : LastUpdatedView {
        override fun render(state: LastUpdatedTextState) {
            // no-op
        }

        override fun getProfileLastUpdatedIntent(): Observable<GetProfileLastUpdatedIntent> = Observable.empty()
    }
}
