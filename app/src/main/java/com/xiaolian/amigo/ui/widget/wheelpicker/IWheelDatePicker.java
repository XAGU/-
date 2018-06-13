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

public interface IWheelDatePicker {

    void setOnDateSelectedListener(WheelDatePicker.OnDateSelectedListener listener);

    Date getCurrentDate();

    int getItemAlignYear();

    void setItemAlignYear(int align);

    int getItemAlignMonth();

    void setItemAlignMonth(int align);

    WheelYearPicker getWheelYearPicker();

    CustomWheelMonthPicker getWheelMonthPicker();

    TextView getTextViewYear();

    TextView getTextViewMonth();
}
