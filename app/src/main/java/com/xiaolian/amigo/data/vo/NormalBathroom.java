package com.xiaolian.amigo.data.vo;

/**
 * 默认的浴室地址
 */
public class NormalBathroom {

        /**
         * buildingId : 0
         * existHistory : false
         * isPubBath : false
         * macAddress : string
         * residenceId : 0
         * residenceType : 0
         */

        private long buildingId;
        private boolean existHistory;
        private boolean isPubBath;
        private String macAddress;
        private long residenceId;
        private long residenceType;
        private long  supplierId ;  // 设备商id

        public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        public boolean isExistHistory() {
            return existHistory;
        }

        public void setExistHistory(boolean existHistory) {
            this.existHistory = existHistory;
        }

        public boolean isIsPubBath() {
            return isPubBath;
        }

        public void setIsPubBath(boolean isPubBath) {
            this.isPubBath = isPubBath;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public long getResidenceId() {
            return residenceId;
        }

        public void setResidenceId(long residenceId) {
            this.residenceId = residenceId;
        }

        public long getResidenceType() {
            return residenceType;
        }

        public void setResidenceType(long residenceType) {
            this.residenceType = residenceType;
        }

}
