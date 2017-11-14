package com.xiaolian.amigo.data.network.model.dto.response;

import java.io.Serializable;

import lombok.Data;

/**
 * 宿舍详情
 * <p>
 * Created by zcd on 17/11/14.
 */
@Data
public class UserResidenceDTO implements Serializable {
    private Long buildingId;
    private Long floorId;
    private Long residenceId;
}
