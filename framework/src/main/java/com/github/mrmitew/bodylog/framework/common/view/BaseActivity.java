package com.github.mrmitew.bodylog.framework.common.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import icepick.Icepick;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends InjectableActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void showToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }
}