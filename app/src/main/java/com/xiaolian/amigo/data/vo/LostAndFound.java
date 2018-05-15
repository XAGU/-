package com.xiaolian.amigo.data.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class LostAndFound implements Serializable {

    private Long createTime;
    private String description;
    private Long id;
    private String itemName;
    private String location;
    private Long lostTime;
    private String mobile;
    private Long schoolId;
    private String schoolName;
    private String title;
    private Integer type;
    private String user;
    private Long userId;
    private List<String> images;

    private Integer commentsCount;
    private Integer reportCount;
    private Integer viewCount;

}
