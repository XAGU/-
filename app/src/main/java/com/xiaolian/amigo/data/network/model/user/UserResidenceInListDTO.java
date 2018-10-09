package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 宿舍绑定
 *
 * @author zcd
 * @date 17/9/20
 */

@Data
public class UserResidenceInListDTO {

        /**
         * buildingId : 0
         * id : 0
         * isPubBath : false
         * macAddress : string
         * residenceId : 0
         * residenceName : string
         * residenceType : 0
         * supplierId : 0
         */

        private long buildingId;
        private long id;
        private boolean isPubBath;
        private String macAddress;
        private long residenceId;
        private String residenceName;
        private long residenceType;
        private long supplierId;

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPubBath() {
        return isPubBath;
    }

    public void setPubBath(boolean pubBath) {
        isPubBath = pubBath;
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

    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }

    public long getResidenceType() {
        return residenceType;
    }

    public void setResidenceType(long residenceType) {
        this.residenceType = residenceType;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
}
