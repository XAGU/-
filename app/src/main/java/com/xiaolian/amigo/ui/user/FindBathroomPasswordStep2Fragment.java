package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.user.adaptor.TipAdapter;
import com.xiaolian.amigo.ui.widget.PasswordEditText;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FindBathroomPasswordStep2Fragment extends BaseFragment {
    private static final int BATHROOM_PASSWORD_NUMBER = 6; //  浴室密码固定为6位
    @BindView(R.id.et_userpwd)
    PasswordEditText etUserpwd;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.rv_tip)
    RecyclerView rvTip ;
    Unbinder unbinder;

    private List<TipAdapter.Tip> tipList = new ArrayList<>();
    private TipAdapter tipAdapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_bathroom_password_step2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        etUserpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        toggleButton();
        CommonUtil.showSoftInput(getContext(), etUserpwd);
    }


    private void init() {
        ViewUtil.setEditHintAndSize(getString(R.string.please_enter_new_six_pure_number_password), 14, etUserpwd);
        if (etUserpwd != null) etUserpwd.setOpen(true);
        initRecyclerView();
    }

    private void initRecyclerView(){
        if (mActivity instanceof  FindBathroomPasswordActivity){
            List<String> bathroomPasswordDesc = ((FindBathroomPasswordActivity) mActivity).presenter.getBathroomPasswordDesc();
            if (bathroomPasswordDesc != null) {
                for (String desc : bathroomPasswordDesc) {
                    tipList.add(new TipAdapter.Tip(desc));
                }
            }
        }
        tipAdapter = new TipAdapter(mActivity ,R.layout.item_tip ,tipList);
        rvTip.setLayoutManager(new LinearLayoutManager(mActivity));
        rvTip.setAdapter(tipAdapter);
    }

    private void toggleButton() {
        boolean valid = !TextUtils.isEmpty(etUserpwd.getText())
                && etUserpwd.getText().length() == BATHROOM_PASSWORD_NUMBER;
        btSubmit.setEnabled(valid);
    }

    @OnClick(R.id.bt_submit)
    public void click(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                confirmPassword();
                break;
        }
    }

    /**
     * 确认浴室密码
     */
    private void confirmPassword() {
        if (getContext() instanceof FindBathroomPasswordActivity) {
            ((FindBathroomPasswordActivity) getContext()).updateBathroomPassword(etUserpwd.getText().toString().trim());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }
}
