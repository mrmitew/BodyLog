package com.github.mrmitew.bodylog.adapter.profile_details.main.presenter

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor
import com.github.mrmitew.bodylog.adapter.profile_details.main.model.ProfileDetailsState
import com.github.mrmitew.bodylog.adapter.profile_details.main.view.ProfileDetailsView
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import javax.inject.Inject

class ProfileDetailsPresenter @Inject constructor(
        // Loads a profile from the repository
        private val loadProfileInteractor: LoadProfileInteractor,
        /*
         * State relays are subscribed to the business logic (model) and will cache (and perhaps* emit) the latest changes in the
         * business logic.
         *
         * * If the View is not attached, the relays will keep a cached state of a particular result, which
         * will be emitted as soon as the View attaches once again.
         */
        private val profileResultStateRelay: BehaviorRelay<ResultState>)
    : DetachableMviPresenter<ProfileDetailsView, ProfileDetailsState>(ProfileDetailsView.Empty()) {

    override fun bindInternalIntents() {
        super.bindInternalIntents()
        modelGateways.add(Observable.just(LoadProfileIntent())
                .compose(loadProfileInteractor)
                .doOnNext { state -> println(String.format("[DETAILS] [PROFILE MODEL] (%s) : %s", state.hashCode(), state)) }
                .subscribe(profileResultStateRelay))
    }

    override fun createResultStateObservable(uiIntentStream: Observable<UIIntent>): Observable<ResultState> =
            uiIntentStream.publish { shared -> shared.ofType(LoadProfileIntent::class.java).flatMap { profileResultStateRelay } }

    override fun initialState(): ProfileDetailsState = ProfileDetailsState.Factory.inProgress()

    override fun createViewState(previousState: ProfileDetailsState, resultState: ResultState): ProfileDetailsState {
        if (resultState is LoadProfileInteractor.State) {
            when {
                resultState.isInProgress -> return previousState.copy(
                        inProgress = true,
                        loadSuccessful = false,
                        loadError = StateError.Empty.INSTANCE)
                resultState.isSuccessful -> return previousState.copy(
                        profile = resultState.profile,
                        inProgress = false,
                        loadSuccessful = true)
                resultState.error !is StateError.Empty -> return previousState.copy(
                        inProgress = false,
                        loadSuccessful = false,
                        loadError = resultState.error)
            }
        }

        throw IllegalArgumentException("Unknown partial state: " + resultState)
    }

    // FIXME: Ugh... no comment.
    override fun viewIntents(): Observable<UIIntent> {
        if (view != null && view is ProfileDetailsView) {
            return (view as ProfileDetailsView).getLoadProfileIntent().cast(UIIntent::class.java)
        }
        return Observable.empty<UIIntent>()
    }
}