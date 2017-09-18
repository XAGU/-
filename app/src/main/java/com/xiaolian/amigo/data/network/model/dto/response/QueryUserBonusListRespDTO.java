package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 红包列表DTO
 * @author zcd
 */
@Data
public class QueryUserBonusListRespDTO {

    private int total;
    private List<BonusesBean> bonuses;

    public static class BonusesBean {
        /**
         * amount : 0
         * createTime : 2017-09-14T07:16:52.034Z
         * deviceType : 0
         * endTime : 2017-09-14T07:16:52.035Z
         * id : 0
         * name : string
         * remarks : string
         * timeLimit : 0
         * updateTime : 2017-09-14T07:16:52.035Z
         * useStatus : 0
         * validStatus : 0
         */

        private int amount;
        private String createTime;
        private int deviceType;
        private String endTime;
        private int id;
        private String name;
        private String remarks;
        private int timeLimit;
        private String updateTime;
        private int useStatus;
        private int validStatus;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(int useStatus) {
            this.useStatus = useStatus;
        }

        public int getValidStatus() {
            return validStatus;
        }

        public void setValidStatus(int validStatus) {
            this.validStatus = validStatus;
        }
    }
}
