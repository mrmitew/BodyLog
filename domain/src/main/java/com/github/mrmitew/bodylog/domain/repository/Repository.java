package com.github.mrmitew.bodylog.domain.repository;

import com.github.mrmitew.bodylog.domain.repository.entity.Profile;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface Repository {
    Observable<Profile> getProfile();

    Observable<Profile> getProfileRefreshing();

    Completable setProfile(Profile profile);
}
