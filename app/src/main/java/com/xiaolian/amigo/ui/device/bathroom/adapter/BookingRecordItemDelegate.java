package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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
    private Context context;

    public BookingRecordItemDelegate(Context context) {
        this.context = context;
    }

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
//        holder.setText(R.id.tv_left_top, String.valueOf(bookingRecordWrapper.getOrder().getConsume()));
        if (bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.CANCEL.getCode()
                || bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.EXPIRED.getCode()) {
            holder.getView(R.id.tv_left_bottom_first).setVisibility(View.GONE);
            holder.getView(R.id.v_divide).setVisibility(View.GONE);
            ((TextView) holder.getView(R.id.tv_left_bottom_second)).setTextColor(context.getResources().getColor(R.color.colorDark9));
            holder.setText(R.id.tv_left_bottom_second, TimeUtils.orderTimestampFormat(bookingRecordWrapper.getOrder().getCreateTime()));
        } else if (bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.BOOKING_SUCCESS.getCode()) {
            ((TextView) holder.getView(R.id.tv_left_bottom_second)).setTextColor(context.getResources().getColor(R.color.colorFullRed));
//            holder.setText(R.id.tv_left_bottom_second ,"剩余时间 08：25");
            holder.setText(R.id.tv_left_bottom_first, bookingRecordWrapper.getLeftBottomText());
            holder.getView(R.id.tv_left_bottom_first).setVisibility(View.VISIBLE);
            holder.getView(R.id.v_divide).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_left_bottom_second, TimeUtils.orderBathroomLastTime(bookingRecordWrapper.getCreateTime(),"剩余时间 "));
        } else if (bookingRecordWrapper.getOrder().getStatus() == BathroomOperationStatus.FINISHED.getCode()) {
            holder.setText(R.id.tv_left_bottom_first, bookingRecordWrapper.getLeftBottomText());
            holder.getView(R.id.tv_left_bottom_first).setVisibility(View.VISIBLE);
            holder.getView(R.id.v_divide).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.tv_left_bottom_second)).setTextColor(context.getResources().getColor(R.color.colorDark9));
            holder.setText(R.id.tv_left_bottom_second, TimeUtils.orderTimestampFormat(bookingRecordWrapper.getCreateTime()));
        }


//        holder.setText(R.id.tv_left_bottom_first, bookingRecordWrapper.getOrder().getLeftBottomText());
//        holder.setText(R.id.tv_left_bottom_second,
//                TimeUtils.noticeTimestampFormat(bookingRecordWrapper.getCreateTime()));
        holder.setText(R.id.tv_right, bookingRecordWrapper.getRightText());
        holder.setText(R.id.tv_left_top, bookingRecordWrapper.getAmount());
    }
}
