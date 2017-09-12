package com.github.mrmitew.bodylog.framework.common.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.github.mrmitew.bodylog.framework.AndroidApplication
import com.github.mrmitew.bodylog.framework.di.activity.HasActivitySubcomponentBuilders

abstract class InjectableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupActivityComponent()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun injectMembers(hasActivitySubcomponentBuilders: HasActivitySubcomponentBuilders)

    private fun setupActivityComponent() {
        injectMembers(AndroidApplication.getActivitySubcomponentBuilders(application))
    }
}