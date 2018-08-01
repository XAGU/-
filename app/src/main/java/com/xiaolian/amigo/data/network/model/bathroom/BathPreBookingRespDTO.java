package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/17
 */
@Data
public class BathPreBookingRespDTO {
        /**
         * balance : 0
         * location : string
         * maxMissAbleTimes : 0
         * minPrepay : 0
         * missedTimes : 0
         * prepay : 0
         * reservedMinute : 0
         */

        private Double balance;   //  用户金额
        private String location;   // 位置
        private int maxMissAbleTimes;   // 最大失约次数
        private Double minPrepay;   // 最小预付金额
        private int missedTimes;   // 失约次数
        private Double prepay;   //  预付金额
        private String reservedMinute;   // 预留分钟数

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getMaxMissAbleTimes() {
            return maxMissAbleTimes;
        }

        public void setMaxMissAbleTimes(int maxMissAbleTimes) {
            this.maxMissAbleTimes = maxMissAbleTimes;
        }

        public Double getMinPrepay() {
            return minPrepay;
        }

        public void setMinPrepay(Double minPrepay) {
            this.minPrepay = minPrepay;
        }

        public int getMissedTimes() {
            return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
            this.missedTimes = missedTimes;
        }

        public Double getPrepay() {
            return prepay;
        }

        public void setPrepay(Double prepay) {
            this.prepay = prepay;
        }

        public String getReservedMinute() {
            return reservedMinute;
        }

        public void setReservedMinute(String reservedMinute) {
            this.reservedMinute = reservedMinute;
        }
}
