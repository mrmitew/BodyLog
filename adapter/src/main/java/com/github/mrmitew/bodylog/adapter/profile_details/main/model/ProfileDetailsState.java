package com.github.mrmitew.bodylog.adapter.profile_details.main.model;

import com.github.mrmitew.bodylog.adapter.common.UiState;
import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.google.auto.value.AutoValue;


@AutoValue
public abstract class ProfileDetailsState extends UiState {
    public static class Factory {
        public static ProfileDetailsState inProgress() {
            return builder()
                    .inProgress(true)
                    .loadSuccessful(false)
                    .profile(Profile.EMPTY)
                    .loadError(StateError.Empty.Companion.getINSTANCE())
                    .build();
        }

        public static ProfileDetailsState idle() {
            return builder()
                    .inProgress(false)
                    .loadSuccessful(false)
                    .profile(Profile.EMPTY)
                    .loadError(StateError.Empty.Companion.getINSTANCE())
                    .build();
        }
    }

    public abstract boolean inProgress();

    public abstract boolean loadSuccessful();

    public abstract Throwable loadError();

    public abstract Profile profile();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ProfileDetailsState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder inProgress(final boolean inProgress);

        public abstract Builder loadSuccessful(final boolean loadSuccsessful);

        public abstract Builder loadError(final Throwable loadError);

        public abstract Builder profile(final Profile getProfile);

        public abstract ProfileDetailsState build();
    }
}