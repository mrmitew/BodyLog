package com.github.mrmitew.bodylog.adapter.profile_edit.view;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.model.ProfileEditState;

import io.reactivex.Observable;

public interface ProfileEditView extends BaseView<ProfileEditState> {
    Observable<LoadProfileIntent> getLoadProfileIntent();

    Observable<SaveProfileIntent> getSaveIntent();

    Observable<CheckRequiredFieldsIntent> getRequiredFieldsFilledInIntent();
}
