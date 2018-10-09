package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.CircleProgressView;

import butterknife.BindView;

/**
 * @author zcd
 * @date 18/7/6
 */
public class BathroomBookingDialog extends Dialog {
    public CircleProgressView circleProgressView;
    TextView title;
    private Context context;

    public BathroomBookingDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_bathroom_booking);
        circleProgressView = $(R.id.circleProgressView);
        title = $(R.id.title);
    }


    @Override
    public void show() {
        super.show();
        if (circleProgressView != null)
            circleProgressView.setProgressInTime(0, 99, 15000);
    }

    public void setTitleContent(String content) {
        if (title != null) title.setText(content);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        circleProgressView.cancelAnimator();
    }

    /**
     * 设置完成
     */
    public void setFinish() {
        if (circleProgressView != null)
            circleProgressView.setFinish();
    }

    /**
     * 解除绑定
     */
    public void onDettechView() {
        if (context != null) {
            context = null;
        }
    }

    private <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }
}
