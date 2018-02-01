package com.xiaolian.amigo.data.network.model.user;

import java.io.Serializable;

import lombok.Data;

/**
 * 宿舍详情
 *
 * @author zcd
 * @date 17/11/14
 */
@Data
public class UserResidenceDTO implements Serializable {
    private Long buildingId;
    private Long floorId;
    private Long residenceId;
}
