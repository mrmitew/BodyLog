package com.github.mrmitew.bodylog.adapter.profile_edit.main.view

import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.model.ProfileEditState
import io.reactivex.Observable

interface ProfileEditView : BaseView<ProfileEditState> {
    val loadProfileIntent: Observable<LoadProfileIntent>

    val saveIntent: Observable<SaveProfileIntent>

    val requiredFieldsFilledInIntent: Observable<CheckRequiredFieldsIntent>
}
