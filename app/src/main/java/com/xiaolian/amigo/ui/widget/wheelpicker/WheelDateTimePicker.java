package com.xiaolian.amigo.ui.widget.wheelpicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.IWheelPicker;
import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.IWheelDayPicker;
import com.aigestudio.wheelpicker.widgets.IWheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.IWheelYearPicker;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.aigestudio.wheelpicker.widgets.WheelDayPicker;
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.xiaolian.amigo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author zcd
 * @date 17/9/28
 */

public class WheelDateTimePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener,
        IWheelPicker, IWheelDateTimePicker, IWheelYearPicker, IWheelMonthPicker,
        IWheelDayPicker, IWheelMinutePicker, IWheelHourPicker {

    private static final SimpleDateFormat SDF =
            new SimpleDateFormat("yyyy-M-d-H-m", Locale.getDefault());

    private WheelYearPicker mPickerYear;
    private WheelMonthPicker mPickerMonth;
    private WheelDayPicker mPickerDay;
    private WheelHourPicker mPickerHour;
    private WheelMinutePicker mPickerMinute;

    private OnDateTimeSelectedListener mListener;

    private TextView mTVYear, mTVMonth, mTVDay, mTVHour, mTVMinute;

    private int mYear, mMonth, mDay, mHour, mMinute;

    public WheelDateTimePicker(Context context) {
        this(context, null);
    }

    public WheelDateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_wheel_date_time_picker, this);

        mPickerYear = (WheelYearPicker) findViewById(R.id.wheel_date_picker_year);
        mPickerMonth = (WheelMonthPicker) findViewById(R.id.wheel_date_picker_month);
        mPickerDay = (WheelDayPicker) findViewById(R.id.wheel_date_picker_day);
        mPickerHour = (WheelHourPicker) findViewById(R.id.wheel_date_picker_hour);
        mPickerMinute = (WheelMinutePicker) findViewById(R.id.wheel_date_picker_minute);

        mPickerYear.setOnItemSelectedListener(this);
        mPickerMonth.setOnItemSelectedListener(this);
        mPickerDay.setOnItemSelectedListener(this);
        mPickerHour.setOnItemSelectedListener(this);
        mPickerMinute.setOnItemSelectedListener(this);

        setMaximumWidthTextYear();
        mPickerMonth.setMaximumWidthText("00");
        mPickerDay.setMaximumWidthText("00");
        mPickerHour.setMaximumWidthText("00");
        mPickerMinute.setMaximumWidthText("00");

        mTVYear = (TextView) findViewById(R.id.wheel_date_picker_year_tv);
        mTVMonth = (TextView) findViewById(R.id.wheel_date_picker_month_tv);
        mTVDay = (TextView) findViewById(R.id.wheel_date_picker_day_tv);
        mTVHour = (TextView) findViewById(R.id.wheel_date_picker_hour_tv);
        mTVMinute = (TextView) findViewById(R.id.wheel_date_picker_minute_tv);

        mYear = mPickerYear.getCurrentYear();
        mMonth = mPickerMonth.getCurrentMonth();
        mDay = mPickerDay.getCurrentDay();
        mHour = mPickerHour.getCurrentHour();
        mMinute = mPickerMinute.getCurrentMinute();
    }

    private void setMaximumWidthTextYear() {
        List years = mPickerYear.getData();
        String lastYear = String.valueOf(years.get(years.size() - 1));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lastYear.length(); i++)
            sb.append("0");
        mPickerYear.setMaximumWidthText(sb.toString());
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        if (picker.getId() == R.id.wheel_date_picker_year) {
            mYear = (int) data;
            mPickerDay.setYear(mYear);
        } else if (picker.getId() == R.id.wheel_date_picker_month) {
            mMonth = (int) data;
            mPickerDay.setMonth(mMonth);
        }
        mDay = mPickerDay.getCurrentDay();
        mHour = mPickerHour.getCurrentHour();
        mMinute = mPickerMinute.getCurrentMinute();
        String date = mYear + "-" + mMonth + "-" + mDay + "-" + mHour + "-" + mMinute;
        if (null != mListener) try {
//            mListener.onDateSelected(this, SDF.parse(date));
            mListener.onDateTimeSelected(this, SDF.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getVisibleItemCount() {
        if (mPickerYear.getVisibleItemCount() == mPickerMonth.getVisibleItemCount() &&
                mPickerMonth.getVisibleItemCount() == mPickerDay.getVisibleItemCount())
            return mPickerYear.getVisibleItemCount();
        throw new ArithmeticException("Can not get visible item count correctly from" +
                "WheelDatePicker!");
    }

    @Override
    public void setVisibleItemCount(int count) {
        mPickerYear.setVisibleItemCount(count);
        mPickerMonth.setVisibleItemCount(count);
        mPickerDay.setVisibleItemCount(count);
        mPickerHour.setVisibleItemCount(count);
        mPickerMinute.setVisibleItemCount(count);
    }

    @Override
    public boolean isCyclic() {
        return mPickerYear.isCyclic() && mPickerMonth.isCyclic() && mPickerDay.isCyclic()
                && mPickerHour.isCyclic() && mPickerMinute.isCyclic();
    }

    @Override
    public void setCyclic(boolean isCyclic) {
        mPickerYear.setCyclic(isCyclic);
        mPickerMonth.setCyclic(isCyclic);
        mPickerDay.setCyclic(isCyclic);
        mPickerHour.setCyclic(isCyclic);
        mPickerMinute.setCyclic(isCyclic);
    }

    @Deprecated
    @Override
    public void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener listener) {
        throw new UnsupportedOperationException("You can not set OnItemSelectedListener for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public int getSelectedItemPosition() {
        throw new UnsupportedOperationException("You can not get position of selected item from" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setSelectedItemPosition(int position) {
        throw new UnsupportedOperationException("You can not set position of selected item for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public int getCurrentItemPosition() {
        throw new UnsupportedOperationException("You can not get position of current item from" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public List getData() {
        throw new UnsupportedOperationException("You can not get data source from WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You don't need to set data source for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setSameWidth(boolean hasSameSize) {
        throw new UnsupportedOperationException("You don't need to set same width for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public boolean hasSameWidth() {
        throw new UnsupportedOperationException("You don't need to set same width for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener listener) {
        throw new UnsupportedOperationException("WheelDatePicker unsupport set" +
                "OnWheelChangeListener");
    }

    @Deprecated
    @Override
    public String getMaximumWidthText() {
        throw new UnsupportedOperationException("You can not get maximum width text from" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setMaximumWidthText(String text) {
        throw new UnsupportedOperationException("You don't need to set maximum width text for" +
                "WheelDatePicker");
    }

    @Deprecated
    @Override
    public int getMaximumWidthTextPosition() {
        throw new UnsupportedOperationException("You can not get maximum width text position" +
                "from WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setMaximumWidthTextPosition(int position) {
        throw new UnsupportedOperationException("You don't need to set maximum width text" +
                "position for WheelDatePicker");
    }

    @Override
    public int getSelectedItemTextColor() {
        if (mPickerYear.getSelectedItemTextColor() == mPickerMonth.getSelectedItemTextColor() &&
                mPickerMonth.getSelectedItemTextColor() == mPickerDay.getSelectedItemTextColor() &&
                mPickerDay.getSelectedItemTextColor() == mPickerHour.getSelectedItemTextColor() &&
                mPickerHour.getSelectedItemTextColor() == mPickerMinute.getSelectedItemTextColor())
            return mPickerYear.getSelectedItemTextColor();
        throw new RuntimeException("Can not get color of selected item text correctly from" +
                "WheelDatePicker!");
    }

    @Override
    public void setSelectedItemTextColor(int color) {
        mPickerYear.setSelectedItemTextColor(color);
        mPickerMonth.setSelectedItemTextColor(color);
        mPickerDay.setSelectedItemTextColor(color);
        mPickerHour.setSelectedItemTextColor(color);
        mPickerMinute.setSelectedItemTextColor(color);
    }

    @Override
    public int getItemTextColor() {
        if (mPickerYear.getItemTextColor() == mPickerMonth.getItemTextColor() &&
                mPickerMonth.getItemTextColor() == mPickerDay.getItemTextColor())
            return mPickerYear.getItemTextColor();
        throw new RuntimeException("Can not get color of item text correctly from" +
                "WheelDatePicker!");
    }

    @Override
    public void setItemTextColor(int color) {
        mPickerYear.setItemTextColor(color);
        mPickerMonth.setItemTextColor(color);
        mPickerDay.setItemTextColor(color);
        mPickerHour.setItemTextColor(color);
        mPickerMinute.setItemTextColor(color);
    }

    @Override
    public int getItemTextSize() {
        if (mPickerYear.getItemTextSize() == mPickerMonth.getItemTextSize() &&
                mPickerMonth.getItemTextSize() == mPickerDay.getItemTextSize())
            return mPickerYear.getItemTextSize();
        throw new RuntimeException("Can not get size of item text correctly from" +
                "WheelDatePicker!");
    }

    @Override
    public void setItemTextSize(int size) {
        mPickerYear.setItemTextSize(size);
        mPickerMonth.setItemTextSize(size);
        mPickerDay.setItemTextSize(size);
        mPickerHour.setItemTextSize(size);
        mPickerMinute.setItemTextSize(size);
    }

    @Override
    public int getItemSpace() {
        if (mPickerYear.getItemSpace() == mPickerMonth.getItemSpace() &&
                mPickerMonth.getItemSpace() == mPickerDay.getItemSpace())
            return mPickerYear.getItemSpace();
        throw new RuntimeException("Can not get item space correctly from WheelDatePicker!");
    }

    @Override
    public void setItemSpace(int space) {
        mPickerYear.setItemSpace(space);
        mPickerMonth.setItemSpace(space);
        mPickerDay.setItemSpace(space);
        mPickerHour.setItemSpace(space);
        mPickerMinute.setItemSpace(space);
    }

    @Override
    public void setIndicator(boolean hasIndicator) {
        mPickerYear.setIndicator(hasIndicator);
        mPickerMonth.setIndicator(hasIndicator);
        mPickerDay.setIndicator(hasIndicator);
        mPickerHour.setIndicator(hasIndicator);
        mPickerMinute.setIndicator(hasIndicator);
    }

    @Override
    public boolean hasIndicator() {
        return mPickerYear.hasIndicator() && mPickerMonth.hasIndicator() &&
                mPickerDay.hasIndicator();
    }

    @Override
    public int getIndicatorSize() {
        if (mPickerYear.getIndicatorSize() == mPickerMonth.getIndicatorSize() &&
                mPickerMonth.getIndicatorSize() == mPickerDay.getIndicatorSize())
            return mPickerYear.getIndicatorSize();
        throw new RuntimeException("Can not get indicator size correctly from WheelDatePicker!");
    }

    @Override
    public void setIndicatorSize(int size) {
        mPickerYear.setIndicatorSize(size);
        mPickerMonth.setIndicatorSize(size);
        mPickerDay.setIndicatorSize(size);
        mPickerHour.setIndicatorSize(size);
        mPickerMinute.setIndicatorSize(size);
    }

    @Override
    public int getIndicatorColor() {
        if (mPickerYear.getCurtainColor() == mPickerMonth.getCurtainColor() &&
                mPickerMonth.getCurtainColor() == mPickerDay.getCurtainColor())
            return mPickerYear.getCurtainColor();
        throw new RuntimeException("Can not get indicator color correctly from WheelDatePicker!");
    }

    @Override
    public void setIndicatorColor(int color) {
        mPickerYear.setIndicatorColor(color);
        mPickerMonth.setIndicatorColor(color);
        mPickerDay.setIndicatorColor(color);
        mPickerHour.setIndicatorColor(color);
        mPickerMinute.setIndicatorColor(color);
    }

    @Override
    public void setCurtain(boolean hasCurtain) {
        mPickerYear.setCurtain(hasCurtain);
        mPickerMonth.setCurtain(hasCurtain);
        mPickerDay.setCurtain(hasCurtain);
        mPickerHour.setCurtain(hasCurtain);
        mPickerMinute.setCurtain(hasCurtain);
    }

    @Override
    public boolean hasCurtain() {
        return mPickerYear.hasCurtain() && mPickerMonth.hasCurtain() &&
                mPickerDay.hasCurtain();
    }

    @Override
    public int getCurtainColor() {
        if (mPickerYear.getCurtainColor() == mPickerMonth.getCurtainColor() &&
                mPickerMonth.getCurtainColor() == mPickerDay.getCurtainColor())
            return mPickerYear.getCurtainColor();
        throw new RuntimeException("Can not get curtain color correctly from WheelDatePicker!");
    }

    @Override
    public void setCurtainColor(int color) {
        mPickerYear.setCurtainColor(color);
        mPickerMonth.setCurtainColor(color);
        mPickerDay.setCurtainColor(color);
        mPickerHour.setCurtainColor(color);
        mPickerMinute.setCurtainColor(color);
    }

    @Override
    public void setAtmospheric(boolean hasAtmospheric) {
        mPickerYear.setAtmospheric(hasAtmospheric);
        mPickerMonth.setAtmospheric(hasAtmospheric);
        mPickerDay.setAtmospheric(hasAtmospheric);
        mPickerHour.setAtmospheric(hasAtmospheric);
        mPickerMinute.setAtmospheric(hasAtmospheric);
    }

    @Override
    public boolean hasAtmospheric() {
        return mPickerYear.hasAtmospheric() && mPickerMonth.hasAtmospheric() &&
                mPickerDay.hasAtmospheric();
    }

    @Override
    public boolean isCurved() {
        return mPickerYear.isCurved() && mPickerMonth.isCurved() && mPickerDay.isCurved();
    }

    @Override
    public void setCurved(boolean isCurved) {
        mPickerYear.setCurved(isCurved);
        mPickerMonth.setCurved(isCurved);
        mPickerDay.setCurved(isCurved);
        mPickerHour.setCurved(isCurved);
        mPickerMinute.setCurved(isCurved);
    }

    @Deprecated
    @Override
    public int getItemAlign() {
        throw new UnsupportedOperationException("You can not get item align from WheelDatePicker");
    }

    @Deprecated
    @Override
    public void setItemAlign(int align) {
        throw new UnsupportedOperationException("You don't need to set item align for" +
                "WheelDatePicker");
    }

    @Override
    public Typeface getTypeface() {
        if (mPickerYear.getTypeface().equals(mPickerMonth.getTypeface()) &&
                mPickerMonth.getTypeface().equals(mPickerDay.getTypeface()))
            return mPickerYear.getTypeface();
        throw new RuntimeException("Can not get typeface correctly from WheelDatePicker!");
    }

    @Override
    public void setTypeface(Typeface tf) {
        mPickerYear.setTypeface(tf);
        mPickerMonth.setTypeface(tf);
        mPickerDay.setTypeface(tf);
        mPickerHour.setTypeface(tf);
        mPickerMinute.setTypeface(tf);
    }

//    @Override
//    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
//        mListener = listener;
//    }
//
//    @Override
//    public void setOnDateSelectedListener(WheelDatePicker.OnDateSelectedListener listener) {
//        mListener = listener;
//
//    }

    @Override
    public void setOnDateTimeSelectedListener(OnDateTimeSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public Date getCurrentDate() {
        String date = mYear + "-" + mMonth + "-" + mDay;
        try {
            return SDF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemAlignYear() {
        return mPickerYear.getItemAlign();
    }

    @Override
    public void setItemAlignYear(int align) {
        mPickerYear.setItemAlign(align);
    }

    @Override
    public int getItemAlignMonth() {
        return mPickerMonth.getItemAlign();
    }

    @Override
    public void setItemAlignMonth(int align) {
        mPickerMonth.setItemAlign(align);
    }

    @Override
    public int getItemAlignDay() {
        return mPickerDay.getItemAlign();
    }

    @Override
    public void setItemAlignDay(int align) {
        mPickerDay.setItemAlign(align);
    }

    @Override
    public int getItemAlignHour() {
        return mPickerHour.getItemAlign();
    }

    @Override
    public void setItemAlignHour(int align) {
        mPickerHour.setItemAlign(align);
    }

    @Override
    public int getItemAlignMinute() {
        return mPickerMinute.getItemAlign();
    }

    @Override
    public void setItemAlignMinute(int align) {
        mPickerMinute.setItemAlign(align);
    }

    @Override
    public WheelYearPicker getWheelYearPicker() {
        return mPickerYear;
    }

    @Override
    public WheelMonthPicker getWheelMonthPicker() {
        return mPickerMonth;
    }

    @Override
    public WheelDayPicker getWheelDayPicker() {
        return mPickerDay;
    }

    @Override
    public WheelHourPicker getWheelHourPicker() {
        return mPickerHour;
    }

    @Override
    public WheelMinutePicker getWheelMinutePicker() {
        return mPickerMinute;
    }

    @Override
    public TextView getTextViewYear() {
        return mTVYear;
    }

    @Override
    public TextView getTextViewMonth() {
        return mTVMonth;
    }

    @Override
    public TextView getTextViewDay() {
        return mTVDay;
    }

    @Override
    public TextView getTextViewHour() {
        return mTVHour;
    }

    @Override
    public TextView getTextViewMinute() {
        return mTVMinute;
    }

    @Override
    public void setYearFrame(int start, int end) {
        mPickerYear.setYearFrame(start, end);
    }

    @Override
    public int getYearStart() {
        return mPickerYear.getYearStart();
    }

    @Override
    public void setYearStart(int start) {
        mPickerYear.setYearStart(start);
    }

    @Override
    public int getYearEnd() {
        return mPickerYear.getYearEnd();
    }

    @Override
    public void setYearEnd(int end) {
        mPickerYear.setYearEnd(end);
    }

    @Override
    public int getSelectedYear() {
        return mPickerYear.getSelectedYear();
    }

    @Override
    public void setSelectedYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    @Override
    public int getCurrentYear() {
        return mPickerYear.getCurrentYear();
    }

    @Override
    public int getSelectedMonth() {
        return mPickerMonth.getSelectedMonth();
    }

    @Override
    public void setSelectedMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    @Override
    public int getCurrentMonth() {
        return mPickerMonth.getCurrentMonth();
    }

    @Override
    public int getSelectedDay() {
        return mPickerDay.getSelectedDay();
    }

    @Override
    public void setSelectedDay(int day) {
        mDay = day;
        mPickerDay.setSelectedDay(day);
    }

    @Override
    public int getCurrentDay() {
        return mPickerDay.getCurrentDay();
    }

    @Override
    public void setYearAndMonth(int year, int month) {
        mYear = year;
        mMonth = month;
        mPickerYear.setSelectedYear(year);
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setYearAndMonth(year, month);
    }

    @Override
    public int getYear() {
        return getSelectedYear();
    }

    @Override
    public void setYear(int year) {
        mYear = year;
        mPickerYear.setSelectedYear(year);
        mPickerDay.setYear(year);
    }

    @Override
    public int getMonth() {
        return getSelectedMonth();
    }

    @Override
    public void setMonth(int month) {
        mMonth = month;
        mPickerMonth.setSelectedMonth(month);
        mPickerDay.setMonth(month);
    }

    @Override
    public int getSelectedHour() {
        return mPickerHour.getSelectedHour();
    }

    @Override
    public void setSelectedHour(int hour) {
        mHour = hour;
        mPickerHour.setSelectedHour(hour);
    }

    @Override
    public int getCurrentHour() {
        return mPickerHour.getCurrentHour();
    }

    @Override
    public int getSelectedMinute() {
        return mPickerMinute.getSelectedMinute();
    }

    @Override
    public void setSelectedMinute(int minute) {
        mMinute = minute;
        mPickerMinute.setSelectedMinute(minute);
    }

    @Override
    public int getCurrentMinute() {
        return mPickerMinute.getCurrentMinute();
    }

    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(WheelDateTimePicker picker, Date date);
    }
}
