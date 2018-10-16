package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * 通知dialog
 *
 * @author zcd
 * @date 17/9/25
 */

public class WithDrawExplainAlertDialog extends Dialog {


    private Button btOk;
    private OnOkClickListener listener;
    private TextView time , object ,explain ;


    public WithDrawExplainAlertDialog(@NonNull Context context) {
        super(context, R.style.NoticeAlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //此处可以设置dialog显示的位置
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_wihtdraw_explain);
        btOk = findViewById(R.id.bt_ok);
        btOk.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOkClick(this);
            }
            dismiss();
        });

        explain = findViewById(R.id.explain);
        object = findViewById(R.id.object);
        time = findViewById(R.id.time);

    }

    public void setTime(String timeTxt) {
        if (time != null)
        time.setText(timeTxt);
    }

    public void setObject(String objectTxt) {
        if (object != null)
            object.setText(objectTxt);
    }

    public void setExplain(String explainTxt) {
        if (explain != null)
            explain.setText(explainTxt);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }

}
