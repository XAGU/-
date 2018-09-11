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

import java.text.DecimalFormat;

/**
 * 洗衣机模式选择dialog
 *
 * @author zcd
 * @date 18/1/15
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
    private TextView tvConfirm , tvTitle;
    private TextView tvMode;
    private LinearLayout llBonus;
    private TextView tvBonus;
    private View vBonus;
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
        tvMode.setText(String.format("%s（%s元）", name, price));
    }

    public void showBonus() {
        llBonus.setVisibility(View.VISIBLE);
        vBonus.setVisibility(View.VISIBLE);
    }

    public void setTvTitle(String title){
        tvTitle.setText(title);
    }
    public void setBonus(String bonus) {
        showBonus();
        tvBonus.setText(bonus);
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
        tvConfirm.setText(builder);
    }

    public void setSubmit(String str) {
        this.price = null;
        tvConfirm.setText(str);
    }

    private void bindView() {
        tvConfirm = findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(v -> {
            if (confirmClickListener != null) {
                confirmClickListener.onConfirmClick();
            }
        });
        tvMode = findViewById(R.id.tv_mode);
        tvBonus = findViewById(R.id.tv_bonus);
        llBonus = findViewById(R.id.ll_bonus);
        llBonus.setOnClickListener(v -> {
            if (bonusClickListener != null) {
                bonusClickListener.onBonusClick();
            }
        });
        vBonus = findViewById(R.id.v_bonus);
        tvTitle = findViewById(R.id.title);
    }
}
