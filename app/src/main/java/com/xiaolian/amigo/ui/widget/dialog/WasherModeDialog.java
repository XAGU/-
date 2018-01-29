package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DimentionUtils;
import com.xiaolian.amigo.util.Log;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * 洗衣机模式选择dialog
 * <p>
 * Created by zcd on 18/1/15.
 */

public class WasherModeDialog extends Dialog {
    public interface OnConfirmClickListener {
        void onConfirmClick();
    }
    public interface OnBonusClickListener {
        void onBonusClick();
    }
    private OnConfirmClickListener confirmClickListener;
    private OnBonusClickListener bonusClickListener;
    private TextView tv_confirm;
    private TextView tv_mode;
    private LinearLayout ll_bonus;
    private TextView tv_bonus;
    private View v_bonus;
    private Double price;
    private DecimalFormat df = new DecimalFormat("#.##");

    public void setConfirmClickListener(OnConfirmClickListener listener) {
        this.confirmClickListener = listener;
    }

    public void setBonusClickListener(OnBonusClickListener listener) {
        this.bonusClickListener = listener;
    }

    public WasherModeDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_washer_mode);
        bindView();
    }

    public void setMode(String name, String price) {
        tv_mode.setText(String.format("%s（%s元）", name, price));
    }

    public void showBonus() {
        ll_bonus.setVisibility(View.VISIBLE);
        v_bonus.setVisibility(View.VISIBLE);
    }

    public void setBonus(String bonus) {
        showBonus();
        tv_bonus.setText(bonus);
    }

    public Double getPrice() {
        return price;
    }

    public void setSubmit(Double price) {
        this.price = price;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("支付");
        String priceText = df.format(price) + "元";
        SpannableString buttonSpan = new SpannableString(priceText);
        buttonSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(18, getContext())), 0, priceText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(buttonSpan);
        builder.append("，开始使用");
        tv_confirm.setText(builder);
    }

    public void setSubmit(String str) {
        this.price = null;
        tv_confirm.setText(str);
    }

    private void bindView() {
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(v -> {
            if (confirmClickListener != null) {
                confirmClickListener.onConfirmClick();
            }
        });
        tv_mode = findViewById(R.id.tv_mode);
        tv_bonus = findViewById(R.id.tv_bonus);
        ll_bonus = findViewById(R.id.ll_bonus);
        ll_bonus.setOnClickListener(v -> {
            if (bonusClickListener != null) {
                bonusClickListener.onBonusClick();
            }
        });
        v_bonus = findViewById(R.id.v_bonus);
    }
}
