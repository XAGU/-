package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户个人信息DTO
 * @author zcd
 */
@Data
public class PersonalUpdateReqDTO {

    private int buildingId;
    private int floorId;
    private String nickName;
    private String pictureUrl;
    private int roomId;
    private int schoolId;
    private int sex;
}
