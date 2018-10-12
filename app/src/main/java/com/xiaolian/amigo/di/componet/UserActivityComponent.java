package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.UserActivityContext;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.user.ChangeBathroomPasswordActivity;
import com.xiaolian.amigo.ui.user.CheckPasswordActivity;
import com.xiaolian.amigo.ui.user.ChooseDormitoryActivity;
import com.xiaolian.amigo.ui.user.ChooseSchoolActivity;
import com.xiaolian.amigo.ui.user.EditAvatarActivity;
import com.xiaolian.amigo.ui.user.EditDormitoryActivity;
import com.xiaolian.amigo.ui.user.EditMobileActivity;
import com.xiaolian.amigo.ui.user.EditNickNameActivity;
import com.xiaolian.amigo.ui.user.EditPasswordActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.user.EditUserInfoActivity;
import com.xiaolian.amigo.ui.user.FindBathroomPasswordActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.user.CompleteInfoActivity;
import com.xiaolian.amigo.ui.user.UserCertificationActivity;
import com.xiaolian.amigo.ui.user.UserCertificationStatusActivity;

import dagger.Component;

/**
 * User模块Component
 *
 * @author zcd
 */
@UserActivityContext
@Component(dependencies = ApplicationComponent.class, modules = UserActivityModule.class)
public interface UserActivityComponent {

    void inject(EditProfileActivity activity);

    void inject(EditNickNameActivity activity);

    void inject(EditMobileActivity activity);

    void inject(EditPasswordActivity activity);

    void inject(ListChooseActivity activity);

    void inject(EditDormitoryActivity activity);

    void inject(EditAvatarActivity activity);

    void inject(CheckPasswordActivity activity);

    void inject(ChooseDormitoryActivity activity);

    void inject(ChangeBathroomPasswordActivity activity);

    void inject(FindBathroomPasswordActivity activity);

    void inject(CompleteInfoActivity activity);

    void inject(UserCertificationActivity activity);

    void inject(UserCertificationStatusActivity activity);

    void inject(ChooseSchoolActivity activity);

    void inject(EditUserInfoActivity activity);
}
