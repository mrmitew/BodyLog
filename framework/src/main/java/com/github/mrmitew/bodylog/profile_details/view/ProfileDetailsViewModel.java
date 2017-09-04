package com.github.mrmitew.bodylog.profile_details.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.adapter.common.presenter.Bindable;
import com.github.mrmitew.bodylog.adapter.common.presenter.HasDetachableView;
import com.github.mrmitew.bodylog.adapter.profile_details.presenter.ProfileDetailsPresenter;
import com.github.mrmitew.bodylog.adapter.profile_details.view.ProfileDetailsView;

import javax.inject.Inject;


public class ProfileDetailsViewModel extends AndroidViewModel implements Bindable, HasDetachableView<ProfileDetailsView> {
    @Inject
    ProfileDetailsPresenter mPresenter;

    public ProfileDetailsViewModel(final Application application) {
        super(application);
        ((AndroidApplication) application).getApplicationComponent().inject(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachView();
    }

    @Override
    public void attachView(final ProfileDetailsView view) {
        mPresenter.attachView(view);
    }

    @Override
    public void detachView() {
        mPresenter.detachView();
    }

    @Override
    public void bindIntents() {
        mPresenter.bindIntents();
    }

    @Override
    public void unbindIntents() {
        mPresenter.unbindIntents();
    }
}
