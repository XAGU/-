package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class LostAndFound {

    private String createTime;
    private String description;
    private Integer id;
    private String itemName;
    private String location;
    private String lostTime;
    private String mobile;
    private Integer schoolId;
    private String schoolName;
    private String title;
    private Integer type;
    private String user;
    private Integer userId;
    private List<String> images;

}
