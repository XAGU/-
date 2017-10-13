package com.xiaolian.amigo.ui.notice.adaptor;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Notice;
import com.xiaolian.amigo.data.network.model.notify.Notify;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
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
        if (position == 0) {
            LinearLayout ll_item = holder.getView(R.id.ll_item_notice);
            if (!TextUtils.equals((CharSequence) ll_item.getChildAt(0).getTag(), "divider")) {
                View view = new View(ll_item.getContext());
                view.setTag("divider");
                view.setBackgroundResource(R.drawable.divider);
                ll_item.addView(view, 0);
            }
        }
        if (noticeWapper.getType() != null) {
            Notice notice = Notice.getNotice(noticeWapper.getType());
            if (notice != null) {
                holder.setImageResource(R.id.iv_image, notice.getDrawableRes());
                holder.setText(R.id.tv_type, notice.getDesc());
            }
        }
        holder.setText(R.id.tv_content, noticeWapper.getContent());
        holder.setText(R.id.tv_time, TimeUtils.convertTimestampToFormat(noticeWapper.getCreateTime())
                + "/" + TimeUtils.millis2String(noticeWapper.getCreateTime(), TimeUtils.MY_TIME_FORMAT));
    }

    @Data
    public static class NoticeWapper implements Serializable {
        private String content;
        private Long id;
        private Integer type;
        private Long createTime;
        private Integer readStatus;

        public NoticeWapper(Notify notify) {
            this.content = notify.getContent();
            this.id = notify.getId();
            this.type = notify.getType();
            this.createTime = notify.getCreateTime();
            this.readStatus = notify.getReadStatus();
        }
    }
}
