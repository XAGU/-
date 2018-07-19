package com.xiaolian.amigo.ui.user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordView;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import lombok.Data;

public class FindBathroomPasswordActivity extends UserBaseActivity implements IFindBathroomPasswordView {

    @Inject
    IFindBathroomPasswordPresenter<IFindBathroomPasswordView> presenter;

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    private FindBathroomPasswordStep1Fragment findBathroomPasswordStep1Fragment;
    private FindBathroomPasswordStep2Fragment findBathroomPasswordStep2Fragment ;
    private boolean isHadSetBathPassword = false;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setMainBackground(R.color.colorBackgroundGray);
        presenter.onAttach(this);
        init();
        showStep1Fragment();
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isHadSetBathPassword = getIntent().getBooleanExtra(Constant.EXTRA_KEY, false);
        }
    }

    /**
     * @param textView
     */
    public void initMobile(TextView textView) {
        if (presenter != null) textView.setText(presenter.getMobile());
    }

    /**
     * 初始化
     */
    private void init() {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected int setTitle() {
        return isHadSetBathPassword ? R.string.bathroom_password_find : R.string.set_bathroom_password;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_bathroom_find_password;
    }

    /**
     * 显示第一步fragment
     */
    private void showStep1Fragment() {
        if (findBathroomPasswordStep1Fragment == null) {
            findBathroomPasswordStep1Fragment = new FindBathroomPasswordStep1Fragment();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.find_bathroom_password_fragment, findBathroomPasswordStep1Fragment).commit();
    }



    /**
     * 确认浴室密码
     */
    public void updateBathroomPassword(String password) {
        if (presenter != null) presenter.updateBathroomPassword(password);
    }

    @Override
    public void nextStep() {
        if (findBathroomPasswordStep2Fragment == null) {
            findBathroomPasswordStep2Fragment = new FindBathroomPasswordStep2Fragment();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.find_bathroom_password_fragment, findBathroomPasswordStep2Fragment).commit();
    }

    @Override
    public void startTimer() {
        if (findBathroomPasswordStep1Fragment != null && findBathroomPasswordStep1Fragment.isVisible()){
            findBathroomPasswordStep1Fragment.startTimer();
        }
    }

    @Override
    public void backToCenter() {
        this.finish();
    }


}
