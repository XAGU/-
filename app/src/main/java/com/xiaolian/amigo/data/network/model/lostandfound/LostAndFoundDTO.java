package com.xiaolian.amigo.data.network.model.lostandfound;

import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.data.vo.Mapper;

import java.util.List;

import lombok.Data;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class LostAndFoundDTO implements Mapper<LostAndFound> {
    private String itemName;

    private Integer commentsCount;
    private Integer reportCount;
    private Integer viewCount;

    private Long createTime;
    private String description;
    private List<String> images;
    private Long id;
    private String location;
    private Long lostTime;
    private String mobile;
    private Long schoolId;
    private String schoolName;
    private String title;
    private Integer type;
    private String user;
    private Long userId;

    @Override
    public LostAndFound transform() {
        LostAndFound lostAndFound = new LostAndFound();
        lostAndFound.setCreateTime(createTime);
        lostAndFound.setDescription(description);
        lostAndFound.setId(id);
        lostAndFound.setItemName(itemName);
        lostAndFound.setLocation(location);
        lostAndFound.setLostTime(lostTime);
        lostAndFound.setMobile(mobile);
        lostAndFound.setSchoolId(schoolId);
        lostAndFound.setSchoolName(schoolName);
        lostAndFound.setTitle(title);
        lostAndFound.setType(type);
        lostAndFound.setUser(user);
        lostAndFound.setUserId(userId);
        lostAndFound.setImages(images);
        lostAndFound.setViewCount(viewCount);
        lostAndFound.setCommentsCount(commentsCount);
        lostAndFound.setReportCount(reportCount);
        return lostAndFound;
    }
}
