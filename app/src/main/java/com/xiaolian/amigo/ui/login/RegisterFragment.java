package com.xiaolian.amigo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 账户注册第二步
 * <p>
 * Created by zcd on 9/19/17.
 */

public class RegisterFragment extends Fragment {

    private static final int REQUEST_CODE_CHOOSE_SCHOOL = 0x1101;

    @BindView(R.id.et_userpwd)
    EditText et_userpwd;

    @BindView(R.id.tv_school)
    TextView tv_school;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    @OnClick(R.id.bt_submit)
    void register() {
        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).register(et_userpwd.getText().toString(),
                    schoolId);
        }
    }

    private Long schoolId;

    @OnClick(R.id.tv_school)
    void chooseSchool() {
        Intent intent = new Intent(getActivity(), ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_SCHOOL_RESULT);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_SCHOOL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_registry, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtil.setEditHintAndSize(getString(R.string.password_hint), 14, et_userpwd);
        ViewUtil.setEditHintAndSize(getString(R.string.school_hint), 14, tv_school);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_SCHOOL) {
                if (data.getExtras() != null) {
                    ListChooseAdaptor.Item item = data.getExtras().getParcelable(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ITEM_RESULT);
                    if (item != null) {
                        tv_school.setText(item.getContent());
                        schoolId = item.getId();
                    }
                }
            }
        }
    }
}
