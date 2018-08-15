package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/17
 */
public class BathPreBookingRespDTO {

        /**
         * location : string
         * maxMissAbleTimes : 0
         * missedTimes : 0
         * prepayInfo : {"balance":0,"minPrepay":0,"prepay":0}
         * reservedMinute : 0
         */

        private String location;
        private int maxMissAbleTimes;
        private int missedTimes;
        private PrepayInfo prepayInfo;
        private String reservedMinute;

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

        public int getMissedTimes() {
                return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
                this.missedTimes = missedTimes;
        }

        public PrepayInfo getPrepayInfo() {
                return prepayInfo;
        }

        public void setPrepayInfo(PrepayInfo prepayInfo) {
                this.prepayInfo = prepayInfo;
        }

        public String getReservedMinute() {
                return reservedMinute;
        }

        public void setReservedMinute(String reservedMinute) {
                this.reservedMinute = reservedMinute;
        }
}
