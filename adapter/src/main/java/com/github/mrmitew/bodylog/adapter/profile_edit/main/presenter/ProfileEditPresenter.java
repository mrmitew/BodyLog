package com.github.mrmitew.bodylog.adapter.profile_edit.main.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.presenter.DetachableMviPresenter;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.CheckRequiredFieldsInteractor;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.interactor.SaveProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.model.ProfileEditState;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.view.ProfileEditView;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ProfileEditPresenter extends DetachableMviPresenter<ProfileEditView, ProfileEditState> {
    //
    // Interactors
    //

    /**
     * Loads a profile from the repository
     */
    private final LoadProfileInteractor mLoadProfileInteractor;

    /**
     * Saves a profile into the repository
     */
    private final SaveProfileInteractor mSaveProfileInteractor;

    /**
     * Checks if all fields are properly filled in
     */
    private final CheckRequiredFieldsInteractor mCheckRequiredFieldsInteractor;

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
    public ProfileEditPresenter(final LoadProfileInteractor loadProfileInteractor,
                                final CheckRequiredFieldsInteractor checkRequiredFieldsInteractor,
                                final SaveProfileInteractor saveProfileInteractor,
                                final BehaviorRelay<ResultState> profileResultStateRelay) {
        super(new ProfileEditView.Empty());
        mLoadProfileInteractor = loadProfileInteractor;
        mCheckRequiredFieldsInteractor = checkRequiredFieldsInteractor;
        mSaveProfileInteractor = saveProfileInteractor;
        mProfileResultStateRelay = profileResultStateRelay;
    }

    @Override
    protected Observable<UIIntent> viewIntents() {
        final ProfileEditView view = getView();
        return view != null ? Observable.merge(view.getRequiredFieldsFilledInIntent(),
                view.getSaveIntent(),
                view.getLoadProfileIntent()) : Observable.empty();
    }

    @Override
    protected void bindInternalIntents() {
        super.bindInternalIntents();
        getModelGateways().add(Observable.just(new LoadProfileIntent())
                .compose(mLoadProfileInteractor)
                .doOnNext(state -> System.out.println(String.format("[EDIT] [PROFILE MODEL] (%s) : %s", state.hashCode(), state)))
                .subscribe(mProfileResultStateRelay));
    }

    @Override
    protected Observable<ResultState> createResultStateObservable(final Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable.publish(shared ->
                Observable.merge(
                        shared.ofType(LoadProfileIntent.class).flatMap(__ -> mProfileResultStateRelay),
                        shared.ofType(CheckRequiredFieldsIntent.class).compose(mCheckRequiredFieldsInteractor),
                        shared.ofType(SaveProfileIntent.class).compose(mSaveProfileInteractor)
                )
        );
    }

    @Override
    protected ProfileEditState createViewState(final ProfileEditState previousState, final ResultState resultState) {
        if (resultState instanceof LoadProfileInteractor.State) {
            if (resultState.isInProgress()) {
                return previousState.toBuilder()
                        .setInProgress(true)
                        .setLoadSuccessful(false)
                        .setLoadError(StateError.Empty.Companion.getINSTANCE())
                        .build();
            } else if (resultState.isSuccessful()) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setLoadSuccessful(true)
                        .setProfile(((LoadProfileInteractor.State) resultState).profile())
                        .build();
            } else if (!(resultState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setLoadSuccessful(false)
                        .setLoadError(resultState.error())
                        .build();
            }
        } else if (resultState instanceof SaveProfileInteractor.State) {
            if (resultState.isInProgress()) {
                return previousState.toBuilder()
                        .setInProgress(true)
                        .setSaveSuccessful(false)
                        .setSaveError(StateError.Empty.Companion.getINSTANCE())
                        .build();
            } else if (resultState.isSuccessful()) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setSaveSuccessful(true)
                        .build();
            } else if (!(resultState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setSaveSuccessful(false)
                        .setSaveError(resultState.error())
                        .build();
            }
        } else if (resultState instanceof CheckRequiredFieldsInteractor.State) {
            if (resultState.isSuccessful()) {
                return previousState.toBuilder()
                        .setRequiredFieldsFilledIn(true)
                        .setRequiredFieldsError(StateError.Empty.Companion.getINSTANCE())
                        .build();
            } else if (!(resultState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setRequiredFieldsFilledIn(false)
                        .setRequiredFieldsError(resultState.error())
                        .build();
            }
        }

        throw new IllegalArgumentException("Unknown partial state: " + resultState);
    }

    @Override
    protected ProfileEditState initialState() {
        return ProfileEditState.DefaultStateFactories.idle();
    }
}
