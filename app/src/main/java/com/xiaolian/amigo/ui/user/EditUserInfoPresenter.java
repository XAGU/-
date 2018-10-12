package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditUserInfoPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditUserInfoView;

import javax.inject.Inject;

public class EditUserInfoPresenter<v extends IEditUserInfoView> extends BasePresenter<v>
     implements IEditUserInfoPresenter<v>{


    private IUserDataManager userDataManager ;


    @Inject
    public EditUserInfoPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }

    @Override
    public void saveDepartment(String content) {

    }

    @Override
    public void saveProfession(String content) {

    }

    @Override
    public void saveClass(String content) {

    }

    @Override
    public void saveStudentId(String content) {

    }
}
