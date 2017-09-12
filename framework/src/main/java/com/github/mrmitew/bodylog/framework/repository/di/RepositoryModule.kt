package com.github.mrmitew.bodylog.framework.repository.di

import com.github.mrmitew.bodylog.data.repository.ProfileRepository
import com.github.mrmitew.bodylog.domain.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton internal fun providesRepository(): Repository = ProfileRepository()
}