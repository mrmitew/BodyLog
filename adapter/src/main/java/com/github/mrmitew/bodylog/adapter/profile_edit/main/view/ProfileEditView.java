package com.github.mrmitew.bodylog.adapter.profile_edit.main.view;

import com.github.mrmitew.bodylog.adapter.common.view.BaseView;
import com.github.mrmitew.bodylog.adapter.profile_common.intent.LoadProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.CheckRequiredFieldsIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.intent.SaveProfileIntent;
import com.github.mrmitew.bodylog.adapter.profile_edit.main.model.ProfileEditState;

import io.reactivex.Observable;

public interface ProfileEditView extends BaseView<ProfileEditState> {
    Observable<LoadProfileIntent> getLoadProfileIntent();

    Observable<SaveProfileIntent> getSaveIntent();

    Observable<CheckRequiredFieldsIntent> getRequiredFieldsFilledInIntent();
}
