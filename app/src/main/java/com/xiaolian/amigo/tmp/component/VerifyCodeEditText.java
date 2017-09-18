package com.xiaolian.amigo.tmp.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CountDownButtonHelper;

public class VerifyCodeEditText extends LinearLayout {

    CountDownButtonHelper cdb;
    ClearableEditText clearableEditText;
    Button button;
    Context context;

    public VerifyCodeEditText(Context context) {
        this(context, null);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout(context);
//        setWillNotDraw(false);
        initTimer();
    }

    private void initTimer() {
        cdb = new CountDownButtonHelper(button, "获取验证码", 60, 1);
        cdb.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

            @Override
            public void finish() {
                button.setEnabled(true);
                button.setText("获取验证码");
            }
        });
    }

    private void initLayout(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_verifycode, this);
        Button button = (Button) findViewById(R.id.bt_verify_code);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cdb.start();
            }
        });
        ClearableEditText editText = (ClearableEditText) findViewById(R.id.et_mobile);
    }

    @Override
    protected void onDetachedFromWindow() {
        cdb.cancel();
        super.onDetachedFromWindow();
    }

}
