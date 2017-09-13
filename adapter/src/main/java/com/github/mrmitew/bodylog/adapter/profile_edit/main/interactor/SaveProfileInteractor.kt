package com.github.mrmitew.bodylog.adapter.profile_edit.main.interactor

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent
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
class SaveProfileInteractor @Inject constructor(private val threadExecutor: ThreadExecutor,
                                                private val postExecutionThread: PostExecutionThread,
                                                private val repository: Repository) : ObservableTransformer<SaveProfileIntent, SaveProfileInteractor.State> {
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

    override fun apply(upstream: Observable<SaveProfileIntent>): Observable<State> =
            upstream
                    .concatMap { buildUseCaseObservable(it) }
                    .map { State.Factory.successful(it) }
                    .onErrorReturn { State.Factory.error(it) }
                    .startWith(State.Factory.inProgress())
                    .observeOn(postExecutionThread.getScheduler())

    internal fun getUseCaseObservable(profile: Profile): Observable<Profile> =
            repository.setProfile(profile)
                    .toObservable<Profile>()

    private fun buildUseCaseObservable(intent: SaveProfileIntent): Observable<Profile> =
            getUseCaseObservable(intent.profile)
                    .subscribeOn(Schedulers.from(threadExecutor))
}