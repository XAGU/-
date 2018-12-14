package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IThirdBindPresenter;
import com.xiaolian.amigo.ui.user.intf.IThirdBindView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdBindActivity extends UserBaseActivity implements IThirdBindView {
    //0 支付宝 1微信
    private int type;
    private String nick_name;
    AvailabilityDialog availabilityDialog;
    private static final int REQUEST_CODE_PASSWORD_VERIFY_UNBIND = 0x00;

    @Inject
    IThirdBindPresenter<IThirdBindView> presenter;

    @BindView(R.id.tv_nickName)
    TextView tv_nickName;

    @BindView(R.id.iv_third)
    ImageView iv_thrid;

    @BindView(R.id.rel_unbind)
    RelativeLayout relativeLayout;

    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setUnBinder(ButterKnife.bind(this));
        if(!TextUtils.isEmpty(nick_name)){
            setNickName(nick_name);
        }
        if (type == 0){
            iv_thrid.setImageResource(R.drawable.bangdingzhi);
        }else if(type == 1){
            iv_thrid.setImageResource(R.drawable.bangdingwei);
        }
        setMainBackground(R.color.white);
    }

    @Override
    protected int setTitle() {
       if(type == EditProfileActivity.ALIPAY_BIND){
           return R.string.alipay_bind;
       }else if(type == EditProfileActivity.WECHAT_BIND){
           return R.string.wechat_bind;
       }
        return R.string.wechat_bind;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_thirdlogin;
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() !=null){
            type = getIntent().getIntExtra("third_login",-1);
            nick_name = getIntent().getStringExtra("nick_name");
        }
    }

    @OnClick(R.id.rel_unbind)
    void unbind(View v){
        showCancelDialog(type);
    }


    public void showCancelDialog(final int type) {
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            return;
        }
        availabilityDialog.setOkText(getString(R.string.confirm));
        if (type == EditProfileActivity.WECHAT_BIND) {
            availabilityDialog.setTitle("确认解除微信绑定吗?");
            availabilityDialog.setTip("解除微信绑定后可重新绑定");
        }else if (type == EditProfileActivity.ALIPAY_BIND){
            availabilityDialog.setTitle("确认解除支付宝绑定吗?");
            availabilityDialog.setTip("解除支付宝后可重新绑定");
        }
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            Intent intent = new Intent(this,PasswordVerifyActivity.class);
            intent.putExtra("type",PasswordVerifyActivity.TYPE_MONEY_RETURN);
            startActivityForResult(intent,REQUEST_CODE_PASSWORD_VERIFY_UNBIND);
        });
        availabilityDialog.show();
    }

    @Override
    public void setNickName(String nickName) {
        if(type == EditProfileActivity.ALIPAY_BIND){
            tv_nickName.setText("已绑定支付宝: " +nick_name);
        }else if(type == EditProfileActivity.WECHAT_BIND){
            tv_nickName.setText("已绑定微信号: " +nick_name);
        }
    }

    @Override
    public void gotoEditProfile(EditProfileActivity.Event.EventType type) {
        EventBus.getDefault().post(new EditProfileActivity.Event(type));
        goEditProfileActivity();
    }


    private void goEditProfileActivity(){
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            if (requestCode == REQUEST_CODE_PASSWORD_VERIFY_UNBIND){
                presenter.unbind(type);
            }
        }
    }
}
