package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 宿舍删除返回的默认宿舍id
 *
 * @author zcd
 * @date 17/11/2
 */
@Data
public class DeleteResidenceRespDTO {
    private Long residenceId;
    private String residenceName;
}
