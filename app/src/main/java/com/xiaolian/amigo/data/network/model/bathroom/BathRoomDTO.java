package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathRoomDTO {
    private Long id;
    private String deviceNo;
    private Integer status;
    private Integer xaxis;
    private Integer yaxis;
}
