package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BathroomOperationStatus;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author zcd
 * @date 18/7/5
 */
public class BookingRecordItemDelegate
        implements ItemViewDelegate<BookingRecordAdapter.BookingRecordWrapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_bathroom_booking_record;
    }

    @Override
    public boolean isForViewType(BookingRecordAdapter.BookingRecordWrapper item, int position) {
        return item.isRecord();
    }

    @Override
    public void convert(ViewHolder holder, BookingRecordAdapter.BookingRecordWrapper bookingRecordWrapper, int position) {
        holder.setText(R.id.tv_left_top, String.valueOf(bookingRecordWrapper.getOrder().getConsume()));
        if (bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.CANCEL.getCode()
                || bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.BOOKING_FAIL.getCode()) {
            holder.getView(R.id.tv_left_bottom_first).setVisibility(View.GONE);
            holder.getView(R.id.v_divide).setVisibility(View.GONE);
        } else {
            holder.setText(R.id.tv_left_bottom_first, bookingRecordWrapper.getOrder().getLocation());
            holder.getView(R.id.v_divide).setVisibility(View.VISIBLE);
        }
        holder.setText(R.id.tv_left_bottom_second, TimeUtils.orderTimestampFormat(bookingRecordWrapper.getOrder().getCreateTime()));

//        holder.setText(R.id.tv_left_bottom_first, bookingRecordWrapper.getOrder().getLeftBottomText());
        holder.setText(R.id.tv_left_bottom_second,
                TimeUtils.noticeTimestampFormat(bookingRecordWrapper.getCreateTime()));
        holder.setText(R.id.tv_right, bookingRecordWrapper.getRightText());
    }
}
