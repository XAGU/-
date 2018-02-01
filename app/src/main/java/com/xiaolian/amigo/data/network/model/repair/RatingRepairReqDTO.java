package com.xiaolian.amigo.data.network.model.repair;

import java.util.List;

import lombok.Data;

/**
 * 维修评价
 *
 * @author zcd
 * @date 17/9/21
 */
@Data
public class RatingRepairReqDTO {
    private String content;
    private Integer rating;
    private List<Integer> ratingOptions;
    private Long repairId;
}
