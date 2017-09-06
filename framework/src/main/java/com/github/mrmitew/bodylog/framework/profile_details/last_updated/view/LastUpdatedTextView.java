package com.github.mrmitew.bodylog.framework.profile_details.last_updated.view;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.intent.GetProfileLastUpdatedIntent;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.model.LastUpdatedTextState;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.presenter.LastUpdatedPresenter;
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.view.LastUpdatedView;
import com.github.mrmitew.bodylog.framework.common.view.BasePresentableTextView;
import com.github.mrmitew.bodylog.framework.common.view.BasePresenterHolder;
import com.github.mrmitew.bodylog.framework.di.presenter.PresenterHolderInjector;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LastUpdatedTextView extends BasePresentableTextView<LastUpdatedView, LastUpdatedTextState> implements LastUpdatedView {
    public static class PresenterHolder extends BasePresenterHolder<LastUpdatedView, LastUpdatedTextState> {
        @Inject
        LastUpdatedPresenter mPresenter;

        public PresenterHolder(final Application application) {
            super(application);
        }

        @Override
        public LastUpdatedPresenter getPresenter() {
            return mPresenter;
        }

        @Override
        protected void injectMembers(final PresenterHolderInjector injector) {
            injector.inject(this);
        }
    }

    private static final String TAG = "LastUpdatedTextView";

    public LastUpdatedTextView(final Context context) {
        super(context);
    }

    public LastUpdatedTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public LastUpdatedTextView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LastUpdatedView getView() {
        return this;
    }

    @Override
    public PresenterHolder injectPresenterHolder() {
        return ViewModelProviders.of(((AppCompatActivity) getContext())).get(PresenterHolder.class);
    }

    @Override
    public Observable<GetProfileLastUpdatedIntent> getProfileLastUpdatedIntent() {
        return Observable.just(new GetProfileLastUpdatedIntent());
    }

    @Override
    public void render(final LastUpdatedTextState state) {
        if(state.error() != StateError.Empty.INSTANCE) {
            // TODO: 9/6/17
            Log.e(TAG, "render: ", state.error());
        }

        setText(state.lastUpdated());
    }
}
