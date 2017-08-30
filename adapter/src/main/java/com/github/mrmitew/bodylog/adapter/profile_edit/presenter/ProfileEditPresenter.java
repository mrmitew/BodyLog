package com.github.mrmitew.bodylog.adapter.profile_edit.presenter;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.adapter.common.presenter.BaseMviPresenter;
import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.CheckRequiredFieldsInteractor;
import com.github.mrmitew.bodylog.adapter.profile_common.interactor.LoadProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.interactor.SaveProfileInteractor;
import com.github.mrmitew.bodylog.adapter.profile_edit.model.ProfileEditState;
import com.github.mrmitew.bodylog.adapter.profile_edit.view.ProfileEditView;
import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;

public class ProfileEditPresenter extends BaseMviPresenter<ProfileEditState> {
    //
    // View
    //
    private final ProfileEditView mProfileEditView;

    //
    // Interactors
    //
    private final LoadProfileInteractor mLoadProfileInteractor;
    private final CheckRequiredFieldsInteractor mCheckRequiredFieldsInteractor;
    private final SaveProfileInteractor mSaveProfileInteractor;

    //
    // Relays
    //
    private final BehaviorRelay<PartialState> mProfilePartialStateRelay;

    public ProfileEditPresenter(final ProfileEditView profileEditView,
                                final LoadProfileInteractor loadProfileInteractor,
                                final CheckRequiredFieldsInteractor checkRequiredFieldsInteractor,
                                final SaveProfileInteractor saveProfileInteractor,
                                final BehaviorRelay<PartialState> profilePartialStateRelay) {
        mProfileEditView = profileEditView;
        mLoadProfileInteractor = loadProfileInteractor;
        mCheckRequiredFieldsInteractor = checkRequiredFieldsInteractor;
        mSaveProfileInteractor = saveProfileInteractor;
        mProfilePartialStateRelay = profilePartialStateRelay;
    }

    @Override
    protected Observable<UIIntent> getViewIntents() {
        return Observable.merge(mProfileEditView.getRequiredFieldsFilledInIntent(),
                mProfileEditView.getSaveIntent(),
                mProfileEditView.getLoadProfileIntent());
    }

    @Override
    protected void bindInternalIntents() {
        super.bindInternalIntents();
        mModelGateways.add(Observable.just(new LoadProfileIntent())
                .compose(mLoadProfileInteractor)
                .doOnNext(state -> System.out.println(String.format("[EDIT] [MODEL] (%s) : %s", state.hashCode(), state)))
                .subscribe(mProfilePartialStateRelay));
    }

    @Override
    protected Observable<PartialState> createPartialStateObservable(final Observable<UIIntent> uiIntentObservable) {
        return uiIntentObservable.publish(shared ->
                Observable.merge(
                        shared.ofType(LoadProfileIntent.class).flatMap(__ -> mProfilePartialStateRelay),
                        shared.ofType(CheckRequiredFieldsIntent.class).compose(mCheckRequiredFieldsInteractor),
                        shared.ofType(SaveProfileIntent.class).compose(mSaveProfileInteractor)
                )
        );
    }

    @Override
    protected ProfileEditState createState(final ProfileEditState previousState, final PartialState partialState) {
        if (partialState instanceof LoadProfileInteractor.State) {
            if (partialState.isInProgress()) {
                return previousState.toBuilder()
                        .setInProgress(true)
                        .setLoadSuccessful(false)
                        .setLoadError(StateError.Empty.INSTANCE)
                        .build();
            } else if (partialState.isSuccessful()) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setLoadSuccessful(true)
                        .setProfile(((LoadProfileInteractor.State) partialState).profile())
                        .build();
            } else if (!(partialState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setLoadSuccessful(false)
                        .setLoadError(partialState.error())
                        .build();
            }
        } else if (partialState instanceof SaveProfileInteractor.State) {
            if (partialState.isInProgress()) {
                return previousState.toBuilder()
                        .setInProgress(true)
                        .setSaveSuccessful(false)
                        .setSaveError(StateError.Empty.INSTANCE)
                        .build();
            } else if (partialState.isSuccessful()) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setSaveSuccessful(true)
                        .build();
            } else if (!(partialState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setInProgress(false)
                        .setSaveSuccessful(false)
                        .setSaveError(partialState.error())
                        .build();
            }
        } else if (partialState instanceof CheckRequiredFieldsInteractor.State) {
            if (partialState.isSuccessful()) {
                return previousState.toBuilder()
                        .setRequiredFieldsFilledIn(true)
                        .setRequiredFieldsError(StateError.Empty.INSTANCE)
                        .build();
            } else if (!(partialState.error() instanceof StateError.Empty)) {
                return previousState.toBuilder()
                        .setRequiredFieldsFilledIn(false)
                        .setRequiredFieldsError(partialState.error())
                        .build();
            }
        }

        throw new IllegalArgumentException("Unknown partial state: " + partialState);
    }

    @Override
    protected ProfileEditState getInitialState() {
        return ProfileEditState.DefaultStateFactories.idle();
    }

    @Override
    protected BaseView<ProfileEditState> getView() {
        return mProfileEditView;
    }
}
