package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户个人信息DTO
 * @author zcd
 */
@Data
public class PersonalUpdateReqDTO {

    private Integer buildingId;
    private Integer floorId;
    private String nickName;
    private String pictureUrl;
    private Integer roomId;
    private Integer schoolId;
    private Integer sex;
}
