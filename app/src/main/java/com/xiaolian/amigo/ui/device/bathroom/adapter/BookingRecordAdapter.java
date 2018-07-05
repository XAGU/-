package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/5
 */
public class BookingRecordAdapter extends MultiItemTypeAdapter<BookingRecordAdapter.BookingRecordWrapper> {

    public BookingRecordAdapter(Context context, List<BookingRecordWrapper> datas) {
        super(context, datas);
    }

    @Data
    public static final class BookingRecordWrapper {
        /**
         * 是否是预约记录
         */
        private boolean record;
        /**
         * 预约记录内容
         */
        private String amount;
        private String leftBottomText;
        private Long createTime;
        private String rightText;

        /**
         * 预约信息汇总
         */
        private Integer bookingTime;
        private Integer missedBookingTime;
    }
}
