package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * 供水时段、提现时段提示、另一设备登录提示
 *
 * @author zcd
 * @date 17/9/25
 */

public class AvailabilityDialog extends Dialog {

    private TextView tvTitle;
    private TextView tvTip;
    private TextView tvSubTip;
    private TextView tvCancel;
    private TextView tvOk;
    private View vDivide , vDivide2;
    private Type type;
    private OnOkClickListener listener;
    private onCancelListener cancelListener ;
    private Context context;

    public AvailabilityDialog(@NonNull Context context) {
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
        setContentView(R.layout.dialog_avaliability);
        vDivide = findViewById(R.id.v_divide);
        vDivide2 = findViewById(R.id.top_line);
        tvTitle = findViewById(R.id.tv_title);
        tvTip = findViewById(R.id.tv_tip);
        tvSubTip = findViewById(R.id.tv_sub_tip);
        tvCancel = findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> {
            dismiss();
            if (cancelListener != null) cancelListener.onCancelLick();
        });

        tvOk = findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onOkClick(this);
            }
        });
    }


    public void setCancelListener(onCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setType(Type type) {
        switch (type) {
            case NO_DEVICE:
            case TIME_VALID:
            case BIND_DORMITORY:
            case WITHDRAW_VALID:
                tvTitle.setVisibility(View.VISIBLE);
                tvTip.setGravity(Gravity.START);
                tvTip.setTextSize(12);
                break;

            case OPEN_LOCAION_SERVICE:
                tvTitle.setVisibility(View.GONE);
                tvTip.setGravity(Gravity.CENTER);
                tvTip.setTextSize(17);
                break;
            case ANOTHER_DEVICE_LOGIN:
                tvTitle.setVisibility(View.GONE);
                tvTip.setTextSize(12);
                tvTip.setGravity(Gravity.START);
                vDivide.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                vDivide2.setVisibility(View.GONE);
                break;
            default:
                tvTitle.setVisibility(View.VISIBLE);
                tvTip.setGravity(Gravity.START);
                tvTip.setTextSize(12);
                vDivide.setVisibility(View.GONE);
                tvOk.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.GONE);
                break;
        }
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    public void setTip(String tip) {
        tvTip.setVisibility(View.VISIBLE);
        tvTip.setText(tip);
    }



    public void setSubTipVisible(boolean visible) {
        tvSubTip.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSubTip(String subTip) {
        tvSubTip.setText(subTip);
    }

    public void setOkText(String ok) {
        tvOk.setText(ok);
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }

    public void setCancelVisible(boolean visible) {
        tvCancel.setVisibility(visible ? View.VISIBLE : View.GONE);
        vDivide.setVisibility(visible ? View.VISIBLE : View.GONE);
    }



    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }

    public interface onCancelListener{
        void onCancelLick();
    }

    public enum Type {
        NO_DEVICE("你的默认宿舍无热水澡服务！", "默认宿舍无设备"),
        TIME_VALID("当前时间没有热水供应", "时间段错误"),
        BIND_DORMITORY("你还没有绑定宿舍信息哦！", "绑定宿舍"),
        OPEN_LOCAION_SERVICE("", "打开位置服务"),
        WITHDRAW_VALID("当前时间无法提现","提现时间段错误"),
        ANOTHER_DEVICE_LOGIN("","你的账号在另一台设备登录，如非本人操作请及时修改密码。");

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
