package com.xiaolian.amigo.data.network.model.bathroom;

/**
 * 公共浴室预约信息
 */
public class TryBookingResultRespDTO {

        /**
         * bookingInfo : {"bathOrderId":0,"createTime":0,"deviceNo":0,"expiredTime":0,"id":0,"location":"string","status":0}
         * queueInfo : {"bathBookingId":0,"id":0,"location":"string","remain":0}
         */

        private BookingInfo bookingInfo;
        private QueueInfo queueInfo;

        public BookingInfo getBookingInfo() {
            return bookingInfo;
        }

        public void setBookingInfo(BookingInfo bookingInfo) {
            this.bookingInfo = bookingInfo;
        }

        public QueueInfo getQueueInfo() {
            return queueInfo;
        }

        public void setQueueInfo(QueueInfo queueInfo) {
            this.queueInfo = queueInfo;
        }


}
