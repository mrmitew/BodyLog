package com.github.mrmitew.bodylog.common.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mrmitew.bodylog.AndroidApplication;
import com.github.mrmitew.bodylog.di.presenter.PresenterInjector;


public abstract class InjectableViewModel extends AndroidViewModel {
    public InjectableViewModel(final Application application) {
        super(application);
        setupActivityComponent();
    }

    private void setupActivityComponent() {
        injectMembers(((AndroidApplication)getApplication()).getPresenterHolderInjector());
    }

    protected abstract void injectMembers(PresenterInjector presenterInjector);
}
