package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.annotation.IntDef;

import com.xiaolian.amigo.data.network.model.lostandfound.LostFoundReplyDTO;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

import static com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW;
import static com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.MAIN;

/**
 * 失物招领回复详情
 * @author zcd
 * @date 18/5/14
 */
public class LostAndFoundReplyDetailAdapter extends MultiItemTypeAdapter<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> {
    public LostAndFoundReplyDetailAdapter(Context context, List<LostAndFoundReplyDetailWrapper> datas) {
        super(context, datas);
    }

    @IntDef({MAIN, FOLLOW})
    public @interface LostAndFoundReplyDetailItemType {
        int MAIN = 1;
        int FOLLOW = 2;
    }

    @Data
    public static final class LostAndFoundReplyDetailWrapper {
        /**
         * @see LostAndFoundReplyDetailItemType
         */
        private int type;
        private String content;
        private Long authorId;
        private String author;
        private Long id;
        private Long time;
        private String image;
        private boolean owner;

        private Long replyToUserId;
        private String replyToUserNickName;

        public LostAndFoundReplyDetailWrapper(@LostAndFoundReplyDetailItemType int type,
                                              boolean owner, String commentContent,
                                              String commentAuthor, Long time, String image) {
            this.type = type;
            this.owner = owner;
            this.content = commentContent;
            this.author = commentAuthor;
            this.time = time;
            this.image = image;
        }

        public LostAndFoundReplyDetailWrapper(LostFoundReplyDTO replyDTO, boolean owner) {
            this.type = LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailItemType.FOLLOW;
            this.content = replyDTO.getContent();
            this.author = replyDTO.getUserNickname();
            this.time = replyDTO.getCreateTime();
            this.image = replyDTO.getPictureUrl();
            this.owner = owner;
            this.replyToUserId = replyDTO.getReplyToUserId();
            this.replyToUserNickName = replyDTO.getReplyToUserNickname();
            this.authorId = replyDTO.getUserId();
            this.id = replyDTO.getId();
        }
    }
}
