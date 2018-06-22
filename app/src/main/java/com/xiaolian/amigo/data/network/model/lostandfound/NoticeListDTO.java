package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/22
 */
@Data
public class NoticeListDTO {
    private List<LostFoundNoticeDTO> list;
    private Integer size;
}
