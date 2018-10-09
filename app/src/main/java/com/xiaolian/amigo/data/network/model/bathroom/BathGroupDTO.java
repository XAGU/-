package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathGroupDTO {
    private String groupName;
    /**
     * 客户端显示名称  楼层+ groupName（如果只有一条则只有楼层名）
     */
    private String displayName;
    private long groupId ;
    private List<BathRoomDTO> bathRooms;
}
