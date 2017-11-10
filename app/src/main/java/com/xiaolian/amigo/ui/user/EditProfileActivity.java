package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import lombok.Data;

/**
 * 编辑个人信息Activity
 *
 * @author zcd
 */

public class EditProfileActivity extends UserBaseActivity implements IEditProfileView {

    private static final int REQUEST_CODE_CHECK_PASSWORD = 0x0101;
    private static final int REQUEST_CODE_EDIT_NICKNAME = 0x0102;
    private static final int REQUEST_CODE_EDIT_SCHOOL = 0x0103;
    private static final int REQUEST_CODE_EDIT_DORMITORY = 0x0104;
    private static final int REQUEST_CODE_EDIT_SEX = 0x0105;
    private static final int REQUEST_CODE_EDIT_AVATAR = 0x0106;
    @Inject
    IEditProfilePresenter<IEditProfileView> presenter;

    /**
     * 宿舍
     */
    @BindView(R.id.tv_residence)
    TextView tv_residence;

    /**
     * 学校
     */
    @BindView(R.id.tv_school)
    TextView tv_school;

    /**
     * 手机
     */
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    /**
     * 性别
     */
    @BindView(R.id.tv_sex)
    TextView tv_sex;

    /**
     * 昵称
     */
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.iv_avatar)
    CircleImageView iv_avatar;

    private String avatarUrl;

    private boolean isNeedRefresh;
    private AvailabilityDialog availabilityDialog;

    @Override
    protected void setUp() {

    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditProfileActivity.this);
        presenter.getPersonProfile();
        setMainBackground(R.color.white);
    }

    @Override
    protected int setTitle() {
        return R.string.edit_profile;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }


    public void onclick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rel_edit_avatar:
//                getImage(imageUri -> {
//                    presenter.uploadImage(imageUri);
////                    Log.d("imageUri", imageUri.getPath());
////                    iv_avatar.setImageDrawable(null);
////                    Glide.with(EditProfileActivity.this)
////                        .load(imageUri)
////                        .asBitmap().skipMemoryCache(true)
////                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_avatar);
//                });
                intent = new Intent(this, EditAvatarActivity.class);
                intent.putExtra(EditAvatarActivity.INTENT_KEY_CURRENT_AVATAR, avatarUrl);
                startActivityForResult(intent, REQUEST_CODE_EDIT_AVATAR);
                break;
            case R.id.rel_edit_nickname:
                intent = new Intent(this, EditNickNameActivity.class);
                intent.putExtra(Constant.EXTRA_KEY, new EditNickNameActivity.Model(tv_nickname.getText().toString().trim()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_NICKNAME, new Bundle());
                } else {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_NICKNAME);
                }
                break;
            case R.id.rel_edit_sex:
                intent = new Intent(this, ListChooseActivity.class);
                if (!TextUtils.isEmpty(tv_sex.getText())) {
                    intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SEX_TYPE,
                            TextUtils.equals(tv_sex.getText(), "男") ? 1 : 2);
                }
                intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                        ListChooseActivity.ACTION_LIST_SEX);

                startActivityForResult(intent, REQUEST_CODE_EDIT_SEX);
                break;
            case R.id.rel_edit_mobile:
                intent = new Intent(this, CheckPasswordActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHECK_PASSWORD);
//                ChangeMobileDialog dialog = new ChangeMobileDialog(this);
//                dialog.setOnOkClickListener((dialog1, password) -> presenter.checkPassword(password));
//                dialog.show();
                break;
            case R.id.rel_edit_password:
                intent = new Intent(this, EditPasswordActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rel_edit_school:
                presenter.checkChangeSchool();
                break;
            case R.id.rel_edit_room:
                if (TextUtils.isEmpty(tv_residence.getText())) {
//                    intent = new Intent(this, EditDormitoryActivity.class);
//                    intent.putExtra(Constant.EXTRA_KEY, false);
//                    startActivityForResult(intent, REQUEST_CODE_EDIT_DORMITORY);
                    intent = new Intent(this, ListChooseActivity.class);
                    intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                    intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                            ListChooseActivity.ACTION_LIST_BUILDING);
                    intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
                    startActivity(intent);
                } else {
                    intent = new Intent(this, EditDormitoryActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_EDIT_DORMITORY);
                }
                break;
        }
    }

    @Override
    public void showChangeSchoolDialog() {
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            return;
        }
        availabilityDialog.setOkText(getString(R.string.confirm));
        availabilityDialog.setTip(getString(R.string.change_school_tip));
        availabilityDialog.setSubTipVisible(false);
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            Intent intent = new Intent(this, ListChooseActivity.class);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_SCHOOL);
            startActivityForResult(intent, REQUEST_CODE_EDIT_SCHOOL);
        });
        availabilityDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isNeedRefresh = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedRefresh) {
            presenter.getPersonProfile();
            isNeedRefresh = false;
        }
    }

    @Override
    public void setAvatar(String pictureUrl) {
        if (!TextUtils.isEmpty(pictureUrl)) {
            avatarUrl = pictureUrl;
            Glide.with(this).load(pictureUrl)
                    .asBitmap()
                    .placeholder(R.drawable.ic_picture_error)
                    .error(R.drawable.ic_picture_error)
                    .into(iv_avatar);
        } else {
            iv_avatar.setImageResource(R.drawable.ic_picture_error);
        }
    }

    @Override
    public void setNickName(String nickName) {
        tv_nickname.setText(nickName);
    }

    @Override
    public void setSex(int sex) {
        tv_sex.setText(sex == 1 ? "男" : "女");
    }

    @Override
    public void setMobile(String mobile) {
        tv_mobile.setText(mobile);
    }

    @Override
    public void setSchoolName(String schoolName) {
        tv_school.setText(schoolName);
    }

    @Override
    public void setResidenceName(String residenceName) {
        tv_residence.setText(residenceName);
    }

    @Override
    public void gotoChangeMobile() {
        Intent intent;
        intent = new Intent(getApplicationContext(), EditMobileActivity.class);
        intent.putExtra("nickName", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(intent, REQUEST_CODE_CHECK_PASSWORD, new Bundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE_CHECK_PASSWORD);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getType()) {
            case REFRESH:
                isNeedRefresh = true;
                break;
        }
    }

    @Data
    public static class Event {
        EventType type;
        Event(EventType type) {
            this.type = type;
        }

        public enum EventType {
            REFRESH(1);
            int type;

            EventType(int type) {
                this.type = type;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
