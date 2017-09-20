package com.xiaolian.amigo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录Fragment
 * <p>
 * Created by zcd on 9/19/17.
 */

public class LoginFragment extends Fragment {

    @OnClick(R.id.tv_password_retrieval_link)
    void gotoPasswordRetrievalSetp1() {
        Intent intent = new Intent(getContext(), PasswordRetrievalStep1Activity.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
