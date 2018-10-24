package com.xiaolian.amigo.ui.widget.wheelpicker;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.IWheelMonthPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zcd
 * @date 18/6/6
 */
public class CustomWheelMonthPicker extends WheelPicker implements IWheelMonthPicker {
    private Integer currentYear;
    private Integer startYear;
    private Integer startMonth;
    private int mSelectedMonth;

    public CustomWheelMonthPicker(Context context) {
        this(context, null);
    }

    public CustomWheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            data.add(i);
        super.setData(data);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    private void updateMonths() {
        Calendar cal = Calendar.getInstance();
        if (ObjectsCompat.equals(cal.get(Calendar.YEAR), currentYear)) {
            if (ObjectsCompat.equals(startYear, currentYear)) {
                List<Integer> data = new ArrayList<>();
                Integer currentMonth = cal.get(Calendar.MONTH) + 1;
                for (int i = startMonth; i <= currentMonth; i++) {
                    data.add(i);
                }
                super.setData(data);
            } else {
                List<Integer> data = new ArrayList<>();
                Integer currentMonth = cal.get(Calendar.MONTH) + 1;
                for (int i = 1; i <= currentMonth; i++) {
                    data.add(i);
                }
                super.setData(data);
            }
        } else if (ObjectsCompat.equals(startYear, currentYear)) {
            List<Integer> data = new ArrayList<>();
            for (int i = startMonth; i <= 12; i++) {
                data.add(i);
            }
            super.setData(data);
        } else {
            if (getData().size() != 12) {
                List<Integer> data = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    data.add(i);
                }
                super.setData(data);
            }
        }
    }

    public void setYear(int year) {
        currentYear = year;
        updateMonths();
    }

    public void setStartYearAndMonth(Integer year, Integer month) {
        this.startYear = year;
        this.startMonth = month;
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    @Override
    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getCurrentMonth() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}
