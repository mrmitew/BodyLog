package com.github.mrmitew.bodylog.repository.di;

import com.github.mrmitew.bodylog.data.repository.ProfileRepository;
import com.github.mrmitew.bodylog.domain.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    @Singleton
    Repository providesRepository() {
        return new ProfileRepository();
    }
}