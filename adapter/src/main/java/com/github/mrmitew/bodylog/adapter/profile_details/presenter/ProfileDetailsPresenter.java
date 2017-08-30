package com.github.mrmitew.bodylog.adapter.profile_details.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.presenter.BaseMviPresenter;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_details.model.ProfileDetailsState;
import com.github.mrmitew.bodylog.adapter.profile_details.view.ProfileDetailsView;
import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;

public class ProfileDetailsPresenter extends BaseMviPresenter<ProfileDetailsState> {
    //
    // View
    //
    private final ProfileDetailsView mProfileDetailsView;

    //
    // Interactors
    //
    private LoadProfileInteractor mLoadProfileInteractor;

    //
    // Relays
    //
    private final BehaviorRelay<PartialState> mProfilePartialStateRelay;

    public ProfileDetailsPresenter(final ProfileDetailsView profileDetailsView,
                                   final LoadProfileInteractor loadProfileInteractor,
                                   final BehaviorRelay<PartialState> profilePartialStateRelay) {
        mProfileDetailsView = profileDetailsView;
        mLoadProfileInteractor = loadProfileInteractor;
        mProfilePartialStateRelay = profilePartialStateRelay;
    }

    @Override
    protected void bindInternalIntents() {
        super.bindInternalIntents();
        mModelGateways.add(Observable.just(new LoadProfileIntent())
                .compose(mLoadProfileInteractor)
                .doOnNext(state -> System.out.println(String.format("[DETAILS] [MODEL] (%s) : %s", state.hashCode(), state)))
                .subscribe(mProfilePartialStateRelay));
    }

    @Override
    protected Observable<PartialState> createPartialStateObservable(final Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable
                .publish(shared -> shared.ofType(LoadProfileIntent.class).flatMap(__ -> mProfilePartialStateRelay));
    }

    @Override
    protected ProfileDetailsState createState(final ProfileDetailsState previousState, final PartialState partialState) {
        if (partialState instanceof LoadProfileInteractor.State) {
            if (partialState.isInProgress()) {
                return previousState.toBuilder()
                        .inProgress(true)
                        .loadSuccessful(false)
                        .loadError(StateError.Empty.INSTANCE)
                        .build();
            } else if (partialState.isSuccessful()) {
                return previousState.toBuilder()
                        .inProgress(false)
                        .loadSuccessful(true)
                        .profile(((LoadProfileInteractor.State) partialState).profile())
                        .build();
            } else if (!(partialState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .inProgress(false)
                        .loadSuccessful(false)
                        .loadError(partialState.error())
                        .build();
            }
        }

        throw new IllegalArgumentException("Unknown partial state: " + partialState);
    }

    @Override
    protected ProfileDetailsState getInitialState() {
        return ProfileDetailsState.Factory.inProgress();
    }

    @Override
    protected Observable<UIIntent> getViewIntents() {
        return mProfileDetailsView.getLoadProfileIntent()
                .cast(UIIntent.class);
    }

    @Override
    protected BaseView<ProfileDetailsState> getView() {
        return mProfileDetailsView;
    }
}
