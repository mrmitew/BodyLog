package com.github.mrmitew.bodylog.data.repository;

import com.github.mrmitew.bodylog.domain.repository.Repository;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;
import com.jakewharton.rxrelay2.BehaviorRelay;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class ProfileRepository implements Repository {
    private final BehaviorRelay<Profile> mProfileBehaviorRelay;

    private static Profile sCachedProfile = Profile.builder()
            .name("John Doe")
            .description("With hard work you can achieve your goals and you can become successful!")
            .weight(70)
            .bodyFat(8)
            .backSize(120)
            .chestSize(100)
            .armsSize(40)
            .waistSize(74.5f)
            .build();

    public ProfileRepository() {
        mProfileBehaviorRelay = BehaviorRelay.create();
    }

    @Override
    @NotNull
    public Observable<Profile> getProfile() {
        return Observable.just(sCachedProfile)
                // Simulate a long process
                .delay(1500, TimeUnit.MILLISECONDS);
    }

    @Override
    @NotNull
    public Observable<Profile> getProfileRefreshing() {
        return mProfileBehaviorRelay.startWith(sCachedProfile)
                // Simulate a long process
                .delay(1500, TimeUnit.MILLISECONDS);
    }

    @Override
    @NotNull
    public Completable setProfile(@NotNull final Profile profile) {
        return Completable.fromAction(() -> sCachedProfile = profile)
                .doOnComplete(() -> mProfileBehaviorRelay.accept(profile));
    }
}
