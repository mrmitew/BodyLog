package com.github.mrmitew.bodylog.adapter.profile_details.main.model

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.domain.repository.entity.Profile

data class ProfileDetailsState(val profile: Profile,
                               val inProgress: Boolean,
                               val loadSuccessful: Boolean,
                               val loadError: Throwable) : UiState() {
    object Factory {
        fun inProgress(): ProfileDetailsState =
                ProfileDetailsState(profile = Profile.Factory.EMPTY,
                        inProgress = true,
                        loadSuccessful = false,
                        loadError = StateError.Empty.INSTANCE)

        fun idle(): ProfileDetailsState =
                ProfileDetailsState(profile = Profile.Factory.EMPTY,
                        inProgress = false,
                        loadSuccessful = false,
                        loadError = StateError.Empty.INSTANCE)
    }
}

