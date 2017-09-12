package com.github.mrmitew.bodylog.adapter.profile_edit.main.interactor;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread;
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor;
import com.github.mrmitew.bodylog.domain.repository.Repository;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.google.auto.value.AutoValue;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class SaveProfileInteractor implements ObservableTransformer<SaveProfileIntent, SaveProfileInteractor.State> {
    @AutoValue
    public abstract static class State extends ResultState {
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
                    .error(StateError.Empty.Companion.getINSTANCE())
                    .build();
        }

        static State inProgress() {
            return State.builder()
                    .isInProgress(true)
                    .isSuccessful(false)
                    .profile(Profile.EMPTY)
                    .error(StateError.Empty.Companion.getINSTANCE())
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
