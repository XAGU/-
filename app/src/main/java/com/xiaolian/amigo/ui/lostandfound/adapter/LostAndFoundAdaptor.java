package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFound;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 9/18/17.
 */

public class LostAndFoundAdaptor extends CommonAdapter<LostAndFoundAdaptor.LostAndFoundWapper>{
    public LostAndFoundAdaptor(Context context, int layoutId, List<LostAndFoundWapper> datas) {
        super(context, layoutId, datas);
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
        holder.setText(R.id.tv_time, lostAndFoundWapper.getTime());
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
            if (null != lostAndFound.getImages() && lostAndFound.getImages().size() > 0) {
                this.hasImage = true;
            } else {
                this.hasImage = false;
            }

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
        String time;
        // 是否包含图片
        boolean hasImage;

        Long id;
    }
}
