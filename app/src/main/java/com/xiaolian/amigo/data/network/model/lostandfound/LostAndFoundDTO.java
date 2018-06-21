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
    /**
     * 是否开启评论
     */
    private Boolean commentEnable;
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

    /**
     * 点赞数量
     */
    private Integer likeCount;
    /**
     * 本人是否点赞 1 本人已点赞 2 本人未点赞
     */
    private Integer liked;
    /**
     * 头像
     */
    private String pictureUrl;

    @Override
    public LostAndFound transform() {
        LostAndFound lostAndFound = new LostAndFound();
        lostAndFound.setCommentEnable(commentEnable);
        lostAndFound.setCreateTime(createTime);
        lostAndFound.setDescription(description.replaceAll("\n+", "\n"));
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
        lostAndFound.setLikeCount(likeCount);
        lostAndFound.setLiked(liked);
        lostAndFound.setPictureUrl(pictureUrl);
        return lostAndFound;
    }
}
