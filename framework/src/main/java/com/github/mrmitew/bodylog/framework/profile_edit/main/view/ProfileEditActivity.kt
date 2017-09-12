package com.github.mrmitew.bodylog.framework.profile_edit.main.view

import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.mrmitew.bodylog.R
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_edit.main.model.ProfileEditState
import com.github.mrmitew.bodylog.adapter.profile_edit.main.presenter.ProfileEditPresenter
import com.github.mrmitew.bodylog.adapter.profile_edit.main.view.ProfileEditView
import com.github.mrmitew.bodylog.domain.repository.entity.Profile
import com.github.mrmitew.bodylog.framework.common.presenter.BasePresenterHolder
import com.github.mrmitew.bodylog.framework.common.view.BasePresentableActivity
import com.github.mrmitew.bodylog.framework.di.activity.HasActivitySubcomponentBuilders
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector
import com.github.mrmitew.bodylog.framework.profile_edit.di.ProfileEditActivityComponent
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject

class ProfileEditActivity : BasePresentableActivity<ProfileEditView, ProfileEditState>(), ProfileEditView {
    class PresenterHolder(application: Application) : BasePresenterHolder<ProfileEditView, ProfileEditState>(application) {
        @Inject override lateinit var presenter: ProfileEditPresenter

        override fun injectMembers(injector: PresenterHolderInjector) {
            injector.inject(this)
        }
    }

    companion object Factory {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ProfileEditActivity::class.java)
        }
    }

    override val view: ProfileEditView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
    }

    override fun render(state: ProfileEditState) {
        // Widgets
        btn_save.isEnabled = state.requiredFieldsFilledIn && state.requiredFieldsError == StateError.Empty.INSTANCE

        // Content
        inflate(state.profile)

        // Layout visibility
        vg_state_loading.visibility = if (state.isInProgress) View.VISIBLE else View.GONE
        vg_state_error.visibility =
                if (!state.isLoadSuccessful || state.loadError != StateError.Empty.INSTANCE)
                    View.VISIBLE
                else
                    View.GONE
        vg_state_result.visibility = if (state.loadError == StateError.Empty.INSTANCE) View.VISIBLE else View.GONE

        if (!state.isSaveSuccessful && state.saveError != StateError.Empty.INSTANCE) {
            // TODO: 9/5/17 Give feedback to the user
            println("render: " + state.saveError)
        }
    }

    override fun injectPresenterHolder(): BasePresenterHolder<ProfileEditView, ProfileEditState> {
        return ViewModelProviders.of(this).get(PresenterHolder::class.java)
    }

    override fun injectMembers(hasActivitySubcomponentBuilders: HasActivitySubcomponentBuilders) {
        (hasActivitySubcomponentBuilders.getActivityComponentBuilder(ProfileEditActivity::class.java) as ProfileEditActivityComponent.Builder)
                .activityModule(ProfileEditActivityComponent.ProfileEditActivityModule(this))
                .build()
                .injectMembers(this)
    }

    override fun getLoadProfileIntent(): Observable<LoadProfileIntent> =
            Observable.just(LoadProfileIntent())

    override fun getSaveIntent(): Observable<SaveProfileIntent> =
            RxView.clicks(btn_save)
                    .map {
                        SaveProfileIntent(Profile.builder()
                                .name(et_name.text.toString())
                                .description(et_description.text.toString())
                                .weight(et_weight.text.toString().toFloat())
                                .bodyFat(et_body_fat_percentage.text.toString().toFloat())
                                .armsSize(et_arms_size.text.toString().toFloat())
                                .backSize(et_back_size.text.toString().toFloat())
                                .chestSize(et_chest_size.text.toString().toFloat())
                                .waistSize(et_waist_size.text.toString().toFloat())
                                .build())
                    }

    override fun getRequiredFieldsFilledInIntent(): Observable<CheckRequiredFieldsIntent> =
            Observable.combineLatest(
                    getNameIntent().map { name -> name.isNotEmpty() },
                    getDescriptionIntent().map { description -> description.isNotEmpty() },
                    BiFunction({ isNameFilledIn: Boolean, isDescriptionFilledIn: Boolean -> isNameFilledIn && isDescriptionFilledIn }))
                    .distinctUntilChanged()
                    .map { CheckRequiredFieldsIntent(it) }


    private fun getNameIntent(): Observable<String> {
        return RxTextView.textChanges(et_name)
                .skip(1)
                .map { it.toString() }
    }

    private fun getDescriptionIntent(): Observable<String> {
        return RxTextView.textChanges(et_description)
                .skip(1)
                .map { it.toString() }
    }

    private fun inflate(profile: Profile) {
        et_name.setText(profile.name())
        et_description.setText(profile.description())
        et_weight.setText(profile.weight().toString())
        et_body_fat_percentage.setText(profile.bodyFat().toString())
        et_back_size.setText(profile.backSize().toString())
        et_chest_size.setText(profile.chestSize().toString())
        et_arms_size.setText(profile.armsSize().toString())
        et_waist_size.setText(profile.waistSize().toString())
    }
}