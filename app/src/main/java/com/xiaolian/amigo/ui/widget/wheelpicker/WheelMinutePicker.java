package com.xiaolian.amigo.ui.widget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * Created by zcd on 9/28/17.
 */

public class WheelMinutePicker extends WheelPicker implements IWheelMinutePicker {

    private int mSelectedMinute;

    public WheelMinutePicker(Context context) {
        this(context, null);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i <= 59; i++)
            data.add(i);
        super.setData(data);

        mSelectedMinute = Calendar.getInstance().get(Calendar.MINUTE);
        setSelectedItemPosition(mSelectedMinute);
    }

    @Override
    public int getSelectedMinute() {
        return mSelectedMinute;
    }

    @Override
    public void setSelectedMinute(int minute) {
        mSelectedMinute = minute;
    }

    @Override
    public int getCurrentMinute() {
        return Integer.parseInt(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}
