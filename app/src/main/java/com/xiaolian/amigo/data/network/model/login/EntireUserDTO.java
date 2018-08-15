package com.xiaolian.amigo.data.network.model.login;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.User;

import lombok.Data;

/**
 * UserDTO
 *
 * @author zcd
 * @date 17/9/15
 */
@Data
public class EntireUserDTO implements Mapper<User> {

    private String residenceName;
    private Long residenceId;
    private String macAddress;
    private String floor;
    private Long floorId;
    private Long id;
    private String mobile;
    private String nickName;
    private String pictureUrl;
    private String room;
    private Long roomId;
    private Long schoolId;
    private String schoolName;
    private Integer sex;
    private Integer type;
    private Long createTime;
    private Long buildingId;
    private boolean hadSetBathPassword ;   //  是否已经设置浴室密码

    @Override
    public User transform() {
        User user = new User(this);
        return user;
    }

    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }

    public Long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isHadSetBathPassword() {
        return hadSetBathPassword;
    }

    public void setHadSetBathPassword(boolean hadSetBathPassword) {
        this.hadSetBathPassword = hadSetBathPassword;
    }
}
