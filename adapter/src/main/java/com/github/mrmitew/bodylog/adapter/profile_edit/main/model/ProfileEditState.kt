package com.github.mrmitew.bodylog.adapter.profile_edit.main.model

import com.github.mrmitew.bodylog.adapter.common.UiState
import com.github.mrmitew.bodylog.adapter.common.model.StateError
import com.github.mrmitew.bodylog.domain.repository.entity.Profile

data class ProfileEditState(
        val isInProgress: Boolean,
        val isLoadSuccessful: Boolean,
        val loadError: Throwable,
        val isSaveSuccessful: Boolean,
        val saveError: Throwable,
        val requiredFieldsFilledIn: Boolean,
        val requiredFieldsError: Throwable,
        val profile: Profile) : UiState() {

    object Factory {
        fun idle(): ProfileEditState = ProfileEditState(
                isInProgress = false,
                isLoadSuccessful = false,
                profile = Profile.Factory.EMPTY,
                loadError = StateError.Empty.INSTANCE,
                requiredFieldsFilledIn = false,
                requiredFieldsError = StateError.Empty.INSTANCE,
                isSaveSuccessful = false,
                saveError = StateError.Empty.INSTANCE)


        fun empty(): ProfileEditState = ProfileEditState(
                isInProgress = false,
                isLoadSuccessful = false,
                profile = Profile.Factory.EMPTY,
                loadError = Throwable("Empty result"),
                requiredFieldsFilledIn = false,
                requiredFieldsError = StateError.Empty.INSTANCE,
                isSaveSuccessful = false,
                saveError = StateError.Empty.INSTANCE)


        fun isInProgress(): ProfileEditState = ProfileEditState(
                isInProgress = true,
                isLoadSuccessful = false,
                profile = Profile.Factory.EMPTY,
                loadError = StateError.Empty.INSTANCE,
                requiredFieldsFilledIn = false,
                requiredFieldsError = StateError.Empty.INSTANCE,
                isSaveSuccessful = false,
                saveError = StateError.Empty.INSTANCE)


        fun successful(profile: Profile): ProfileEditState = ProfileEditState(
                isInProgress = false,
                isLoadSuccessful = true,
                profile = profile,
                loadError = StateError.Empty.INSTANCE,
                requiredFieldsFilledIn = false,
                requiredFieldsError = StateError.Empty.INSTANCE,
                isSaveSuccessful = false,
                saveError = StateError.Empty.INSTANCE)


        fun error(throwable: Throwable): ProfileEditState = ProfileEditState(
                isInProgress = false,
                isLoadSuccessful = false,
                profile = Profile.Factory.EMPTY,
                loadError = throwable,
                requiredFieldsFilledIn = false,
                requiredFieldsError = StateError.Empty.INSTANCE,
                isSaveSuccessful = false,
                saveError = StateError.Empty.INSTANCE)
    }
}