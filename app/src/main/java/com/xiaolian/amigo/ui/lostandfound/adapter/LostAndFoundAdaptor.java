package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 9/18/17.
 */

public class LostAndFoundAdaptor extends CommonAdapter<LostAndFoundAdaptor.LostAndFoundWapper>{
    private Context context;
    private boolean isShowIcon;
    public LostAndFoundAdaptor(Context context, int layoutId, List<LostAndFoundWapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public LostAndFoundAdaptor(Context context, int layoutId, List<LostAndFoundWapper> datas,
                               boolean isShowIcon) {
        super(context, layoutId, datas);
        this.context = context;
        this.isShowIcon = isShowIcon;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundWapper lostAndFoundWapper, int position) {
        holder.setText(R.id.tv_title, lostAndFoundWapper.getTitle());
        if (lostAndFoundWapper.hasImage) {
            holder.setImageResource(R.id.iv_image, R.drawable.picture_light);
        } else {
            holder.setImageResource(R.id.iv_image, R.drawable.picture_gray);
        }
        holder.setText(R.id.tv_content, lostAndFoundWapper.getContent());
        holder.setText(R.id.tv_good, lostAndFoundWapper.getGood());
        holder.setText(R.id.tv_location, lostAndFoundWapper.getLocation());
        holder.setText(R.id.tv_time, TimeUtils.convertTimestampToFormat(lostAndFoundWapper.getTime())
                + "/" + TimeUtils.millis2String(lostAndFoundWapper.getTime(), TimeUtils.MY_TIME_FORMAT));
        if (position == 0) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            marginLayoutParams.setMargins(0, ScreenUtils.dpToPxInt(this.context, 10), 0, 0);
            holder.itemView.setLayoutParams(marginLayoutParams);
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            marginLayoutParams.setMargins(0, 0, 0, 0);
            holder.itemView.setLayoutParams(marginLayoutParams);
        }

        if (isShowIcon) {
            holder.getView(R.id.iv_icon).setVisibility(View.VISIBLE);
            if (CommonUtil.equals(lostAndFoundWapper.getType(), 1)) {
                // 失物
                ((ImageView)holder.getView(R.id.iv_icon)).setImageResource(R.drawable.ic_lost);
            } else {
                ((ImageView)holder.getView(R.id.iv_icon)).setImageResource(R.drawable.ic_found);
            }
        } else {
            holder.getView(R.id.iv_icon).setVisibility(View.GONE);
        }
    }

    public void replaceData(List<LostAndFoundWapper> wapper) {
        mDatas = wapper;
        notifyDataSetChanged();
    }

    @Data
    public static class LostAndFoundWapper {

        public LostAndFoundWapper(LostAndFound lostAndFound) {
            this.id = lostAndFound.getId();
            this.title = lostAndFound.getTitle();
            this.content = lostAndFound.getDescription();
            this.good = lostAndFound.getItemName();
            this.location = lostAndFound.getLocation();
            this.time = lostAndFound.getCreateTime();
            this.hasImage = null != lostAndFound.getImages() && lostAndFound.getImages().size() > 0;
            this.type = lostAndFound.getType();
        }

        // 标题
        String title;
        // 内容
        String content;
        // 物品
        String good;
        // 地点
        String location;
        // 时间
        Long time;
        // 是否包含图片
        boolean hasImage;
        Long id;
        // 1失物或者2招领
        Integer type;
    }
}
