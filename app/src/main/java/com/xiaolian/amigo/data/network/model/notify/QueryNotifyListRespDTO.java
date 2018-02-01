package com.xiaolian.amigo.data.network.model.notify;

import java.util.List;

import lombok.Data;

/**
 * 通知列表DTO
 *
 * @author zcd
 * @date 17/9/22
 */
@Data
public class QueryNotifyListRespDTO {
    private List<NotifyDTO> notices;
    private Integer total;
}
