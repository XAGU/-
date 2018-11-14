package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IChangeSchoolView extends IBaseView {

    void showUpdateSchool(String school);

    void showAppliedStatus();

    void showCancelDialog();

    void showCommitDialog();

    void gotoEditProfile();

    void showUpDialog();

    void hideSuccessDialog();

    void hideFailureDialgo();

    void goEditProfileActivity();

}