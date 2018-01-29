package com.xiaolian.amigo.ui.main.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.DotFlashView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * <p>
 * Created by zcd on 10/11/17.
 */

public class HomeSmallDelegate implements ItemViewDelegate<HomeAdaptor.ItemWrapper> {
    private static final String TAG = HomeSmallDelegate.class.getSimpleName();
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private Context context;

    public HomeSmallDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home_small;
    }

    @Override
    public boolean isForViewType(HomeAdaptor.ItemWrapper item, int position) {
        return item.getType() == HomeAdaptor.SMALL_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, HomeAdaptor.ItemWrapper itemWrapper, int position) {
        if (itemWrapper.isActive()) {
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
        if (position % 2 == 0) {
            try {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.getView(R.id.rl_item).getLayoutParams();
                lp.setMargins(41, 0, 0, 0);
                holder.getView(R.id.rl_item).setLayoutParams(lp);
            } catch (Exception e) {
            }
        } else {
            try {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.getView(R.id.rl_item).getLayoutParams();
                lp.setMargins(0, 0, 41, 0);
                holder.getView(R.id.rl_item).setLayoutParams(lp);
            } catch (Exception e) {
            }
        }
        holder.getView(R.id.rl_item).setBackgroundResource(itemWrapper.getSmallRes());
        holder.setText(R.id.tv_device_title, itemWrapper.getDeviceName());
        holder.setText(R.id.tv_desc, itemWrapper.getDesc());
        if (itemWrapper.getPrepaySize() != 0) {
            holder.getView(R.id.tv_prepay).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_prepay, "(有" + itemWrapper.getPrepaySize() + "笔未找零金额)");
            holder.getView(R.id.tv_desc).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_prepay).setVisibility(View.GONE);
            holder.getView(R.id.tv_desc).setVisibility(View.VISIBLE);
        }
        if (itemWrapper.isUsing()) {
            holder.setText(R.id.tv_device_title, "正在使用");
            holder.getView(R.id.dfv_dot).setVisibility(View.VISIBLE);
            ((DotFlashView)holder.getView(R.id.dfv_dot)).startAnimation();
        } else {
            holder.setText(R.id.tv_device_title, itemWrapper.getDeviceName());
            holder.getView(R.id.dfv_dot).setVisibility(View.GONE);
            ((DotFlashView)holder.getView(R.id.dfv_dot)).endAnimation();
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
