package com.xiaolian.amigo.ui.user;

import android.util.Log;
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
    private static final String TAG = EditNickNameActivity.class.getSimpleName();

    @Inject
    IEditNickNamePresenter<IEditNickNameView> presenter;


    @BindView(R.id.edit_nickname)
    ClearableEditText edit_nickname;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @Override
    protected void setUp() {

    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditNickNameActivity.this);
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
        edit_nickname.setText("");
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                Log.d(TAG, edit_nickname.getText().toString());
                presenter.updateNickName(edit_nickname.getText().toString().trim());
                break;
        }
    }
}
