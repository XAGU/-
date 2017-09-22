package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.notify.Notify;

import java.util.List;

import lombok.Data;

/**
 * 通知列表DTO
 * <p>
 * Created by zcd on 9/22/17.
 */
@Data
public class QueryNotifyListRespDTO {
    private List<Notify> notices;
    private Integer total;
}
