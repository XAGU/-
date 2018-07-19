package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/17
 */
@Data
public class ShowerRoomRouterRespDTO {
    private String macAddress;
    /**
     * 房间Id
     */
    private Long roomId;
    /**
     * 澡堂id
     */
    private Long bathId;
    /**
     * 房间类型:1-宿舍房间，2-公共浴室房间
     */
    private Integer userResidenceType;
}
