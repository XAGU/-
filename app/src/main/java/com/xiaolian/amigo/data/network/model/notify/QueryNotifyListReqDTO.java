package com.xiaolian.amigo.data.network.model.notify;

import lombok.Data;

/**
 * 获取通知列表DTO
 *
 * @author zcd
 * @date 17/9/22
 */
@Data
public class QueryNotifyListReqDTO {
    private Integer page;
    private Integer size;
}
