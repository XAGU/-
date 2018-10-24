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
import com.xiaolian.amigo.ui.widget.wheelpicker.WheelDatePicker;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 时间选择
 *
 * @author zcd
 * @date 178/9/21
 */

public class YearMonthPickerDialog extends Dialog {

    @BindView(R.id.wp_date)
    WheelDatePicker wpDate;

    public YearMonthPickerDialog(@NonNull Context context, Long startDate) {
//        super(context);
        this(context, R.style.ActionSheetDialogStyle, startDate);
    }

    public YearMonthPickerDialog(@NonNull Context context, @StyleRes int themeResId, Long startDate) {
        super(context, themeResId);

        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);

        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        setContentView(R.layout.dialog_year_month_picker);
        ButterKnife.bind(this);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate);
        Integer startYear = cal.get(Calendar.YEAR);
        Integer startMonth = cal.get(Calendar.MONTH) + 1;
        cal.setTime(new Date());
        Integer endYear = cal.get(Calendar.YEAR);
        wpDate.setYearStart(startYear);
        wpDate.setYearEnd(endYear);
        wpDate.getWheelMonthPicker().setStartYearAndMonth(startYear, startMonth);
        wpDate.getWheelMonthPicker().setYear(endYear);
        wpDate.setCyclic(false);
        wpDate.setCurtainColor(Color.GRAY);
        wpDate.setSelectedItemTextColor(Color.BLACK);
        wpDate.setItemTextColor(Color.GRAY);
        wpDate.setItemTextSize(ScreenUtils.dpToPxInt(context, 16));
        wpDate.setVisibleItemCount(5);
//        wpDate.getWheelYearPicker().setVisibleItemCount(endYear - startYear > 5 ? 5 : endYear - startYear + 1);
        wpDate.setCurtainColor(Color.BLACK);
//        wpDate.setOnDateSelectedListener((picker, date) -> {
//            if (listener != null) {
//                listener.onItemSelected(picker, date);
//            }
//        });
    }

    @OnClick(R.id.tv_ok)
    public void confirmSelect() {
        if (listener != null) {
            listener.onItemSelected(wpDate, wpDate.getCurrentDate());
        }
        dismiss();
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
        void onItemSelected(WheelDatePicker picker, Date date);
    }

    public enum WheelType {

        YEAR(1),
        MONTH(2);

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
