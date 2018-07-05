package com.xiaolian.amigo.ui.device.bathroom.adapter;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/7/5
 */
public class BookingRecordSummaryDelegate
        implements ItemViewDelegate<BookingRecordAdapter.BookingRecordWrapper> {
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
        holder.setText(R.id.tv_booking_time,
                "本月成功预约次数：" + bookingRecordWrapper.getBookingTime());
        holder.setText(R.id.tv_missed_booking_time,
                "提示：每月失约3次将无法预约，你已失约" +
                        bookingRecordWrapper.getMissedBookingTime() + "次");
    }
}
