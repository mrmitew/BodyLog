package com.github.mrmitew.bodylog.adapter.profile_edit.main.model;

import com.github.mrmitew.bodylog.adapter.common.model.StateError;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ProfileEditState {
    public static class DefaultStateFactories {
        public static ProfileEditState idle() {
            return builder()
                    .setInProgress(false)
                    .setLoadSuccessful(false)
                    .setProfile(Profile.EMPTY)
                    .setLoadError(StateError.Empty.INSTANCE)
                    .setRequiredFieldsFilledIn(false)
                    .setRequiredFieldsError(StateError.Empty.INSTANCE)
                    .setSaveSuccessful(false)
                    .setSaveError(StateError.Empty.INSTANCE)
                    .build();
        }

        public static ProfileEditState empty() {
            return builder()
                    .setInProgress(false)
                    .setLoadSuccessful(false)
                    .setProfile(Profile.EMPTY)
                    .setLoadError(new Throwable("Empty result"))
                    .setRequiredFieldsFilledIn(false)
                    .setRequiredFieldsError(StateError.Empty.INSTANCE)
                    .setSaveSuccessful(false)
                    .setSaveError(StateError.Empty.INSTANCE)
                    .build();
        }

        public static ProfileEditState inProgress() {
            return builder()
                    .setInProgress(true)
                    .setLoadSuccessful(false)
                    .setProfile(Profile.EMPTY)
                    .setLoadError(StateError.Empty.INSTANCE)
                    .setRequiredFieldsFilledIn(false)
                    .setRequiredFieldsError(StateError.Empty.INSTANCE)
                    .setSaveSuccessful(false)
                    .setSaveError(StateError.Empty.INSTANCE)
                    .build();
        }

        public static ProfileEditState successful(Profile profile) {
            return builder()
                    .setInProgress(false)
                    .setLoadSuccessful(true)
                    .setProfile(profile)
                    .setLoadError(StateError.Empty.INSTANCE)
                    .setRequiredFieldsFilledIn(false)
                    .setRequiredFieldsError(StateError.Empty.INSTANCE)
                    .setSaveSuccessful(false)
                    .setSaveError(StateError.Empty.INSTANCE)
                    .build();
        }

        public static ProfileEditState error(Throwable throwable) {
            return builder()
                    .setInProgress(false)
                    .setLoadSuccessful(false)
                    .setProfile(Profile.EMPTY)
                    .setLoadError(throwable)
                    .setRequiredFieldsFilledIn(false)
                    .setRequiredFieldsError(StateError.Empty.INSTANCE)
                    .setSaveSuccessful(false)
                    .setSaveError(StateError.Empty.INSTANCE)
                    .build();
        }
    }

    public abstract boolean isInProgress();

    public abstract boolean isLoadSuccessful();

    public abstract Throwable getLoadError();

    public abstract boolean isSaveSuccessful();

    public abstract Throwable getSaveError();

    public abstract boolean getRequiredFieldsFilledIn();

    public abstract Throwable getRequiredFieldsError();

    public abstract Profile getProfile();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ProfileEditState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setInProgress(final boolean isInProgress);

        public abstract Builder setLoadSuccessful(final boolean isLoadSuccsessful);

        public abstract Builder setSaveSuccessful(final boolean isSaveSuccessful);

        public abstract Builder setRequiredFieldsFilledIn(final boolean areRequiredFieldsFilledIn);

        public abstract Builder setLoadError(final Throwable loadError);

        public abstract Builder setSaveError(final Throwable saveError);

        public abstract Builder setRequiredFieldsError(final Throwable requiredFieldsError);

        public abstract Builder setProfile(final Profile getProfile);

        public abstract ProfileEditState build();
    }
}