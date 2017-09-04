package com.github.mrmitew.bodylog.adapter.profile_common.interactor;

import com.github.mrmitew.bodylog.adapter.common.model.PartialState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread;
import com.github.mrmitew.bodylog.domain.executor.ThreadExecutor;
import com.github.mrmitew.bodylog.domain.repository.Repository;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.google.auto.value.AutoValue;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class LoadProfileInteractor implements ObservableTransformer<LoadProfileIntent, LoadProfileInteractor.State> {
    @AutoValue
    public abstract static class State extends PartialState {
        public abstract Profile profile();

        public static Builder builder() {
            return new AutoValue_LoadProfileInteractor_State.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder isInProgress(final boolean isInProgress);

            public abstract Builder isSuccessful(final boolean isSuccessful);

            public abstract Builder profile(final Profile profile);

            public abstract Builder error(final Throwable error);

            public abstract State build();
        }

        public static State successful(final Profile profile) {
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
    public LoadProfileInteractor(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 Repository repository) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mRepository = repository;
    }

    @Override
    public Observable<State> apply(final Observable<LoadProfileIntent> upstream) {
        return upstream
                .concatMap(__ -> buildUseCaseObservable())
                .map(State::successful)
                .onErrorReturn(State::error)
                .observeOn(mPostExecutionThread.getScheduler())
                .startWith(State.inProgress());
    }

    Observable<Profile> getUseCaseObservable() {
        return mRepository.getProfileRefreshing();
    }

    private Observable<Profile> buildUseCaseObservable() {
        return getUseCaseObservable()
                .subscribeOn(Schedulers.from(mThreadExecutor));
    }
}
