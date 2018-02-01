package com.xiaolian.amigo.data.network.model.repair;

import lombok.Data;

/**
 * 维修问题
 *
 * @author caidong
 * @date 17/9/20
 */
@Data
public class RepairProblem {

    private Long id;
    private String description;

    public RepairProblem(Long id, String description) {
        this.id = id;
        this.description = description;
    }
}
