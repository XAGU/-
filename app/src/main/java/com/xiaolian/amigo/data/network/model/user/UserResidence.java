package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 宿舍模型
 * <p>
 * Created by zcd on 9/19/17.
 */
@Data
public class UserResidence {
    private Integer id;
    private Integer residenceId;
    // 宿舍名称
    private String residenceName;
}
