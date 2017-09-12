package com.github.mrmitew.bodylog.adapter.profile_details.main.view

import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_details.main.model.ProfileDetailsState

import io.reactivex.Observable

interface ProfileDetailsView : BaseView<ProfileDetailsState> {
    fun getLoadProfileIntent(): Observable<LoadProfileIntent>

    class Empty : ProfileDetailsView {
        override fun render(state: ProfileDetailsState) {
            // no-op
        }

        override fun getLoadProfileIntent(): Observable<LoadProfileIntent> = Observable.empty()
    }
}