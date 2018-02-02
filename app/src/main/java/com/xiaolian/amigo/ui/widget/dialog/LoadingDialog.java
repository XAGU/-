package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ImageView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.indicator.ProgressDrawable;

/**
 * 加载框
 *
 * @author zcd
 * @date 17/10/9
 */

public class LoadingDialog extends Dialog {
    private ImageView ivLoding;
    private ProgressDrawable mProgressDrawable;//刷新动画

    public LoadingDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent);
        initView(context);
    }

    private void initView(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loding);
//        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mProgressDrawable = new ProgressDrawable();
        ivLoding = (ImageView) findViewById(R.id.iv_loding);
        ivLoding.setImageDrawable(mProgressDrawable);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mProgressDrawable.start();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mProgressDrawable.stop();
            }
        });
    }

}
