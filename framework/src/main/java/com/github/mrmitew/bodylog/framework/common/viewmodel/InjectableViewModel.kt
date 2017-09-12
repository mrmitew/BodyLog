package com.github.mrmitew.bodylog.framework.common.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

import com.github.mrmitew.bodylog.framework.AndroidApplication
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector


abstract class InjectableViewModel(application: Application) : AndroidViewModel(application) {
    init {
        setupActivityComponent()
    }

    private fun setupActivityComponent() =
        injectMembers((getApplication<Application>() as AndroidApplication).presenterHolderInjector)

    protected abstract fun injectMembers(injector: PresenterHolderInjector)
}