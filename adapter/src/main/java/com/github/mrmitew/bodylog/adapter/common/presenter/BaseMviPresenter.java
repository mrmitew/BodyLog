package com.github.mrmitew.bodylog.adapter.common.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseMviPresenter<V extends BaseView<S>, S> implements BasePresenter, Disposable {
    /**
     * Have the model gateways been init already
     */
    private boolean mIsInit;

    /**
     * Last emitted state
     */
    private S mLastState;

    /**
     * Gateways from views to business logic
     */
    protected final CompositeDisposable mModelGateways;

    /**
     * Gateways from business logic to views
     */
    protected final CompositeDisposable mViewGateways;

    /**
     * View interface
     */
    protected V mView;

    protected abstract Observable<ResultState> createResultStateObservable(final Observable<UIIntent> uiIntentObservable);

    protected abstract S createViewState(final S previousState, final ResultState resultState);

    protected abstract S getInitialState();

    protected abstract Observable<UIIntent> getViewIntents();

    protected BaseMviPresenter(V view) {
        mView = view;
        mModelGateways = new CompositeDisposable();
        mViewGateways = new CompositeDisposable();
    }

    @Override
    public void bindIntents() {
        if(isDisposed()) {
            throw new RuntimeException("This presenter has already been disposed");
        }

        if (!mIsInit) {
            bindInternalIntents();
            mIsInit = true;
        }

        mViewGateways.add(reduce(getViewIntents())
                .subscribe(mView::render, throwable -> {
                    throw new RuntimeException(throwable);
                }));
    }


    @Override
    public void unbindIntents() {
        mViewGateways.clear();
    }

    @Override
    public void dispose() {
        mModelGateways.dispose();
        mViewGateways.dispose();
        mLastState = null;
    }

    @Override
    public boolean isDisposed() {
        return mModelGateways.isDisposed() || mViewGateways.isDisposed();
    }

    protected void bindInternalIntents() {
    }

    private Observable<S> reduce(Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable
                .compose(this::createResultStateObservable)
                .scan(mLastState == null ? getInitialState() : mLastState, this::createViewState)
                .distinctUntilChanged()
                .doOnNext(state -> mLastState = state)
                .doOnNext(state -> System.out.println("[RENDER] " + state));
    }
}
