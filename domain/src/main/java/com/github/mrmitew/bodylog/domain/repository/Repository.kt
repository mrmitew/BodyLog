package com.github.mrmitew.bodylog.domain.repository

import com.github.mrmitew.bodylog.domain.repository.entity.Profile

import io.reactivex.Completable
import io.reactivex.Observable

interface Repository {
    fun getProfile(): Observable<Profile>

    fun getProfileRefreshing(): Observable<Profile>

    fun setProfile(profile: Profile): Completable
}
