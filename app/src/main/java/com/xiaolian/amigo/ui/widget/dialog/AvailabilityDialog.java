package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DimentionUtils;

/**
 * 供水时段、提现时段提示
 * <p>
 * Created by zcd on 9/25/17.
 */

public class AvailabilityDialog extends Dialog {

    private TextView tv_title;
    private TextView tv_tip;
    private TextView tv_sub_tip;
    private TextView tv_cancel;
    private TextView tv_ok;
    private View v_divide;
    private Type type;
    private OnOkClickListener listener;
    private Context context;

    public AvailabilityDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_avaliability);
        v_divide = findViewById(R.id.v_divide);
        tv_title = findViewById(R.id.tv_title);
        tv_tip = findViewById(R.id.tv_tip);
        tv_sub_tip = findViewById(R.id.tv_sub_tip);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> dismiss());
        tv_ok = findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onOkClick(this);
            }
        });
    }

    public void setType(Type type) {
        switch (type) {
            case NO_DEVICE:
            case TIME_VALID:
            case BIND_DORMITORY:
            case WITHDRAW_VALID:
                tv_title.setVisibility(View.VISIBLE);
                tv_tip.setGravity(Gravity.START);
                tv_tip.setTextSize(12);
                break;
            case OPEN_LOCAION_SERVICE:
                tv_title.setVisibility(View.GONE);
                tv_tip.setGravity(Gravity.CENTER);
                tv_tip.setTextSize(17);
                break;
        }
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setTitle(String title) {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }

    public void setTip(String tip) {
        tv_tip.setText(tip);
    }

    public void setSubTipVisible(boolean visible) {
        tv_sub_tip.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSubTip(String subTip) {
        tv_sub_tip.setText(subTip);
    }

    public void setOkText(String ok) {
        tv_ok.setText(ok);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public void setCancelVisible(boolean visible) {
        tv_cancel.setVisibility(visible ? View.VISIBLE : View.GONE);
        v_divide.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public interface OnOkClickListener{
        void onOkClick(Dialog dialog);
    }

    public enum Type {
        NO_DEVICE("你的默认宿舍无热水澡服务！", "默认宿舍无设备"),
        TIME_VALID("当前时间没有热水供应", "时间段错误"),
        BIND_DORMITORY("你还没有绑定宿舍信息哦！", "绑定宿舍"),
        OPEN_LOCAION_SERVICE("", "打开位置服务"),
        WITHDRAW_VALID("当前时间无法提现", "提现时间段错误")
        ;
        private String title;
        private String desc;

        Type(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }
    }
}
