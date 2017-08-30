package com.github.mrmitew.bodylog.adapter.profile_edit.interactor;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread;
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor;
import com.github.mrmitew.bodylog.domain.repository.Repository;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.google.auto.value.AutoValue;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public class SaveProfileInteractor implements ObservableTransformer<SaveProfileIntent, SaveProfileInteractor.State> {
    @AutoValue
    public abstract static class State extends PartialState {
        public abstract Profile profile();

        public static Builder builder() {
            return new AutoValue_SaveProfileInteractor_State.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder profile(final Profile profile);

            public abstract Builder isInProgress(final boolean isInProgress);

            public abstract Builder isSuccessful(final boolean isSuccessful);

            public abstract Builder error(final Throwable error);

            public abstract State build();
        }

        static State successful(Profile profile) {
            return State.builder()
                    .isInProgress(false)
                    .isSuccessful(true)
                    .profile(profile)
                    .error(StateError.Empty.INSTANCE)
                    .build();
        }

        static State inProgress() {
            return State.builder()
                    .isInProgress(true)
                    .isSuccessful(false)
                    .profile(Profile.EMPTY)
                    .error(StateError.Empty.INSTANCE)
                    .build();
        }

        static State error(Throwable throwable) {
            return State.builder()
                    .isInProgress(false)
                    .isSuccessful(false)
                    .profile(Profile.EMPTY)
                    .error(throwable)
                    .build();
        }
    }

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final Repository mRepository;

    @Inject
    public SaveProfileInteractor(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 Repository repository) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mRepository = repository;
    }

    @Override
    public ObservableSource<State> apply(final Observable<SaveProfileIntent> upstream) {
        return upstream
                .concatMap(this::buildUseCaseObservable)
                .map(State::successful)
                .startWith(State.inProgress())
                .onErrorReturn(State::error)
                .observeOn(mPostExecutionThread.getScheduler());
    }

    private Observable<Profile> getUseCaseObservable(Profile profile) {
        return mRepository.setProfile(profile)
                .toObservable();
    }

    private Observable<Profile> buildUseCaseObservable(SaveProfileIntent intent) {
        return getUseCaseObservable(intent.getProfile())
                .subscribeOn(Schedulers.from(mThreadExecutor));
    }
}