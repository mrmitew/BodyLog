package com.github.mrmitew.bodylog.adapter.profile_common.interactor

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CheckRequiredFieldsInteractor @Inject constructor(private val postExecutionThread: PostExecutionThread)
    : ObservableTransformer<CheckRequiredFieldsIntent, CheckRequiredFieldsInteractor.State> {
    class Error {
        class FieldsNotFilledInThrowable : Throwable() {
            override fun toString(): String {
                return "FieldsNotFilledInThrowable{}"
            }
        }
    }

    data class State(override val isInProgress: Boolean,
                     override val isSuccessful: Boolean,
                     override val error: Throwable) : ResultState(isInProgress, isSuccessful, error) {
        object Factory {
            internal fun successful(): State =
                    State(isInProgress = false,
                            isSuccessful = true,
                            error = StateError.Empty.INSTANCE)

            internal fun error(throwable: Throwable): State =
                    State(isInProgress = false,
                            isSuccessful = false,
                            error = throwable)
        }
    }

    override fun apply(upstream: Observable<CheckRequiredFieldsIntent>): Observable<State> =
            upstream
                    .concatMap { getUseCaseObservable(it) }
                    .map { State.Factory.successful() }
                    .onErrorReturn({ State.Factory.error(it) })
                    .observeOn(postExecutionThread.getScheduler())

    internal fun getUseCaseObservable(intent: CheckRequiredFieldsIntent): Observable<State> =
            Observable.just(when (intent.isFilledIn) {
                true -> State.Factory.successful()
                false -> State.Factory.error(Error.FieldsNotFilledInThrowable())
            })
}