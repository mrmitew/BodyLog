package com.github.mrmitew.bodylog.framework.di.presenter

import com.github.mrmitew.bodylog.framework.profile_details.last_updated.view.LastUpdatedTextView
import com.github.mrmitew.bodylog.framework.profile_details.main.view.ProfileDetailsActivity
import com.github.mrmitew.bodylog.framework.profile_edit.main.view.ProfileEditActivity

interface PresenterHolderInjector {
    fun inject(target: ProfileDetailsActivity.PresenterHolder)

    fun inject(target: ProfileEditActivity.PresenterHolder)

    fun inject(target: LastUpdatedTextView.PresenterHolder)
}
