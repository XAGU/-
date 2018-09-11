package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  wcm
 * @Data  18/09/10
 *
 */

public class SocalContentAdapter extends CommonAdapter<SocalContentAdapter.SocialContentWrapper> {
    private static final String TAG = SocalContentAdapter.class.getSimpleName();
    private Animation animation ;
    private Context context ;
    private boolean animating  =false ;
    private OnItemClickListener onItemClickListener ;


    public SocalContentAdapter(Context context, int layoutId, List<SocalContentAdapter.SocialContentWrapper> datas ,OnItemClickListener onItemClickListener) {
        super(context, layoutId, datas);
        this.context = context ;
        this.onItemClickListener = onItemClickListener ;
    }

    @Override
    protected void convert(ViewHolder holder, SocalContentAdapter.SocialContentWrapper socialContentWrapper, int position) {
        holder.getView(R.id.praise).setOnClickListener(v -> {

            if (animating){
                return ;
            }

            holder.setImageResource(R.id.praise, R.drawable.icon_praise_sel);
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(context, R.anim.lost_found_like);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        animating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animating = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            holder.getView(R.id.praise).startAnimation(animation);
        });

        List<String> strings  = new ArrayList<>();
        for (int i = 0 ; i < 3 ; i++) {
            strings.add(Constant.IMAGE_PREFIX + "system/2.png");
        }
        setImage(holder ,strings);

        holder.setOnClickListener(R.id.linear, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(v , holder ,position );
            }
        });
    }

    private void setImage(ViewHolder holder ,List<String> imgs){
        RecyclerView recyclerView = holder.getView(R.id.images);
        SocialImgAdapter socialImgAdapter = new SocialImgAdapter(context ,R.layout.item_social_img , imgs);
        LinearLayoutManager    linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(context ,9)));
        recyclerView.setAdapter(socialImgAdapter);
    }



    public static final class SocialContentWrapper{


    }



}
