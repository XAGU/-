package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IChangeSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeSchoolView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.ui.widget.dialog.BathroomBookingDialog;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import lombok.Data;


public class ChangeSchoolActivity extends UserBaseActivity implements IChangeSchoolView {

    @BindView(R.id.text_school)
    TextView mSchooll;

    @BindView(R.id.rl_school)
    RelativeLayout relativeLayout;

    @BindView(R.id.bt_commit)
    Button commit;

    @BindView(R.id.et_school)
    EditText editText;

    @BindView(R.id.iv_school)
    ImageView iv_school;

    CityBean cityBean;


    private long appliedId;
    private String appliedReason;
    private String appiedSchoolName;

    private String oldReason;

    private boolean isAppied = false;

    @Inject
    IChangeSchoolPresenter<IChangeSchoolView> presenter;
    private AvailabilityDialog availabilityDialog;
    private BathroomBookingDialog bathroomBookingDialog;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        EventBus.getDefault().register(this);

        presenter.onAttach(this);
        if (cityBean != null) {
            showUpdateSchool(cityBean.getCity());
        }

        if (!TextUtils.isEmpty(oldReason)){
            editText.setText(oldReason);
        }

        if (isAppied) {
            showAppliedStatus();
        }else {
            commit.setEnabled(false);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //判断用户输入的是否全是空格
                    String str = s.toString().replaceAll(" ","");
                    if(str.length() > 0){
                        commit.setEnabled(true);
                    }
                }
            });
        }






    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int setTitle() {
        return R.string.request_change_school;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_layout_change_school;
    }

    @Override
    protected void setUp() {
        if (getIntent() != null) {
            //申请页面数据
            cityBean = (CityBean) getIntent().getParcelableExtra("update");
            oldReason = getIntent().getStringExtra("old_reason");
            if (cityBean != null) {
                isAppied = false;
                return;
            }

            //用于计入审核页面
            appliedId = getIntent().getLongExtra("id", -1);
            appiedSchoolName = getIntent().getStringExtra("schoolName");
            appliedReason = getIntent().getStringExtra("reason");
            if (appliedId != -1) {
                isAppied = true;
            }
        }
    }


    @Override
    public void showUpdateSchool(String school) {
        String info = "更新学校至： " + school;
        mSchooll.setText(info);
    }

    @Override
    public void showAppliedStatus() {
        TextView textView = getSvMainContainer().findViewById(R.id.tv_toolbar_sub_title);
        textView.setTextColor(getResources().getColor(R.color.refresh_red));
        setToolbarSubTitle("(审核中，请稍等...)", null);
        textView.setGravity(Gravity.NO_GRAVITY);

        showUpdateSchool(appiedSchoolName);

        editText.setText(appliedReason);

        commit.setText("取消审核");

        if (isAppied) {
            iv_school.setVisibility(View.GONE);
            relativeLayout.setEnabled(false);
            editText.setEnabled(false);
        }

    }

    @Override
    public void showCancelDialog() {
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            return;
        }
        availabilityDialog.setOkText(getString(R.string.confirm));
        availabilityDialog.setTitle("确认取消审核?");
        availabilityDialog.setTip(getString(R.string.cancel_apply_school_tip));
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            presenter.cancelApplySchool(appliedId);
        });
        availabilityDialog.show();
    }

    @Override
    public void showCommitDialog() {
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            return;
        }
        availabilityDialog.setOkText(getString(R.string.confirm));
        availabilityDialog.setTitle("确认申请更换学校吗？");
        availabilityDialog.setTip(getString(R.string.commit_apply_school_tip));
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            presenter.realChangeSchool(cityBean.getId(), editText.getText().toString().trim());
            appiedSchoolName = cityBean.getCity();
            appliedReason = editText.getText().toString();
            isAppied = true;

        });
        availabilityDialog.show();

    }

    @Override
    public void gotoEditProfile() {
        EventBus.getDefault().post(new EditProfileActivity.Event(EditProfileActivity.Event.EventType.CANCELAPPLYOK));
        goEditProfileActivity();
    }

    @OnClick(R.id.bt_commit)
    public void commitApply(View v) {
        if (isAppied) {
            //取消审核
            showCancelDialog();
            return;
        }
        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
            showUpDialog();
            presenter.preChangeSchool();
        } else {
            onError("请输入更换原因");
        }
        //审核流程

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeSchoolMsg msg) {
        switch (msg.getMsgType()) {
            case 1:
                appliedId = (long) msg.getObj();
                break;
            case 2:
                onSuccess("申请更改学校审核通过");
                //只是随意添加的一个网络请求，用于重新登录
                presenter.requestForRelogin();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showUpDialog() {
        if (bathroomBookingDialog == null) {
            bathroomBookingDialog = new BathroomBookingDialog(this);
        }
        bathroomBookingDialog.circleProgressView.setFinishListener(() -> {
            bathroomBookingDialog.dismiss();

        });
        bathroomBookingDialog.setTitleContent(getString(R.string.dialog_upload_content_school));
        bathroomBookingDialog.show();
    }

    @Override
    public void hideSuccessDialog() {
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()) {
            bathroomBookingDialog.setFinish();
        }
    }

    @Override
    public void hideFailureDialgo() {
        if (bathroomBookingDialog != null && bathroomBookingDialog.isShowing()) {
            bathroomBookingDialog.dismiss();
        }
    }

    @OnClick(R.id.rl_school)
    void back(View v){
        Intent intent = new Intent(this,ChooseSchoolActivity.class);
        intent.putExtra("isReChooseSchool",true);
        intent.putExtra("old_city_bean",cityBean);

        if(!TextUtils.isEmpty(editText.getText().toString().trim())){
            intent.putExtra("old_reason",editText.getText().toString().trim());
        }
        startActivity(intent);

        finish();
    }

    @Data
    public static class ChangeSchoolMsg {
        int msgType;
        Object obj;

        public ChangeSchoolMsg(int msgtype, Object obj){
            this.msgType = msgtype;
            this.obj = obj;
        }
    }

    @OnClick({R.id.iv_back})
    @Optional
    void back() {
        goEditProfileActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goEditProfileActivity();
    }

    @Override
    public void goEditProfileActivity(){
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }


}
