package com.xiaolian.amigo.data.network.model.notify;

import lombok.Data;

/**
 * 获取通知列表DTO
 * <p>
 * Created by zcd on 9/22/17.
 */
@Data
public class QueryNotifyListReqDTO {
    private Integer page;
    private Integer size;
}
