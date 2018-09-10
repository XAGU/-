package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SocalContentAdapter extends CommonAdapter<SocalContentAdapter.SocialContentWrapper> {

    private Animation animation ;
    private Context context ;
    private boolean animating  =false ;
    private LinearLayoutManager linearLayoutManager ;

    public SocalContentAdapter(Context context, int layoutId, List<SocalContentAdapter.SocialContentWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context ;
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


    }

    private void setImage(ViewHolder holder ,List<String> imgs){
        RecyclerView recyclerView = holder.getView(R.id.images);
        if (linearLayoutManager == null){
            linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(context ,9)));
        recyclerView.setAdapter(new CommonAdapter<String>() {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }
        });
    }



    public static final class SocialContentWrapper{


    }



}
