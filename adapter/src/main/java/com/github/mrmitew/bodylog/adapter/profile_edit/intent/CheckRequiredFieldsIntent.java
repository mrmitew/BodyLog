package com.github.mrmitew.bodylog.adapter.profile_edit.intent;

import com.github.mrmitew.bodylog.adapter.common.model.UIIntent;


public class CheckRequiredFieldsIntent extends UIIntent {
    private Boolean mIsFilledIn;

    public CheckRequiredFieldsIntent(final Boolean requiredFieldsFilledIn) {
        mIsFilledIn = requiredFieldsFilledIn;
    }

    public Boolean getIsFilledIn() {
        return mIsFilledIn;
    }
}
