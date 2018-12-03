package com.xiaolian.amigo.data.vo;

import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;

import lombok.Data;

/**
 * @author zcd
 * @date 17/9/19
 */
@Data
public class User {
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
    private boolean hadSetBathPassword ;
    private AlipayBindBean alipayBind;
    private WeChatBindBean weChatBind;



    /***  身份验证    ***/
     // 院系
    private String  department ;

    // 专业
    private String profession ;

    // 年级
    private String grade ;

    // 班级
    private String calsses ;

    // 学号
    private String studentId ;

    // 宿舍
    private String dormitory ;


    public User() {
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

    public User(EntireUserDTO entireUserDTO) {
        this.residenceName = entireUserDTO.getResidenceName();
        this.residenceId = entireUserDTO.getResidenceId();
        this.floor = entireUserDTO.getFloor();
        this.floorId = entireUserDTO.getFloorId();
        this.id = entireUserDTO.getId();
        this.mobile = entireUserDTO.getMobile();
        this.nickName = entireUserDTO.getNickName();
        this.pictureUrl = entireUserDTO.getPictureUrl();
        this.room = entireUserDTO.getRoom();
        this.roomId = entireUserDTO.getRoomId();
        this.schoolId = entireUserDTO.getSchoolId();
        this.schoolName = entireUserDTO.getSchoolName();
        this.sex = entireUserDTO.getSex();
        this.type = entireUserDTO.getType();
        this.macAddress = entireUserDTO.getMacAddress();
        this.createTime = entireUserDTO.getCreateTime();
        this.buildingId = entireUserDTO.getBuildingId();
        this.hadSetBathPassword = entireUserDTO.isHadSetBathPassword();
        this.alipayBind = entireUserDTO.getAlipayBind();
    }

    @Data
    public static class AlipayBindBean {
        /**
         * alipayNickName : string
         * alipayUserId : 0
         * isBinding : false
         */

        private String alipayNickName;
        private Long alipayUserId;
        private boolean isBinding;

        public String getAlipayNickName() {
            return alipayNickName;
        }

        public void setAlipayNickName(String alipayNickName) {
            this.alipayNickName = alipayNickName;
        }

        public Long getAlipayUserId() {
            return alipayUserId;
        }

        public void setAlipayUserId(Long alipayUserId) {
            this.alipayUserId = alipayUserId;
        }

        public boolean isIsBinding() {
            return isBinding;
        }

        public void setIsBinding(boolean isBinding) {
            this.isBinding = isBinding;
        }
    }

    public static class WeChatBindBean {
         String nickname;
         Boolean result;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }
    }
}
