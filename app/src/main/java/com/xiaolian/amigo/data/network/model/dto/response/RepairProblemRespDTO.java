package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.repair.RepairProblem;

import java.util.List;

import lombok.Data;

/**
 * 网络返回 - 报修问题
 * <p>
 * Created by caidong on 2017/9/20.
 */
@Data
public class RepairProblemRespDTO {


    private int total;
    private List<RepairProblem> causes;

}
