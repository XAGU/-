package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
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

import lombok.Data;

/**
 * 失物招领列表adaptor2
 *
 * @author zcd
 * @date 18/5/12
 */

public class LostAndFoundAdaptor2 extends CommonAdapter<LostAndFoundAdaptor2.LostAndFoundWrapper> {
    private Context context;

    public LostAndFoundAdaptor2(Context context, int layoutId, List<LostAndFoundWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public LostAndFoundAdaptor2(Context context, int layoutId, List<LostAndFoundWrapper> datas,
                                boolean isShowIcon) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundWrapper lostAndFoundWrapper, int position) {
        holder.setText(R.id.tv_title, lostAndFoundWrapper.getTitle());
        holder.setText(R.id.tv_content, lostAndFoundWrapper.getContent());
//        holder.setText(R.id.)
        holder.setText(R.id.tv_time, TimeUtils.convertTimestampToFormat(lostAndFoundWrapper.getTime())
                + "/" + TimeUtils.millis2String(lostAndFoundWrapper.getTime(), TimeUtils.MY_TIME_FORMAT));
//        if (position == 0) {
//            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
//            marginLayoutParams.setMargins(0, ScreenUtils.dpToPxInt(this.context, 10), 0, 0);
//            holder.itemView.setLayoutParams(marginLayoutParams);
//        } else {
//            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
//            marginLayoutParams.setMargins(0, 0, 0, 0);
//            holder.itemView.setLayoutParams(marginLayoutParams);
//        }
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
    }

    public void replaceData(List<LostAndFoundWrapper> wapper) {
        mDatas = wapper;
        notifyDataSetChanged();
    }

    @Data
    public static class LostAndFoundWrapper {
        /**
         * 失物招领id
         */
        Long id;
        /**
         * 标题
         */
        String title;
        /**
         * 内容
         */
        String content;
        /**
         * 时间
         */
        Long time;
        /**
         * 1失物或者2招领
         * @see LostAndFound
         */
        Integer type;

        public LostAndFoundWrapper(Long id, String title, String content, Long time, Integer type) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.time = time;
            this.type = type;
        }
    }
}
