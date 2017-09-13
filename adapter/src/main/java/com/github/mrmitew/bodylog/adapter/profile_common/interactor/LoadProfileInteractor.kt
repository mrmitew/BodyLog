package com.github.mrmitew.bodylog.adapter.profile_common.interactor

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor
import com.github.mrmitew.bodylog.domain.repository.Repository
import com.github.mrmitew.bodylog.domain.repository.entity.Profile
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadProfileInteractor @Inject constructor(private val threadExecutor: ThreadExecutor,
                                                private val postExecutionThread: PostExecutionThread,
                                                private val repository: Repository) : ObservableTransformer<LoadProfileIntent, LoadProfileInteractor.State> {
    data class State(val profile: Profile,
                     override val isInProgress: Boolean,
                     override val isSuccessful: Boolean,
                     override val error: Throwable) : ResultState(isInProgress, isSuccessful, error) {
        object Factory {
            internal fun inProgress(): State =
                    State(Profile.Factory.EMPTY,
                            isInProgress = true,
                            isSuccessful = false,
                            error = StateError.Empty.INSTANCE)

            internal fun successful(profile: Profile): State =
                    State(profile,
                            isInProgress = false,
                            isSuccessful = true,
                            error = StateError.Empty.INSTANCE)

            internal fun error(throwable: Throwable): State =
                    State(Profile.Factory.EMPTY,
                            isInProgress = false,
                            isSuccessful = false,
                            error = throwable)
        }
    }

    override fun apply(upstream: Observable<LoadProfileIntent>): Observable<State> =
            upstream
                    .concatMap { buildUseCaseObservable() }
                    .map { State.Factory.successful(it) }
                    .onErrorReturn { State.Factory.error(it) }
                    .startWith(State.Factory.inProgress())
                    .observeOn(postExecutionThread.getScheduler())

    fun getUseCaseObservable(): Observable<Profile> =
            repository.getProfileRefreshing()


    private fun buildUseCaseObservable(): Observable<Profile> =
            getUseCaseObservable()
                    .subscribeOn(Schedulers.from(threadExecutor))

}