package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 宿舍绑定
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class UserResidenceInListDTO {
    private Long id;
    private Long residenceId;
    private String residenceName;
}
