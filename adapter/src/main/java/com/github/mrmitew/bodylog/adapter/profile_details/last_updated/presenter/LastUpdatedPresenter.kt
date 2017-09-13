package com.github.mrmitew.bodylog.adapter.profile_details.last_updated.presenter

import com.github.mrmitew.bodylog.adapter.common.model.ResultState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.intent.GetProfileLastUpdatedIntent
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.model.LastUpdatedTextState
import com.github.mrmitew.bodylog.adapter.profile_details.last_updated.view.LastUpdatedView
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import javax.inject.Inject

class LastUpdatedPresenter
@Inject constructor(private val loadProfileInteractor: LoadProfileInteractor,
                    private val profileResultStateRelay: BehaviorRelay<ResultState>) :
        DetachableMviPresenter<LastUpdatedView, LastUpdatedTextState>(LastUpdatedView.Empty()) {

    override fun initialState(): LastUpdatedTextState = LastUpdatedTextState.Factory.idle()

    override fun viewIntents(): Observable<UIIntent> =
            if (view != null) view!!.getProfileLastUpdatedIntent().cast(UIIntent::class.java) else Observable.empty<UIIntent>()

    override fun bindInternalIntents() {
        super.bindInternalIntents()
        modelGateways.add(Observable.just(LoadProfileIntent())
                .compose(loadProfileInteractor)
                .doOnNext { println("[LAST_UPDATED] [PROFILE MODEL] (${it.hashCode()}) : $it") }
                .subscribe(profileResultStateRelay))
    }

    override fun createResultStateObservable(uiIntentStream: Observable<UIIntent>): Observable<ResultState> =
            uiIntentStream.publish { shared -> shared.ofType(GetProfileLastUpdatedIntent::class.java).flatMap { profileResultStateRelay } }

    override fun createViewState(previousState: LastUpdatedTextState, resultState: ResultState): LastUpdatedTextState {
        when (resultState) {
            is LoadProfileInteractor.State ->
                when {
                    resultState.isInProgress -> // We are not going to indicate we are currently fetching new data,
                        // nor will replace the old one with something, but we'll just clear the error field (if there was one)
                        return if (previousState.error !== StateError.Empty.INSTANCE)
                            previousState.copy(error = StateError.Empty.INSTANCE)
                        else
                            previousState

                    // No error? Then, just emit the old state. No view state changes needed to be done here.
                    resultState.isSuccessful -> return previousState.copy(
                            lastUpdated = LastUpdatedTextState.Factory.DATE_FORMAT.format(resultState.profile.timestamp),
                            error = StateError.Empty.INSTANCE)

                    resultState.error !is StateError.Empty -> return previousState.copy(
                            lastUpdated = LastUpdatedTextState.Factory.DEFAULT_VALUE,
                            error = resultState.error)
                }
        }

        throw IllegalArgumentException("Unknown partial state: " + resultState)
    }
}