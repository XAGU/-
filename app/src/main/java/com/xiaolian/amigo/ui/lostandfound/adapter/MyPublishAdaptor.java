package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * <p>
 * Created by zcd on 9/18/17.
 */

public class MyPublishAdaptor extends CommonAdapter<LostAndFoundAdaptor.LostAndFoundWapper>{
    public interface MyPublishClickListener {
        void onMyPublishClick(int position);
    }
    public interface MyPublishLongClickListener {
        void onMyPublishLongClick();
    }
    public interface MyPublishDeleteListener {
        void onMyPublishDelete(int position);
    }
    private MyPublishClickListener clickListener;
    private MyPublishLongClickListener longClickListener;
    private MyPublishDeleteListener deleteListener;
    private Context context;
    private boolean isShowIcon;
    public MyPublishAdaptor(Context context, int layoutId, List<LostAndFoundAdaptor.LostAndFoundWapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public MyPublishAdaptor(Context context, int layoutId, List<LostAndFoundAdaptor.LostAndFoundWapper> datas,
                            boolean isShowIcon) {
        super(context, layoutId, datas);
        this.context = context;
        this.isShowIcon = isShowIcon;
    }

    public void setClickListener(MyPublishClickListener listener) {
        this.clickListener = listener;
    }

    public void setLongClickListener(MyPublishLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setDeleteListener(MyPublishDeleteListener listener) {
        this.deleteListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundAdaptor.LostAndFoundWapper lostAndFoundWapper, int position) {
        holder.getView(R.id.ll_item).setOnClickListener(v -> {
            clickListener.onMyPublishClick(holder.getAdapterPosition());
        });
        holder.getView(R.id.ll_item).setOnLongClickListener(v -> {
            longClickListener.onMyPublishLongClick();
            return true;
        });
        holder.getView(R.id.tv_delete).setOnClickListener(v -> {
            deleteListener.onMyPublishDelete(holder.getAdapterPosition());
        });
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

    public void replaceData(List<LostAndFoundAdaptor.LostAndFoundWapper> wapper) {
        mDatas = wapper;
        notifyDataSetChanged();
    }

}
