package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 宿舍模型
 *
 * @author zcd
 * @date 17/9/19
 */
@Data
public class UserResidence {
    private Long id;
    private Long residenceId;
    /**
     * 宿舍名称
     */
    private String residenceName;
    private String macAddress;

    private Long supplierId;
}
