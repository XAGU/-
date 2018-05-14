package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.annotation.IntDef;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

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
    public static final class LostAndFoundDetailWrapper {
        /**
         * @see LostAndFoundDetailItemType
         */
        private int type;
        /**
         * 通用
         */
        private Long time;

        /**
         * 详情内容
         */
        private List<String> image;
        private String contentTitle;
        private String content;
        private Integer watchCount;
        private Integer commentCount;

        /**
         * 评论
         */
        private String commentContent;
        private String commentAuthor;
    }
}
