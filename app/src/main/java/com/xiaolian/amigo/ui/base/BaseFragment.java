package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zcd
 * @date 17/10/26
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(setLayout(), container, false);
            initView(mRootView);
        }
        return mRootView;
    }

    @LayoutRes
    protected abstract int setLayout();

    protected abstract void initView(View view);

}
