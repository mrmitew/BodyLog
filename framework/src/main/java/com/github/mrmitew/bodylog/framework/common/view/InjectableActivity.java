package com.github.mrmitew.bodylog.framework.common.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mrmitew.bodylog.framework.AndroidApplication;
import com.github.mrmitew.bodylog.framework.di.activity.HasActivitySubcomponentBuilders;

public abstract class InjectableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupActivityComponent();
        super.onCreate(savedInstanceState);
    }

    protected void setupActivityComponent() {
        injectMembers(AndroidApplication.get(this));
    }

    protected abstract void injectMembers(HasActivitySubcomponentBuilders
                                                  hasActivitySubcomponentBuilders);
}