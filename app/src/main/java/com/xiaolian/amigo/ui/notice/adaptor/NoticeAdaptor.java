package com.xiaolian.amigo.ui.notice.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Notice;
import com.xiaolian.amigo.data.network.model.notify.Notify;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 通知列表Adaptor
 * <p>
 * Created by zcd on 9/22/17.
 */

public class NoticeAdaptor extends CommonAdapter<NoticeAdaptor.NoticeWapper> {

    public NoticeAdaptor(Context context, int layoutId, List<NoticeWapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, NoticeWapper noticeWapper, int position) {
        if (noticeWapper.getType() != null) {
            Notice notice = Notice.getNotice(noticeWapper.getType());
            if (notice != null) {
                holder.setImageResource(R.id.iv_image, notice.getDrawableRes());
                holder.setText(R.id.tv_type, notice.getDesc());
            }
        }
        holder.setText(R.id.tv_content, noticeWapper.getContent());
        holder.setText(R.id.tv_time, noticeWapper.getCreateTime());
    }

    @Data
    public static class NoticeWapper {
        private String content;
        private Long id;
        private Integer type;
        private String createTime;

        public NoticeWapper(Notify notify) {
            this.content = notify.getContent();
            this.id = notify.getId();
            this.type = notify.getType();
            this.createTime = notify.getCreateTime();
        }
    }
}
