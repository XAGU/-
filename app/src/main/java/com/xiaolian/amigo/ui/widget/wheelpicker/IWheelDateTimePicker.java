package com.xiaolian.amigo.ui.widget.wheelpicker;

import android.widget.TextView;

import com.aigestudio.wheelpicker.widgets.WheelDayPicker;
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;

import java.util.Date;

/**
 * @author zcd
 * @date 17/9/28
 */

public interface IWheelDateTimePicker {

    void setOnDateTimeSelectedListener(WheelDateTimePicker.OnDateTimeSelectedListener listener);

    Date getCurrentDate();

    int getItemAlignYear();

    void setItemAlignYear(int align);

    int getItemAlignMonth();

    void setItemAlignMonth(int align);

    int getItemAlignDay();

    void setItemAlignDay(int align);

    int getItemAlignHour();

    void setItemAlignHour(int align);

    int getItemAlignMinute();

    void setItemAlignMinute(int align);

    WheelYearPicker getWheelYearPicker();

    WheelMonthPicker getWheelMonthPicker();

    WheelDayPicker getWheelDayPicker();

    WheelHourPicker getWheelHourPicker();

    WheelMinutePicker getWheelMinutePicker();

    TextView getTextViewYear();

    TextView getTextViewMonth();

    TextView getTextViewDay();

    TextView getTextViewHour();

    TextView getTextViewMinute();
}
