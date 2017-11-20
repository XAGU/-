package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.aigestudio.wheelpicker.WheelPicker;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.wheelpicker.WheelDateTimePicker;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 时间选择
 * <p>
 * Created by zcd on 9/21/17.
 */

public class DatePickerDialog extends Dialog {

    @BindView(R.id.wp_date)
    WheelDateTimePicker wp_date;

    public DatePickerDialog(@NonNull Context context) {
//        super(context);
        this(context, R.style.ActionSheetDialogStyle);
    }

    public DatePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);


        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        setContentView(R.layout.dialog_datepicker);
        ButterKnife.bind(this);

        wp_date.setCyclic(true);
        wp_date.setCurtainColor(Color.GRAY);
        wp_date.setSelectedItemTextColor(Color.BLACK);
        wp_date.setItemTextColor(Color.GRAY);
        wp_date.setItemTextSize(ScreenUtils.dpToPxInt(context, 16));
        wp_date.setVisibleItemCount(5);
        wp_date.setCurtainColor(Color.BLACK);
        wp_date.setOnDateTimeSelectedListener((picker, date) -> {
            if (listener != null) {
                listener.onItemSelected(picker, date);
            }
        });
    }

    private void setWheelPicker(Context context, WheelPicker picker, List<String> data) {
        picker.setData(data);
        picker.setCyclic(true);
        picker.setSelectedItemTextColor(Color.BLACK);
        picker.setItemTextColor(Color.GRAY);
        picker.setItemTextSize(ScreenUtils.dpToPxInt(context, 14));
        picker.setVisibleItemCount(5);
        picker.setAtmospheric(true);

    }

    private OnItemSelectedListener listener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(WheelDateTimePicker picker, Date date);
    }

    public enum WheelType {

        YEAR(1),
        MONTH(2),
        DAY(3),
        HOUR(4),
        MINUTE(5);

        private int type;

        WheelType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
