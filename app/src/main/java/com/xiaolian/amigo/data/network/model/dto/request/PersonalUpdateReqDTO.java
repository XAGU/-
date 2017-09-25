package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户个人信息DTO
 * @author zcd
 */
@Data
public class PersonalUpdateReqDTO {

    private Long residenceId;
    private String nickName;
    private String pictureUrl;
    private Long schoolId;
    private Integer sex;
}
