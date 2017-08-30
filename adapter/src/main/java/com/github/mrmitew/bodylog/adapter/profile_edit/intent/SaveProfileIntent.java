package com.github.mrmitew.bodylog.adapter.profile_edit.intent;

import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;
import com.github.mrmitew.bodylog.domain.repository.entity.Profile;


public class SaveProfileIntent extends UIIntent {
    private Profile mProfile;

    public SaveProfileIntent(final Profile profile) {
        mProfile = profile;
    }

    public Profile getProfile() {
        return mProfile;
    }
}
