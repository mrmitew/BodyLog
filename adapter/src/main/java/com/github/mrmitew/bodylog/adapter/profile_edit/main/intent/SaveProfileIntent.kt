package com.github.mrmitew.bodylog.adapter.profile_edit.main.intent

import com.github.mrmitew.bodylog.adapter.common.model.UIIntent
import com.github.mrmitew.bodylog.domain.repository.entity.Profile


class SaveProfileIntent(val profile: Profile) : UIIntent()
