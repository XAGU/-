package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 编辑个人信息Activity
 * @author zcd
 */

public class EditProfileActivity extends UserBaseActivity implements IEditProfileView {

    @Inject
    IEditProfilePresenter<IEditProfileView> presenter;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(EditProfileActivity.this);
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
                showMessage("修改头像");
                break;
            case R.id.rel_edit_nickname:
                intent = new Intent(getApplicationContext(), com.xiaolian.amigo.ui.user.EditNickNameActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rel_edit_sex:
                Toast.makeText(this.getApplicationContext(), "修改性别", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_edit_mobile:
                intent = new Intent(getApplicationContext(), EditMobileActivity.class);
                intent.putExtra("nickName", "");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
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
                intent = new Intent(getApplicationContext(), ListChooseActivity.class);
                intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE, "jfkdjf");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(intent, 1, new Bundle());
                } else {
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.rel_edit_room:
                Toast.makeText(this.getApplicationContext(), "修改宿舍", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void setAvatar(String pictureUrl) {

    }

    @Override
    public void setNickName(String nickName) {

    }

    @Override
    public void setSex(int sex) {

    }

    @Override
    public void setMobile(int mobile) {

    }

    @Override
    public void setSchoolName(String schoolName) {

    }

    @Override
    public void setResidenceName(String residenceName) {

    }
}
