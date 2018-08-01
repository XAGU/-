package com.xiaolian.amigo.data.network.model.bathroom;

public class BathRouteRespDTO {
        /**
         * buildingId : 0
         * existHistory : false
         * isPubBath : false
         * macAddress : string
         * residenceId : 0
         * residenceType : 0
         */

        private long buildingId;   // 楼栋id
        private boolean existHistory;   //  是否存有历史记录
        private boolean isPubBath;   //  是否是公共浴室
        private String macAddress;  // 设备地址
        private long residenceId;   // 建筑id
        private int residenceType;  //位置类别：1、楼栋 2、楼层 3、宿舍房间/设备位置 4、公共浴室组
        private long  supplierId ;  // 设备商id

    public boolean isPubBath() {
        return isPubBath;
    }

    public void setPubBath(boolean pubBath) {
        isPubBath = pubBath;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

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

        public int getResidenceType() {
            return residenceType;
        }

        public void setResidenceType(int residenceType) {
            this.residenceType = residenceType;
        }
    }
