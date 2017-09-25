package com.xiaolian.amigo.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.CircleImageView;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;
import com.xiaolian.amigo.ui.widget.dialog.ChangeMobileDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 编辑个人信息Activity
 *
 * @author zcd
 */

public class EditProfileActivity extends UserBaseActivity implements IEditProfileView {

    private static final int REQUEST_CODE_EDIT_MOBILE = 0x0101;
    private static final int REQUEST_CODE_EDIT_NICKNAME = 0x0102;
    private static final int REQUEST_CODE_EDIT_SCHOOL = 0x0103;
    private static final int REQUEST_CODE_EDIT_DORMITORY = 0x0104;
    private static final int REQUEST_CODE_EDIT_SEX = 0x0105;
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

    @Override
    protected void setUp() {

    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditProfileActivity.this);
        presenter.getPersonProfile();
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
                getImage(imageUri -> {
                    presenter.uploadImage(imageUri);
//                    Log.d("imageUri", imageUri.getPath());
//                    iv_avatar.setImageDrawable(null);
//                    Glide.with(EditProfileActivity.this)
//                        .load(imageUri)
//                        .asBitmap().skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_avatar);
                });
                break;
            case R.id.rel_edit_nickname:
                intent = new Intent(getApplicationContext(), com.xiaolian.amigo.ui.user.EditNickNameActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_NICKNAME, new Bundle());
                } else {
                    startActivityForResult(intent, REQUEST_CODE_EDIT_NICKNAME);
                }
                break;
            case R.id.rel_edit_sex:
                intent = new Intent(getApplicationContext(), ListChooseActivity.class);
                if (!TextUtils.isEmpty(tv_sex.getText())) {
                    intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SEX_TYPE,
                            TextUtils.equals(tv_sex.getText(), "男") ? 1 : 2);
                }
                intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                        ListChooseActivity.ACTION_LIST_SEX);

                startActivityForResult(intent, REQUEST_CODE_EDIT_SEX);
                break;
            case R.id.rel_edit_mobile:
                ChangeMobileDialog dialog = new ChangeMobileDialog(this);
                dialog.setOnOkClickListener((dialog1, password) -> presenter.checkPassword(password));
                dialog.show();
                break;
            case R.id.rel_edit_password:
                intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rel_edit_school:
                intent = new Intent(this, ListChooseActivity.class);
                intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                        ListChooseActivity.ACTION_LIST_SCHOOL);
                startActivityForResult(intent, REQUEST_CODE_EDIT_SCHOOL);
                break;
            case R.id.rel_edit_room:
                intent = new Intent(this, EditDormitoryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_DORMITORY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            presenter.getPersonProfile();
        }
    }

    @Override
    public void setAvatar(String pictureUrl) {
        if (!TextUtils.isEmpty(pictureUrl)) {
            Glide.with(this).load(pictureUrl).into(iv_avatar);
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
            startActivityForResult(intent, REQUEST_CODE_EDIT_MOBILE, new Bundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE_EDIT_MOBILE);
        }
    }

}
