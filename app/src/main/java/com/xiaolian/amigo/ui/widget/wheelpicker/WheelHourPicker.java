package com.xiaolian.amigo.ui.widget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zcd
 * @date 17/9/28
 */

public class WheelHourPicker extends WheelPicker implements IWheelHourPicker {
    private int mSelectedHour;

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i <= 23; i++)
            data.add(i);
        super.setData(data);

        mSelectedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        setSelectedItemPosition(mSelectedHour);
    }

    @Override
    public int getSelectedHour() {
        return mSelectedHour;
    }

    @Override
    public void setSelectedHour(int hour) {
        mSelectedHour = hour;
    }

    @Override
    public int getCurrentHour() {
        return Integer.parseInt(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}
