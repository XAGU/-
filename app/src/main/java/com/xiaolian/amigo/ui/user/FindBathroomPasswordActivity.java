package com.xiaolian.amigo.ui.user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class FindBathroomPasswordActivity  extends UserBaseActivity implements IFindBathroomPasswordView{

    @Inject
    IFindBathroomPasswordPresenter<IFindBathroomPasswordView> presenter ;

    private android.support.v4.app.FragmentManager fragmentManager ;
    private android.support.v4.app.FragmentTransaction fragmentTransaction ;

    private FindBathroomPasswordStep1Fragment findBathroomPasswordStep1Fragment ;
    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setMainBackground(R.color.colorBackgroundGray);
        presenter.onAttach(this);
        init();
        showStep1Fragment();
    }

    /**
     *
     * @param textView
     */
    public void initMobile(TextView textView){
        if (presenter != null) textView.setText(presenter.getMobile());
    }

    /**
     * 初始化
     */
    private void init(){
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected int setTitle() {
        return R.string.bathroom_password_find;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_bathroom_find_password;
    }

    /**
     * 显示第一步fragment
     */
    private void  showStep1Fragment(){
        if (findBathroomPasswordStep1Fragment == null){
            findBathroomPasswordStep1Fragment = new FindBathroomPasswordStep1Fragment();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.find_bathroom_password_fragment ,findBathroomPasswordStep1Fragment).commit();
    }

    /**
     * 确认浴室密码
     */
    public  void confirmPassword(){
        if (presenter != null) presenter.confirmPassword();
    }
}
