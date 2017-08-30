package com.github.mrmitew.bodylog.domain.repository.entity;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Profile {
    public static final Profile EMPTY = Profile.builder()
            .empty(true)
            .name("")
            .description("")
            .weight(0)
            .bodyFat(0)
            .backSize(0)
            .chestSize(0)
            .armsSize(0)
            .waistSize(0)
            .build();

    public abstract boolean empty();

    public abstract String name();

    public abstract String description();

    public abstract float weight();

    public abstract float bodyFat();

    public abstract float backSize();

    public abstract float chestSize();

    public abstract float armsSize();

    public abstract float waistSize();

    public static Builder builder() {
        final AutoValue_Profile.Builder builder = new AutoValue_Profile.Builder();
        builder.empty(false);
        return builder;
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder empty(final boolean newEmpty);

        public abstract Builder name(final String newName);

        public abstract Builder description(final String newDescription);

        public abstract Builder weight(final float newWeight);

        public abstract Builder bodyFat(final float newBodyFat);

        public abstract Builder backSize(final float newBackSize);

        public abstract Builder chestSize(final float newChestSize);

        public abstract Builder armsSize(final float newArmsSize);

        public abstract Builder waistSize(final float newWaistSize);

        public abstract Profile build();
    }
}
