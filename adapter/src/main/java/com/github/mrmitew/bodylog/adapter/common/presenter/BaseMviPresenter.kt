package com.github.mrmitew.bodylog.adapter.common.presenter

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent
import com.github.mrmitew.bodylog.adapter.common.view.BaseView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseMviPresenter<V : BaseView<S>, S : UiState>(
        /**
         * View interface
         */
        protected var view: V?) : BasePresenter, Disposable {

    /**
     * Have the model gateways been init already
     */
    private var isInit: Boolean

    /**
     * Last emitted state
     */
    private var lastState: S?

    /**
     * Gateways from views to business logic
     */
    protected val modelGateways: CompositeDisposable

    /**
     * Gateways from business logic to views
     */
    protected val viewGateways: CompositeDisposable

    init {
        isInit = false
        modelGateways = CompositeDisposable()
        viewGateways = CompositeDisposable()
        lastState = null
    }

    protected abstract fun createResultStateObservable(uiIntentStream: Observable<UIIntent>): Observable<ResultState>
    protected abstract fun createViewState(previousState: S, resultState: ResultState): S
    protected abstract fun viewIntents(): Observable<UIIntent>
    protected abstract fun initialState(): S

    override fun bindIntents() {
        check(!isDisposed)

        if (!isInit) {
            bindInternalIntents()
            isInit = true
        }

        viewGateways.add(reduce(viewIntents())
                .subscribe({ s -> view?.render(s) },
                        { t: Throwable -> throw RuntimeException(t) }))
    }

    override fun unbindIntents() {
        viewGateways.clear()
    }

    override fun dispose() {
        modelGateways.dispose()
        viewGateways.dispose()
        lastState = null
    }

    override fun isDisposed(): Boolean = modelGateways.isDisposed || viewGateways.isDisposed

    protected open fun bindInternalIntents() {}

    private fun reduce(uiIntentStream: Observable<UIIntent>): Observable<S> {
        return uiIntentStream
                .compose<ResultState> { this.createResultStateObservable(it) }
                .scan<S>(if (lastState == null) initialState() else lastState, { previousState, resultState -> createViewState(previousState, resultState) })
                .distinctUntilChanged()
                .doOnNext { state -> lastState = state }
                .doOnNext { state -> println("[RENDER] " + state) }
    }

}