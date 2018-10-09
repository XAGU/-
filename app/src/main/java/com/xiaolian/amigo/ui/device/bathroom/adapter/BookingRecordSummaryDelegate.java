package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DimentionUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/7/5
 */
public class BookingRecordSummaryDelegate
        implements ItemViewDelegate<BookingRecordAdapter.BookingRecordWrapper> {
    private Context context;

    public BookingRecordSummaryDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_bathroom_booking_record_summary;
    }

    @Override
    public boolean isForViewType(BookingRecordAdapter.BookingRecordWrapper item, int position) {
        return !item.isRecord();
    }

    @Override
    public void convert(ViewHolder holder, BookingRecordAdapter.BookingRecordWrapper bookingRecordWrapper, int position) {
        setBookingTime(holder.getView(R.id.tv_booking_time), bookingRecordWrapper.getBookingTime());
        setMissedBookingTime(holder.getView(R.id.tv_missed_booking_time), 3, bookingRecordWrapper.getMissedBookingTime());
    }

    private void setMissedBookingTime(TextView textView, Integer availableMissedBookingTime,
                                      Integer missedBookingTime) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString tipSpan1 = new SpannableString("提示：每月失约");
        tipSpan1.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, tipSpan1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, tipSpan1.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan1);

        SpannableString availableMissedBookingTimeSpan = new SpannableString(String.valueOf(availableMissedBookingTime));
        availableMissedBookingTimeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, availableMissedBookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        availableMissedBookingTimeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff5555")), 0, availableMissedBookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(availableMissedBookingTimeSpan);

        SpannableString tipSpan2 = new SpannableString("次将无法预约，你已失约");
        tipSpan2.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, tipSpan2.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan2.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, tipSpan2.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan2);

        SpannableString missedBookingTimeSpan = new SpannableString(String.valueOf(missedBookingTime));
        missedBookingTimeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, missedBookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        missedBookingTimeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff5555")), 0, missedBookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(missedBookingTimeSpan);

        SpannableString tipSpan3 = new SpannableString("次");
        tipSpan3.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, tipSpan3.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan3.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, tipSpan3.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan3);

        textView.setText(builder);
    }

    private void setBookingTime(TextView textView, Integer bookingTime) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString tipSpan = new SpannableString("本月成功预约次数：");
        tipSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(14, context)), 0, tipSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#222222")), 0, tipSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(tipSpan);
        SpannableString bookingTimeSpan = new SpannableString(String.valueOf(bookingTime));
        bookingTimeSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(20, context)), 0, bookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bookingTimeSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff5555")), 0, bookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bookingTimeSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, bookingTimeSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(bookingTimeSpan);
        textView.setText(builder);
    }
}
