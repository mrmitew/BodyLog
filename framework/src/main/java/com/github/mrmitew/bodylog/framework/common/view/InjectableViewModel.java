package com.github.mrmitew.bodylog.framework.common.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.github.mrmitew.bodylog.framework.AndroidApplication;
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector;


public abstract class InjectableViewModel extends AndroidViewModel {
    public InjectableViewModel(final Application application) {
        super(application);
        setupActivityComponent();
    }

    private void setupActivityComponent() {
        injectMembers(((AndroidApplication)getApplication()).getPresenterHolderInjector());
    }

    protected abstract void injectMembers(PresenterHolderInjector presenterHolderInjector);
}
