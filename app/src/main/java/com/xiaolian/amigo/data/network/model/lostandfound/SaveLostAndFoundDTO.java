package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * 保存失物招领
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class SaveLostAndFoundDTO {
    private String description;
    private Long id;
    private String itemName;
    private String location;
    private Long lostTime;
    private String mobile;
    private String title;
    private Integer type;
    private List<String> images;

}
