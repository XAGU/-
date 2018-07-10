package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathFloorDTO {
    private String floorName;

    private List<BathGroupDTO> groups;
}
