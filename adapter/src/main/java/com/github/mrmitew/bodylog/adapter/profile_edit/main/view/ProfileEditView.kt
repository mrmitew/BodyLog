package com.github.mrmitew.bodylog.adapter.profile_edit.main.view

import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.model.ProfileEditState
import io.reactivex.Observable

interface ProfileEditView : BaseView<ProfileEditState> {
    fun getLoadProfileIntent(): Observable<LoadProfileIntent>

    fun getSaveIntent(): Observable<SaveProfileIntent>

    fun getRequiredFieldsFilledInIntent(): Observable<CheckRequiredFieldsIntent>

    class Empty : ProfileEditView {
        override fun getLoadProfileIntent(): Observable<LoadProfileIntent> = Observable.empty()
        override fun getSaveIntent(): Observable<SaveProfileIntent> = Observable.empty()
        override fun getRequiredFieldsFilledInIntent(): Observable<CheckRequiredFieldsIntent> = Observable.empty()
        override fun render(state: ProfileEditState) {
            // no-op
        }
    }
}
