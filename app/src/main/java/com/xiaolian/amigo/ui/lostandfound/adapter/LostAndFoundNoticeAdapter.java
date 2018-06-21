package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundNoticeAdapter extends MultiItemTypeAdapter<LostAndFoundNoticeAdapter.NoticeWrapper> {

    public LostAndFoundNoticeAdapter(Context context, List<NoticeWrapper> datas) {
        super(context, datas);
    }

    @Data
    public static final class NoticeWrapper {
        private ItemType itemType;
        private Long id;
        private Long createTime;
        private String imageUrl;
        private String content;
        private String title;
        private String userName;

        public NoticeWrapper(ItemType itemType, String content, String userName) {
            this.itemType = itemType;
            this.content = content;
            this.userName = userName;
        }
    }
    public enum ItemType {
        REPLY(1),
        LIKE(2);
        private int code;

        ItemType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
