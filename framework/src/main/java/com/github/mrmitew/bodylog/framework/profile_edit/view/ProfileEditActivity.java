package com.github.mrmitew.bodylog.framework.profile_edit.view;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.mrmitew.bodylog.R;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.model.ProfileEditState;
import com.github.mrmitew.bodylog.adapter.profile_edit.presenter.ProfileEditPresenter;
import com.github.mrmitew.bodylog.adapter.profile_edit.view.ProfileEditView;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.github.mrmitew.bodylog.framework.common.view.BaseActivity;
import com.github.mrmitew.bodylog.framework.common.view.BasePresenterHolder;
import com.github.mrmitew.bodylog.framework.di.activity.HasActivitySubcomponentBuilders;
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector;
import com.github.mrmitew.bodylog.framework.profile_edit.di.ProfileEditActivityComponent;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class ProfileEditActivity extends BaseActivity implements ProfileEditView {
    public static class PresenterHolder extends BasePresenterHolder<ProfileEditView, ProfileEditState> {
        @Inject
        ProfileEditPresenter mPresenter;

        public PresenterHolder(final Application application) {
            super(application);
        }

        @Override
        public ProfileEditPresenter getPresenter() {
            return mPresenter;
        }

        @Override
        protected void injectMembers(final PresenterHolderInjector injector) {
            injector.inject(this);
        }
    }

    private static final String TAG = "ProfileEditActivity";
    
    private PresenterHolder mViewModel;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProfileEditActivity.class);
    }

    //
    // Profile details layout
    //

    @BindView(R.id.et_name)
    EditText mEtName;

    @BindView(R.id.et_description)
    EditText mEtDescription;

    @BindView(R.id.et_weight)
    EditText mEtWeight;

    @BindView(R.id.et_body_fat_percentage)
    EditText mEtBodyFat;

    @BindView(R.id.et_chest_size)
    EditText mEtChestSize;

    @BindView(R.id.et_back_size)
    EditText mEtBackSize;

    @BindView(R.id.et_arms_size)
    EditText mEtArmsSize;

    @BindView(R.id.et_waist_size)
    EditText mEtWaistSize;

    @BindView(R.id.btn_save)
    Button mBtnSave;

    //
    // State loading layout
    //

    @BindView(R.id.vg_state_result)
    ViewGroup mVgStateResult;

    //
    // State loading layout
    //

    @BindView(R.id.vg_state_loading)
    ViewGroup mVgStateLoading;

    //
    // State error layout
    //

    @BindView(R.id.vg_state_error)
    ViewGroup mVgStateError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(PresenterHolder.class);
    }

    @Override
    protected void injectMembers(final HasActivitySubcomponentBuilders hasActivitySubcomponentBuilders) {
        ((ProfileEditActivityComponent.Builder) hasActivitySubcomponentBuilders.getActivityComponentBuilder(ProfileEditActivity.class))
                .activityModule(new ProfileEditActivityComponent.ProfileEditActivityModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.attachView(this);
        mViewModel.bindIntents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.detachView();
    }

    @Override
    public void render(final ProfileEditState state) {
        // Widgets
        mBtnSave.setEnabled(state.getRequiredFieldsFilledIn() && state.getRequiredFieldsError().equals(StateError.Empty.INSTANCE));

        // Content
        inflate(state.getProfile());

        // Layout visibility
        mVgStateLoading.setVisibility(state.isInProgress() ? View.VISIBLE : View.GONE);
        mVgStateError.setVisibility(!state.isLoadSuccessful()
                || !state.getLoadError().equals(StateError.Empty.INSTANCE) ? View.VISIBLE : View.GONE);
        mVgStateResult.setVisibility(state.getLoadError().equals(StateError.Empty.INSTANCE) ? View.VISIBLE : View.GONE);

        if (!state.isSaveSuccessful() && !state.getSaveError().equals(StateError.Empty.INSTANCE)) {
            // TODO: 9/5/17 Give feedback to the user
            Log.e(TAG, "render: ", state.getSaveError());
        }
    }

    @Override
    public Observable<LoadProfileIntent> getLoadProfileIntent() {
        return Observable.just(new LoadProfileIntent());
    }

    public Observable<String> getNameIntent() {
        return RxTextView.textChanges(mEtName)
                .skip(1)
                .map(CharSequence::toString);
    }

    public Observable<String> getDescriptionIntent() {
        return RxTextView.textChanges(mEtDescription)
                .skip(1)
                .map(CharSequence::toString);
    }

    @Override
    public Observable<CheckRequiredFieldsIntent> getRequiredFieldsFilledInIntent() {
        return Observable.combineLatest(getNameIntent().map(name -> !TextUtils.isEmpty(name)),
                getDescriptionIntent().map(description -> !TextUtils.isEmpty(description)),
                (isNameFilledIn, isDescriptionFilledIn) -> isNameFilledIn && isDescriptionFilledIn)
                .distinctUntilChanged()
                .map(CheckRequiredFieldsIntent::new);
    }

    @Override
    public Observable<SaveProfileIntent> getSaveIntent() {
        return RxView.clicks(mBtnSave)
                .map(__ -> new SaveProfileIntent(Profile.builder()
                        .name(mEtName.getText().toString())
                        .description(mEtDescription.getText().toString())
                        .weight(Float.valueOf(mEtWeight.getText().toString()))
                        .bodyFat(Float.valueOf(mEtBodyFat.getText().toString()))
                        .armsSize(Float.valueOf(mEtArmsSize.getText().toString()))
                        .backSize(Float.valueOf(mEtBackSize.getText().toString()))
                        .chestSize(Float.valueOf(mEtChestSize.getText().toString()))
                        .waistSize(Float.valueOf(mEtWaistSize.getText().toString()))
                        .build()));
    }

    private void inflate(Profile profile) {
        mEtName.setText(profile.name());
        mEtDescription.setText(profile.description());
        mEtWeight.setText(String.valueOf(profile.weight()));
        mEtBodyFat.setText(String.valueOf(profile.bodyFat()));
        mEtBackSize.setText(String.valueOf(profile.backSize()));
        mEtChestSize.setText(String.valueOf(profile.chestSize()));
        mEtArmsSize.setText(String.valueOf(profile.armsSize()));
        mEtWaistSize.setText(String.valueOf(profile.waistSize()));
    }
}
