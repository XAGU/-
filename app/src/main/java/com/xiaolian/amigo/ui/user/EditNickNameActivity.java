package com.xiaolian.amigo.ui.user;

import android.text.TextUtils;

import com.xiaolian.amigo.util.Log;

import android.view.View;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.ui.user.intf.IEditNickNamePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditNickNameView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * 编辑昵称页面
 *
 * @author zcd
 * @date 17/9/15
 */
public class EditNickNameActivity extends UserBaseActivity implements IEditNickNameView {
    private static final String TAG = EditNickNameActivity.class.getSimpleName();

    @Inject
    IEditNickNamePresenter<IEditNickNameView> presenter;

    @BindView(R.id.edit_nickname)
    ClearableEditText editNickname;

    @BindView(R.id.bt_submit)
    Button btSubmit;
    private Model model;

    @Override
    protected void setUp() {
        model = (Model) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setMainBackground(R.color.colorBackgroundGray);
        presenter.onAttach(EditNickNameActivity.this);
        if (model != null && !TextUtils.isEmpty(model.getNickname())) {
            editNickname.setText(model.getNickname());
            editNickname.setSelection(editNickname.getText().length());
            btSubmit.setEnabled(true);
        }
        CommonUtil.showSoftInput(this, editNickname);
    }

    @Override
    protected int setTitle() {
        return R.string.edit_nickname;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_nickname;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void clearEditText() {
        editNickname.setText("");
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                Log.d(TAG, editNickname.getText().toString());
                presenter.updateNickName(editNickname.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    @Data
    public static class Model implements Serializable {
        public Model(String nickname) {
            this.nickname = nickname;
        }

        String nickname;
    }
}
