package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.component.ClearableEditText;
import com.xiaolian.amigo.ui.user.intf.IEditNickNamePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditNickNameView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 编辑昵称页面
 * @author zcd
 */
public class EditNickNameActivity extends UserBaseActivity implements IEditNickNameView {

    @Inject
    IEditNickNamePresenter<IEditNickNameView> mPresenter;


    @BindView(R.id.edit_nickname)
    ClearableEditText edit_nickname;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        mPresenter.onAttach(EditNickNameActivity.this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void clearEditText() {
        edit_nickname.setText("");
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                mPresenter.updateNickName(edit_nickname.getText().toString());
                break;
        }
    }
}
