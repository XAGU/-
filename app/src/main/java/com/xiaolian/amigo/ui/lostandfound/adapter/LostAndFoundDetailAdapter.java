package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.annotation.IntDef;

import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundCommentDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundReplyDTO;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

import static com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailAdapter.LostAndFoundDetailItemType.COMMENT;
import static com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailAdapter.LostAndFoundDetailItemType.CONTENT;

/**
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundDetailAdapter extends MultiItemTypeAdapter<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> {


    public LostAndFoundDetailAdapter(Context context, List<LostAndFoundDetailWrapper> datas) {
        super(context, datas);
    }

    @IntDef({CONTENT, COMMENT})
    public @interface LostAndFoundDetailItemType {
        int CONTENT = 1;
        int COMMENT = 2;
    }

    @Data
    public static final class LostAndFoundDetailWrapper {
        /**
         * @see LostAndFoundDetailItemType
         */
        private int itemType = LostAndFoundDetailItemType.CONTENT;
        private int type;
        /**
         * 通用
         */
        private Long time;
        private Long id;

        /**
         * 详情内容
         */
        private List<String> image;
        private String contentTitle;
        private String content;
        private Integer viewCount;
        private Integer commentCount;

        /**
         * 评论
         */
        private String commentContent;
        private String commentAuthor;
        private Long commentAuthorId;
        private String avatar;
        private List<LostFoundReplyDTO> replies;
        private Integer replyContent;
        private boolean owner = false;
        private Long ownerId;
        private boolean commentEnable;

        private LostAndFoundDetailWrapper() {
        }

        public LostAndFoundDetailWrapper(LostAndFound lostAndFound) {
            this.commentEnable = lostAndFound.getCommentEnable();
            this.itemType = LostAndFoundDetailItemType.CONTENT;
            this.type = lostAndFound.getType();
            this.time = lostAndFound.getCreateTime();
            this.image = lostAndFound.getImages();
            this.contentTitle = lostAndFound.getTitle();
            this.content = lostAndFound.getDescription();
            this.viewCount = lostAndFound.getViewCount();
            this.commentCount = lostAndFound.getCommentsCount();
        }

        public LostAndFoundDetailWrapper(LostFoundCommentDTO comment, boolean owner, Long ownerId,
                                         int type, boolean commentEnable) {
            this.commentEnable = commentEnable;
            this.itemType = LostAndFoundDetailItemType.COMMENT;
            this.type = type;
            this.time = comment.getCreateTime();
            this.avatar = comment.getPictureUrl();
            this.replies = comment.getReplies();
            this.commentAuthor = comment.getUserNickname();
            this.commentAuthorId = comment.getUserId();
            this.commentContent = comment.getContent();
            this.replyContent = comment.getRepliesCount() - comment.getRepliesDelCount();
            this.id = comment.getId();
            this.owner = owner;
            this.ownerId = ownerId;
        }
    }
}
