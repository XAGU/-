package com.xiaolian.amigo.data.network.model.device;

import lombok.Data;

/**
 * 设备详情
 */
public class BriefDeviceDTO {

    /**
     * deviceType : 0
     * id : 0
     * location : string
     * schoolId : 0
     * schoolName : string
     * supplierId : 0
     */

    private int deviceType;
    private long id;
    private String location;
    private long schoolId;
    private String schoolName;
    private long supplierId; // 设备供应商
    private long residenceId ;
    private boolean favor ;

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
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

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(long residenceId) {
        this.residenceId = residenceId;
    }

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }
}
