package com.github.mrmitew.bodylog.profile_edit.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.adapter.common.presenter.Bindable;
import com.github.mrmitew.bodylog.adapter.common.presenter.HasDetachableView;
import com.github.mrmitew.bodylog.adapter.profile_edit.presenter.ProfileEditPresenter;
import com.github.mrmitew.bodylog.adapter.profile_edit.view.ProfileEditView;

import javax.inject.Inject;


public class ProfileEditViewModel extends AndroidViewModel implements Bindable, HasDetachableView<ProfileEditView> {
    @Inject
    ProfileEditPresenter mPresenter;

    public ProfileEditViewModel(final Application application) {
        super(application);
        ((AndroidApplication) application).getApplicationComponent().inject(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        detachView();
    }

    @Override
    public void attachView(final ProfileEditView view) {
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
