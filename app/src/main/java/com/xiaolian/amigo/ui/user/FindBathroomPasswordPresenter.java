package com.xiaolian.amigo.ui.user;

import android.widget.Toast;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordView;

import javax.inject.Inject;

public class FindBathroomPasswordPresenter<V extends IFindBathroomPasswordView> extends BasePresenter<V>
implements IFindBathroomPasswordPresenter<V>{

    private IUserDataManager manager ;

    @Inject
    public FindBathroomPasswordPresenter(IUserDataManager manager){
        super();
        this.manager = manager ;
    }



    @Override
    public void confirmPassword() {
        if (getMvpView() != null) getMvpView().onSuccess("修改浴室密码成功");
    }

    @Override
    public String  getMobile(){
        User user = null ;
        if (this.manager != null){
            user = manager.getUser() ;
        }
        if (user != null){
            return user.getMobile();
        }else{
            return "";
        }

    }
}
