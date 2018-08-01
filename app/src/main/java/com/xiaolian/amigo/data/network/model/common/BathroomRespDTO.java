package com.xiaolian.amigo.data.network.model.common;

/**
 * 公共浴室获取洗澡地址列表请求参数
 */
public class BathroomRespDTO {

    /**
     * buildingType : 0
     * deviceType : 0
     * existDevice : true
     * locationType : 0
     * page : 0
     * parentId : 0
     * residenceLevel : 0
     * resultType : 0
     * schoolId : 0
     * size : 0
     */

    private int buildingType;
    private int deviceType;
    private boolean existDevice;
    private int locationType;
    private int page;
    private int parentId;
    private int residenceLevel;
    private int resultType;
    private int schoolId;
    private int size;

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isExistDevice() {
        return existDevice;
    }

    public void setExistDevice(boolean existDevice) {
        this.existDevice = existDevice;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getResidenceLevel() {
        return residenceLevel;
    }

    public void setResidenceLevel(int residenceLevel) {
        this.residenceLevel = residenceLevel;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
