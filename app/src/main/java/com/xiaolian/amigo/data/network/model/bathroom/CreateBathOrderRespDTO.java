package com.xiaolian.amigo.data.network.model.bathroom;


public class CreateBathOrderRespDTO {

        /**
         * bathOrderId : 0
         * createTime : 0
         * expiredTime : 0
         * location : string
         * missedTimes : 0
         * status : 0
         * totalMissTimes : 0
         */

        private int bathOrderId;    //  预约订单Id
        private int createTime;    //创建时间
        private int expiredTime;   // 过期时间
        private String location;  // 位置
        private int missedTimes;  //失约次数， 只有预约才会返回
        private int status;   // 订单状态
        private int totalMissTimes;  // 总共失约次数   只有预约才会返回

        public int getBathOrderId() {
            return bathOrderId;
        }

        public void setBathOrderId(int bathOrderId) {
            this.bathOrderId = bathOrderId;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(int expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getMissedTimes() {
            return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
            this.missedTimes = missedTimes;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTotalMissTimes() {
            return totalMissTimes;
        }

        public void setTotalMissTimes(int totalMissTimes) {
            this.totalMissTimes = totalMissTimes;
        }

}
