package com.xiaolian.amigo.ui.more;

import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们
 * <p>
 * Created by zcd on 10/13/17.
 */

public class AboutUsActivity extends BaseToolBarActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void initInject() {
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        setMainBackground(R.color.colorBackgroundWhite);
        tv_version.setText(getString(R.string.app_name) + " " +AppUtils.getVersionName(getApplicationContext()));
    }

    @Override
    protected int setTitle() {
        return R.string.about_us;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_about_us;
    }
}
