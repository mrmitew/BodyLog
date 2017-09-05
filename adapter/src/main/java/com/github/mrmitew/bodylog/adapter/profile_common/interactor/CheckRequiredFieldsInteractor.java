package com.github.mrmitew.bodylog.adapter.profile_common.interactor;

import com.github.mrmitew.bodylog.adapter.common.model.ResultState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.domain.executor.PostExecutionThread;
import com.google.auto.value.AutoValue;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

@Singleton
public class CheckRequiredFieldsInteractor implements ObservableTransformer<CheckRequiredFieldsIntent, CheckRequiredFieldsInteractor.State> {
    @AutoValue
    public abstract static class State extends ResultState {
        public static class Error {
            public static final class FieldsNotFilledInThrowable extends Throwable {
                @Override
                public String toString() {
                    return "FieldsNotFilledInThrowable{}";
                }
            }
        }

        public static State successful() {
            return State.builder()
                    .isSuccessful(true)
                    .error(StateError.Empty.INSTANCE)
                    .build();
        }

        public static State error(Throwable throwable) {
            return State.builder()
                    .isSuccessful(false)
                    .error(throwable)
                    .build();
        }

        public static Builder builder() {
            final AutoValue_CheckRequiredFieldsInteractor_State.Builder builder =
                    new AutoValue_CheckRequiredFieldsInteractor_State.Builder();
            builder.isInProgress(false);
            return builder;
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder isInProgress(final boolean isInProgress);

            public abstract Builder isSuccessful(final boolean isSuccessful);

            public abstract Builder error(final Throwable error);

            public abstract State build();
        }
    }

    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public CheckRequiredFieldsInteractor(PostExecutionThread postExecutionThread) {
        mPostExecutionThread = postExecutionThread;
    }

    @Override
    public ObservableSource<State> apply(final Observable<CheckRequiredFieldsIntent> upstream) {
        return upstream
                .concatMap(intent -> Observable.just(intent.getIsFilledIn()
                        ? State.successful()
                        : State.error(new State.Error.FieldsNotFilledInThrowable())))
                .onErrorReturn(State::error)
                .observeOn(mPostExecutionThread.getScheduler());
    }
}
