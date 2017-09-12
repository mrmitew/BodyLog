package com.github.mrmitew.bodylog.adapter.profile_details.main.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_details.main.model.ProfileDetailsState;
import com.github.mrmitew.bodylog.adapter.profile_details.main.view.ProfileDetailsView;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ProfileDetailsPresenter extends DetachableMviPresenter<ProfileDetailsView, ProfileDetailsState> {
    //
    // Interactors
    //

    /**
     * Loads a profile from the repository
     */
    private LoadProfileInteractor mLoadProfileInteractor;

    //
    // State relays
    //

    /*
     * State relays are subscribed to the business logic (model) and will cache (and perhaps* emit) the latest changes in the
     * business logic.
     *
     * * If the View is not attached, the relays will keep a cached state of a particular result, which
     * will be emitted as soon as the View attaches once again.
     */

    /**
     * Profile state relay
     */
    private final BehaviorRelay<ResultState> mProfileResultStateRelay;

    @Inject
    public ProfileDetailsPresenter(final LoadProfileInteractor loadProfileInteractor,
                                   final BehaviorRelay<ResultState> profileResultStateRelay) {
        super(null);
        mLoadProfileInteractor = loadProfileInteractor;
        mProfileResultStateRelay = profileResultStateRelay;
    }

    @Override
    protected void bindInternalIntents() {
        super.bindInternalIntents();
        getModelGateways().add(Observable.just(new LoadProfileIntent())
                .compose(mLoadProfileInteractor)
                .doOnNext(state -> System.out.println(String.format("[DETAILS] [PROFILE MODEL] (%s) : %s", state.hashCode(), state)))
                .subscribe(mProfileResultStateRelay));
    }

    @Override
    protected Observable<ResultState> createResultStateObservable(final Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable
                .publish(shared -> shared.ofType(LoadProfileIntent.class).flatMap(__ -> mProfileResultStateRelay));
    }

    @Override
    protected ProfileDetailsState createViewState(final ProfileDetailsState previousState, final ResultState resultState) {
        if (resultState instanceof LoadProfileInteractor.State) {
            if (resultState.isInProgress()) {
                return previousState.toBuilder()
                        .inProgress(true)
                        .loadSuccessful(false)
                        .loadError(StateError.Empty.INSTANCE)
                        .build();
            } else if (resultState.isSuccessful()) {
                return previousState.toBuilder()
                        .inProgress(false)
                        .loadSuccessful(true)
                        .profile(((LoadProfileInteractor.State) resultState).profile())
                        .build();
            } else if (!(resultState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .inProgress(false)
                        .loadSuccessful(false)
                        .loadError(resultState.error())
                        .build();
            }
        }

        throw new IllegalArgumentException("Unknown partial state: " + resultState);
    }

    @Override
    protected ProfileDetailsState initialState() {
        return ProfileDetailsState.Factory.inProgress();
    }

    @Override
    protected Observable<UIIntent> viewIntents() {
        return getView() != null ? getView().getLoadProfileIntent()
                .cast(UIIntent.class) : Observable.empty();
    }
}
