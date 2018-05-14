package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

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
        private String commentContent;
        private String commentAuthor;
        private Long time;
        private String image;
        private boolean publisher;

        public LostAndFoundReplyDetailWrapper(@LostAndFoundReplyDetailItemType int type,
                                              boolean publisher, String commentContent,
                                              String commentAuthor, Long time, String image) {
            this.type = type;
            this.publisher = publisher;
            this.commentContent = commentContent;
            this.commentAuthor = commentAuthor;
            this.time = time;
            this.image = image;
        }

    }
}
