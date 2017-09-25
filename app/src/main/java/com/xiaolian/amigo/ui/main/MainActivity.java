package com.xiaolian.amigo.ui.main;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.notice.NoticeActivity;
import com.xiaolian.amigo.ui.widget.dialog.NoticeAlertDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yik on 2017/9/5.
 */

public class MainActivity extends MainBaseActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    IMainPresenter<IMainView> presenter;

    @BindView(R.id.bt_switch)
    ImageView btSwitch;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_nickName)
    TextView tv_nickName;

    @BindView(R.id.tv_schoolName)
    TextView tv_schoolName;

    /**
     * 通知数量上标
     */
    @BindView(R.id.tv_notice_count)
    TextView tv_notice_count;

    /**
     * 通知
     */
    @BindView(R.id.rl_notice)
    RelativeLayout rl_notice;


    HomeFragment homeFragment;
    ProfileFragment profileFragment;

    int current = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);

        btSwitch.setBackgroundResource(R.drawable.profile);

        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fm_container, homeFragment);
        transaction.commit();
        presenter.getNoticeAmount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!presenter.isLogin()) {
            tv_nickName.setText("登录／注册");
            tv_schoolName.setText("登录以后才能使用哦");
        } else {
            tv_nickName.setText(presenter.getUserInfo().getNickName());
            tv_schoolName.setText(presenter.getUserInfo().getSchoolName());
        }
    }

    @Override
    protected void setUp() {

    }

    @OnClick(R.id.bt_switch)
    void onSwitch(View v) {
        ImageView imageView = (ImageView) v;
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (current == 0) {
            transaction.remove(homeFragment);
            if (profileFragment == null) {
                profileFragment = new ProfileFragment();
            }
            transaction.add(R.id.fm_container, profileFragment);
            transaction.commit();

            imageView.setBackgroundResource(R.drawable.home);
            current = 1;
            rl_notice.setVisibility(View.GONE);
        } else {
            transaction.remove(profileFragment);
            transaction.add(R.id.fm_container, homeFragment);
            transaction.commit();
            imageView.setBackgroundResource(R.drawable.profile);
            current = 0;
            rl_notice.setVisibility(View.VISIBLE);
        }


    }


    @OnClick({R.id.iv_avatar, R.id.ll_user_info})
    void gotoLoginView() {
        if (!presenter.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 通知
     */
    @OnClick(R.id.rl_notice)
    void gotoNoticeList() {
        NoticeAlertDialog dialog = new NoticeAlertDialog(this);
        dialog.show();
//        startActivity(new Intent(this, NoticeActivity.class));
    }

    @Override
    public void showNoticeAmount(Integer amount) {
        if (amount != null && amount != 0) {
            tv_notice_count.setVisibility(View.VISIBLE);
            tv_notice_count.setText(amount);
        } else {
            tv_notice_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void startActivity(AppCompatActivity activity, Class<?> clazz) {
        if (TextUtils.isEmpty(presenter.getToken())) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            super.startActivity(activity, clazz);
        }
    }

    public void startActivity(Class<?> clasz) {
        startActivity(this, clasz);
    }
}
