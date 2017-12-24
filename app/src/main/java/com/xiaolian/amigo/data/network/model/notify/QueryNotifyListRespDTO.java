package com.xiaolian.amigo.data.network.model.notify;

import com.xiaolian.amigo.data.vo.Notify;

import java.util.List;

import lombok.Data;

/**
 * 通知列表DTO
 * <p>
 * Created by zcd on 9/22/17.
 */
@Data
public class QueryNotifyListRespDTO {
    private List<NotifyDTO> notices;
    private Integer total;
}
