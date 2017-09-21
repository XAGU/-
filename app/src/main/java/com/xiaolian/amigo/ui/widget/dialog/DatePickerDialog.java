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
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 时间选择
 * <p>
 * Created by zcd on 9/21/17.
 */

public class DatePickerDialog extends Dialog {

    @BindView(R.id.wp_year)
    WheelPicker wp_year;
    @BindView(R.id.wp_month)
    WheelPicker wp_month;
    @BindView(R.id.wp_day)
    WheelPicker wp_day;
    @BindView(R.id.wp_hour)
    WheelPicker wp_hour;
    @BindView(R.id.wp_minute)
    WheelPicker wp_minute;

    private List<String> years = new ArrayList<String>();
    private List<String> months = new ArrayList<String>();
    private List<String> days = new ArrayList<String>();
    private List<String> hours = new ArrayList<String>();
    private List<String> minutes = new ArrayList<String>();


    public DatePickerDialog(@NonNull Context context) {
//        super(context);
        this(context, R.style.ActionSheetDialogStyle);
    }

    public DatePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);


        for (int i = 1990; i <= 2100; i ++) {
            years.add(String.valueOf(i).concat("年"));
        }
        for (int i = 1; i <= 12; i ++) {
            months.add(String.valueOf(i).concat("月"));
        }
        for (int i = 1; i <= 31; i ++) {
            days.add(String.valueOf(i).concat("日"));
        }
        for (int i = 0; i <= 23; i ++) {
            hours.add(String.valueOf(i));
        }
        for (int i = 0; i <= 60; i ++) {
            minutes.add(String.valueOf(i));
        }


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
        setWheelPicker(context, wp_year, years);
        setWheelPicker(context, wp_month, months);
        setWheelPicker(context, wp_day, days);
        setWheelPicker(context, wp_hour, hours);
        setWheelPicker(context, wp_minute, minutes);
        wp_year.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                listener.onItemSelected(picker, (String) data, position, WheelType.YEAR);
            }
        });
        wp_month.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                listener.onItemSelected(picker, (String) data, position, WheelType.MONTH);
            }
        });
        wp_day.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                listener.onItemSelected(picker, (String) data, position, WheelType.DAY);
            }
        });
        wp_hour.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                listener.onItemSelected(picker, (String) data, position, WheelType.HOUR);
            }
        });
        wp_minute.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                listener.onItemSelected(picker, (String) data, position, WheelType.MINUTE);
            }
        });

    }

    private void setWheelPicker(Context context, WheelPicker picker, List<String> data) {
        picker.setData(data);
        picker.setCyclic(true);
        picker.setSameWidth(true);
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
        void onItemSelected(WheelPicker picker, String data, int position, WheelType type);
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
