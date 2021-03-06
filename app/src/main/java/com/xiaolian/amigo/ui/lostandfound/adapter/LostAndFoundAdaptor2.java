package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * 失物招领列表adaptor2
 *
 * @author zcd
 * @date 18/5/12
 */

public class LostAndFoundAdaptor2 extends CommonAdapter<LostAndFoundAdaptor2.LostAndFoundWrapper> {
    private Context context;
    private boolean marginTop = false;

    public LostAndFoundAdaptor2(Context context, int layoutId, List<LostAndFoundWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public LostAndFoundAdaptor2(Context context, int layoutId, List<LostAndFoundWrapper> datas,
                                boolean marginTop) {
        super(context, layoutId, datas);
        this.context = context;
        this.marginTop = marginTop;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundWrapper lostAndFoundWrapper, int position) {
        holder.setText(R.id.tv_title, lostAndFoundWrapper.getTitle());
        holder.setText(R.id.tv_content, lostAndFoundWrapper.getContent());
//        holder.setText(R.id.)
        holder.setText(R.id.tv_time, TimeUtils.lostAndFoundTimestampFormat(lostAndFoundWrapper.getTime()));
        if (position == 0 && marginTop) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            marginLayoutParams.setMargins(0, ScreenUtils.dpToPxInt(this.context, 10), 0, 0);
            holder.itemView.setLayoutParams(marginLayoutParams);
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
//            marginLayoutParams.setMargins(0, ScreenUtils.dpToPxInt(this.context, 10), 0, 0);
            marginLayoutParams.setMargins(0, 0, 0, 0);
            holder.itemView.setLayoutParams(marginLayoutParams);
        }
        if (ObjectsCompat.equals(lostAndFoundWrapper.getType(), LostAndFound.LOST)) {
            // 失物
            ((TextView) holder.getView(R.id.tv_title))
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_lost, 0);
        } else if (ObjectsCompat.equals(lostAndFoundWrapper.getType(), LostAndFound.FOUND)){
            // 招领
            ((TextView) holder.getView(R.id.tv_title))
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_found, 0);
        } else {
            ((TextView) holder.getView(R.id.tv_title))
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        // 设置查看评论数
        setStat(holder.getView(R.id.tv_stat),
                lostAndFoundWrapper.getViewCount(),
                lostAndFoundWrapper.getCommentCount(),
                lostAndFoundWrapper.isCommentEnable() == null ? false : lostAndFoundWrapper.isCommentEnable());
    }

    private void setStat(TextView textView, Integer viewCount, Integer commentCount, boolean isCommentEnable) {
        if (isCommentEnable) {
            textView.setText(String.format(Locale.getDefault(),
                    "%d查看·%d回复", viewCount, commentCount));
        } else {
            textView.setText(String.format(Locale.getDefault(),
                    "%d查看", viewCount));
        }
    }

    public void replaceData(List<LostAndFoundWrapper> wapper) {
        mDatas = wapper;
        notifyDataSetChanged();
    }

    public static final class LostAndFoundWrapper {
        private com.xiaolian.amigo.data.vo.LostAndFound lostAndFound;

        public LostAndFoundWrapper(com.xiaolian.amigo.data.vo.LostAndFound lostAndFound) {
            this.lostAndFound = lostAndFound;
        }

        public com.xiaolian.amigo.data.vo.LostAndFound getLostAndFound() {
            return lostAndFound;
        }

        public Long getId() {
            return lostAndFound.getId();
        }

        public Boolean isCommentEnable() {
            return lostAndFound.getCommentEnable();
        }

        public Integer getViewCount() {
            return lostAndFound.getViewCount() == null ? 0 : lostAndFound.getViewCount();
        }

        public void addViewCount() {
            lostAndFound.setViewCount(lostAndFound.getViewCount() + 1);
        }

        public void setViewCount(int viewCount) {
            lostAndFound.setViewCount(viewCount);
        }

        public void setCommentCount(int commentCount) {
            lostAndFound.setCommentsCount(commentCount);
        }

        public int getCommentCount() {
            return lostAndFound.getCommentsCount();
        }

        public String getTitle() {
            return lostAndFound.getTitle();
        }

        public String getContent() {
            return lostAndFound.getDescription();
        }

        public Long getTime() {
            return lostAndFound.getCreateTime();
        }

        public Integer getType() {
            return lostAndFound.getType();
        }
    }
}
