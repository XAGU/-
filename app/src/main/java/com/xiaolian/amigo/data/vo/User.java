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

    public User() {
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
    }
}
