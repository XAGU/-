package com.xiaolian.amigo.data.network.model.repair;

import java.util.List;

import lombok.Data;

/**
 * 网络返回 - 报修问题
 *
 * @author caidong
 * @date 17/9/20
 */
@Data
public class RepairProblemRespDTO {


    private int total;
    private List<RepairProblem> causes;

}
