package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;

import com.xiaolian.amigo.data.network.model.bathroom.QueryBathOrderListRespDTO;
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


    public static final class BookingRecordWrapper {

        /**
         * 是否是预约记录
         */
        private boolean record;
        private QueryBathOrderListRespDTO.OrdersBean order;


        /**
         * 预约记录内容
         */
        private String amount;   // 金额
        private String leftBottomText;  //浴室房间
        private Long createTime;  //  时间
        private String rightText;  //  状态

        /**
         * 预约信息汇总
         */
        private Integer bookingTime;   // 预约次数
        private Integer missedBookingTime;  // 失约次数

        public boolean isRecord() {
            return record;
        }

        public void setRecord(boolean record) {
            this.record = record;
        }

        public QueryBathOrderListRespDTO.OrdersBean getOrder() {
            return order;
        }

        public void setOrder(QueryBathOrderListRespDTO.OrdersBean order) {
            this.order = order;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getLeftBottomText() {
            return leftBottomText;
        }

        public void setLeftBottomText(String leftBottomText) {
            this.leftBottomText = leftBottomText;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }

        public Integer getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(Integer bookingTime) {
            this.bookingTime = bookingTime;
        }

        public Integer getMissedBookingTime() {
            return missedBookingTime;
        }

        public void setMissedBookingTime(Integer missedBookingTime) {
            this.missedBookingTime = missedBookingTime;
        }
    }
}
