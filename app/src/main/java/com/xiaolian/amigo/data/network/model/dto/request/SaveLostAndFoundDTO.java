package com.xiaolian.amigo.data.network.model.dto.request;

import java.util.List;

import lombok.Data;

/**
 * 保存失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class SaveLostAndFoundDTO {
    private String description;
    private Long id;
    private String itemName;
    private String location;
    private String lostTime;
    private String mobile;
    private String title;
    private Integer type;
    private List<String> images;

}
