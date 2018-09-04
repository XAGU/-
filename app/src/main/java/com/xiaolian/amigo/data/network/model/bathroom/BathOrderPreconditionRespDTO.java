package com.xiaolian.amigo.data.network.model.bathroom;


public class BathOrderPreconditionRespDTO {
        /**
         * bathBookingId : 0
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * existUsingOrder : false
         * expiredTime : 0
         * location : string
         * maxMissAbleTimes : 0
         * missedTimes : 0
         * prepayAmount : 0
         * prepayInfo : {"balance":0,"minPrepay":0,"prepay":0}
         * status : 0
         * type : 0
         */

        private long bathBookingId;
        private long bathOrderId;
        private long bathQueueId;  //  排队id
        private int maxMissAbleTimes ;
        private int missedTimes ;
        private PrepayInfoBean prepayInfo;
        private int status;

    public long getBathBookingId() {
        return bathBookingId;
    }

    public void setBathBookingId(long bathBookingId) {
        this.bathBookingId = bathBookingId;
    }

    public long getBathOrderId() {
        return bathOrderId;
    }

    public void setBathOrderId(long bathOrderId) {
        this.bathOrderId = bathOrderId;
    }

    public long getBathQueueId() {
        return bathQueueId;
    }

    public void setBathQueueId(long bathQueueId) {
        this.bathQueueId = bathQueueId;
    }

    public int getMaxMissAbleTimes() {
        return maxMissAbleTimes;
    }

    public void setMaxMissAbleTimes(int maxMissAbleTimes) {
        this.maxMissAbleTimes = maxMissAbleTimes;
    }

    public int getMissedTimes() {
        return missedTimes;
    }

    public void setMissedTimes(int missedTimes) {
        this.missedTimes = missedTimes;
    }

    public PrepayInfoBean getPrepayInfo() {
        return prepayInfo;
    }

    public void setPrepayInfo(PrepayInfoBean prepayInfo) {
        this.prepayInfo = prepayInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public static class PrepayInfoBean {
            /**
             * balance : 0
             * minPrepay : 0
             * prepay : 0
             */

            private double balance;
            private double minPrepay;
            private double prepay;


        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getMinPrepay() {
            return minPrepay;
        }

        public void setMinPrepay(double minPrepay) {
            this.minPrepay = minPrepay;
        }

        public double getPrepay() {
            return prepay;
        }

        public void setPrepay(double prepay) {
            this.prepay = prepay;
        }
    }
    }

