package com.github.mrmitew.bodylog.profile_details.view;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mrmitew.bodylog.R;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_details.model.ProfileDetailsState;
import com.github.mrmitew.bodylog.adapter.profile_details.presenter.ProfileDetailsPresenter;
import com.github.mrmitew.bodylog.adapter.profile_details.view.ProfileDetailsView;
import com.github.mrmitew.bodylog.common.view.BaseActivity;
import com.github.mrmitew.bodylog.common.view.BasePresenterHolder;
import com.github.mrmitew.bodylog.di.activity.HasActivitySubcomponentBuilders;
import com.github.mrmitew.bodylog.di.presenter.PresenterInjector;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.github.mrmitew.bodylog.profile_details.di.ProfileDetailsActivityComponent;
import com.github.mrmitew.bodylog.profile_edit.view.ProfileEditActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class ProfileDetailsActivity extends BaseActivity implements ProfileDetailsView {
    public static class PresenterHolder extends BasePresenterHolder<ProfileDetailsView, ProfileDetailsState> {
        @Inject
        ProfileDetailsPresenter mPresenter;

        public PresenterHolder(final Application application) {
            super(application);
        }

        @Override
        public ProfileDetailsPresenter getPresenter() {
            return mPresenter;
        }

        @Override
        protected void injectMembers(final PresenterInjector presenterInjector) {
            presenterInjector.inject(this);
        }
    }

    private static final String TAG = "ProfileDetailsActivity";

    //
    // Profile details layout
    //

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_description)
    TextView mTvDescription;

    @BindView(R.id.tv_weight)
    TextView mTvWeight;

    @BindView(R.id.tv_body_fat_percentage)
    TextView mTvBodyFat;

    @BindView(R.id.tv_chest_size)
    TextView mTvChestSize;

    @BindView(R.id.tv_back_size)
    TextView mTvBackSize;

    @BindView(R.id.tv_arms_size)
    TextView mTvArmsSize;

    @BindView(R.id.tv_waist_size)
    TextView mTvWaistSize;

    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    //
    // State loading layout
    //

    @BindView(R.id.vg_state_loading)
    ViewGroup mVgStateLoading;

    //
    // State no result layout
    //

    @BindView(R.id.vg_state_no_result)
    ViewGroup mVgStateNoResult;

    //
    // State error layout
    //

    @BindView(R.id.vg_state_error)
    ViewGroup mVgStateError;

    //
    // Instance variables
    //

    private PresenterHolder mPresenterHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        ButterKnife.bind(this);
        mPresenterHolder = ViewModelProviders.of(this).get(PresenterHolder.class);
    }

    @Override
    protected void injectMembers(final HasActivitySubcomponentBuilders hasActivitySubcomponentBuilders) {
        ((ProfileDetailsActivityComponent.Builder) hasActivitySubcomponentBuilders.getActivityComponentBuilder(ProfileDetailsActivity.class))
                .activityModule(new ProfileDetailsActivityComponent.ProfileDetailsActivityModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    public void render(final ProfileDetailsState state) {
        final boolean hasError = !(state.loadError() instanceof StateError.Empty);

        // Inflate the layout with the content from the state
        inflate(state.profile());

        if (hasError) {
            // TODO: 9/5/17 Give feedback to the user
            Log.e(TAG, "render: (hasError) ", state.loadError());
        }

        // Layout visibility
        mVgStateLoading.setVisibility(state.inProgress() ? View.VISIBLE : View.GONE);
        mVgStateNoResult.setVisibility(state.loadSuccessful() ? View.GONE : View.VISIBLE);
        mVgStateError.setVisibility(hasError ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.btn_edit)
    public void onEditRequest() {
        startActivity(ProfileEditActivity.getCallingIntent(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenterHolder.attachView(this);
        mPresenterHolder.bindIntents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenterHolder.detachView();
    }

    private void inflate(Profile profile) {
        mTvName.setText(profile.name());
        mTvDescription.setText(profile.description());
        mTvWeight.setText(String.valueOf(profile.weight()));
        mTvBodyFat.setText(String.valueOf(profile.bodyFat()));
        mTvBackSize.setText(String.valueOf(profile.backSize()));
        mTvChestSize.setText(String.valueOf(profile.chestSize()));
        mTvArmsSize.setText(String.valueOf(profile.armsSize()));
        mTvWaistSize.setText(String.valueOf(profile.waistSize()));
    }

    @Override
    public Observable<LoadProfileIntent> getLoadProfileIntent() {
        return Observable.just(new LoadProfileIntent());
    }
}