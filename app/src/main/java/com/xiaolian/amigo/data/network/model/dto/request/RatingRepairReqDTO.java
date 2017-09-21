package com.xiaolian.amigo.data.network.model.dto.request;

import java.util.List;

import lombok.Data;

/**
 * 维修评价
 * <p>
 * Created by zcd on 9/21/17.
 */
@Data
public class RatingRepairReqDTO {
    private String content;
    private Integer rating;
    private List<Integer> ratingOptions;
    private Long repairId;
}
