package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户个人信息DTO
 * @author zcd
 */
@Data
public class PersonalUpdateReqDTO {

    private Integer residneceId;
    private String nickName;
    private String pictureUrl;
    private Integer schoolId;
    private Integer sex;
}
