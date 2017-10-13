package com.xiaolian.amigo.ui.more;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;

/**
 * 关于我们
 * <p>
 * Created by zcd on 10/13/17.
 */

public class AboutUsActivity extends BaseToolBarActivity {
    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        setMainBackground(R.color.colorBackgroundWhite);
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
