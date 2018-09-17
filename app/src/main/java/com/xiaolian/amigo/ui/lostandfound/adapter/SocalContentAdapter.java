package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.util.GildeUtils;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author  wcm
 * @Data  18/09/10
 *
 */

public class SocalContentAdapter extends CommonAdapter<LostAndFoundDTO> {
    private static final String TAG = SocalContentAdapter.class.getSimpleName();
    private Animation animation ;
    private Context context ;
    private boolean animating  =false ;
    private OnItemClickListener onItemClickListener ;
    private LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener ;
    /**
     * 是否本人点赞
     */
    private boolean liked = false;

    public SocalContentAdapter(Context context, int layoutId, List<LostAndFoundDTO> datas , OnItemClickListener onItemClickListener
        ,LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener) {
        super(context, layoutId, datas);
        this.context = context ;
        this.onItemClickListener = onItemClickListener ;
        this.likeClickListener = likeClickListener ;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundDTO dto, int position) {

        holder.setImageResource(R.id.praise,
                ObjectsCompat.equals(dto.getLiked() ,1 ) ?
                        R.drawable.icon_praise_sel : R.drawable.ic_unlike);
        holder.getView(R.id.praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (likeClickListener != null) {
                    Integer likeCount = dto.getLikeCount();
                    if (dto.getLiked() == 1) {
                        dto.setLiked(2);
                        dto.setLikeCount(likeCount- 1);
                        holder.setImageResource(R.id.praise, R.drawable.ic_unlike);
                        likeClickListener.onLikeClick(position, dto.getId(), true);
                    } else {
                        dto.setLiked(1);
                        dto.setLikeCount(likeCount + 1);
                        holder.setImageResource(R.id.praise, R.drawable.icon_praise_sel);
                        likeClickListener.onLikeClick(position, dto.getId(), false);
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
                    }
                }
            }
        });

        CircleImageView avator = holder.getView(R.id.iv_avatar);
        GildeUtils.setImage(context ,avator ,dto.getPictureUrl());
        holder.setText(R.id.name , dto.getUser());
        holder.setText(R.id.title ,dto.getTitle());
        holder.setText(R.id.content ,dto.getDescription());
        holder.setText(R.id.praise_num ,dto.getLikeCount()+"");

        if (dto.getCommentEnable()) {
            holder.setText(R.id.message_num, dto.getCommentsCount() + "");
            showMessage(holder ,true);
        }else {
            showMessage(holder , false);
        }
        holder.setText(R.id.time , TimeUtils.orderTimestampFormatSocial(dto.getCreateTime()) +"发布");
        setImage(holder ,dto.getImages());
        holder.setText(R.id.type , dto.getTopicName());
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

    /**
     * 是否显示评论
     * @param holder
     * @param isCommentEnable
     */
    private void showMessage(ViewHolder holder ,boolean isCommentEnable){
        if (isCommentEnable){
            holder.getView(R.id.message_num).setVisibility(View.VISIBLE);
            holder.getView(R.id.message).setVisibility(View.VISIBLE);
            holder.getView(R.id.praise).setVisibility(View.VISIBLE);
            holder.getView(R.id.praise_num).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.message_num).setVisibility(View.GONE);
            holder.getView(R.id.message).setVisibility(View.GONE);
            holder.getView(R.id.praise).setVisibility(View.GONE);
            holder.getView(R.id.praise_num).setVisibility(View.GONE);
        }
    }


}
