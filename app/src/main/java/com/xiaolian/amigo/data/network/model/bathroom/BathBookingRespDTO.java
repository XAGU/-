package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathBookingRespDTO {

        /**
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * expiredTime : 0
         * id : 0
         * location : string
         * status : 0
         */

        private long bathOrderId;
        private long createTime;
        private long deviceNo;
        private long expiredTime;
        private long id;
        private String location;
        private int status;
        private int type ;

        public long getBathOrderId() {
                return bathOrderId;
        }

        public void setBathOrderId(long bathOrderId) {
                this.bathOrderId = bathOrderId;
        }

        public long getCreateTime() {
                return createTime;
        }

        public void setCreateTime(long createTime) {
                this.createTime = createTime;
        }

        public long getDeviceNo() {
                return deviceNo;
        }

        public void setDeviceNo(long deviceNo) {
                this.deviceNo = deviceNo;
        }

        public long getExpiredTime() {
                return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
                this.expiredTime = expiredTime;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public int getType() {
                return type;
        }

        public void setType(int type) {
                this.type = type;
        }
}
