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
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;
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

public class SocalContentAdapter2 extends CommonAdapter<LostAndFoundDTO> {
    private static final String TAG = SocalContentAdapter2.class.getSimpleName();
    private Animation animation ;
    private Context context ;
    private boolean animating  =false ;
    private OnItemClickListener onItemClickListener ;
    private LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener ;
    private SocialImgAdapter.PhotoClickListener photoClickListener ;
    /**
     * 是否本人点赞
     */
    private boolean liked = false;

    private ILostAndFoundPresenter2<ILostAndFoundView2> presenter ;

    public SocalContentAdapter2(Context context, int layoutId, List<LostAndFoundDTO> datas , OnItemClickListener onItemClickListener
        , LostAndFoundDetailContentDelegate.OnLikeClickListener likeClickListener) {
        super(context, layoutId, datas);
        this.context = context ;
        this.onItemClickListener = onItemClickListener ;
        this.likeClickListener = likeClickListener ;
    }


    public void setPhotoClickListener(SocialImgAdapter.PhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    public void setPresenter(ILostAndFoundPresenter2<ILostAndFoundView2> presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, LostAndFoundDTO dto, int position ) {

        holder.getView(R.id.ll_operator).setOnClickListener(v -> {
            if (presenter != null) presenter.deleteLostAndFounds(dto.getId() , position);
        });

        holder.setImageResource(R.id.praise,
                ObjectsCompat.equals(dto.getLiked() ,1 ) ?
                        R.drawable.icon_praise_sel : R.drawable.ic_unlike);
        holder.getView(R.id.praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                like(dto, holder, position);
            }
        });

        holder.getView(R.id.praise_num).setOnClickListener(v -> {
            like(dto, holder, position);
        });

        CircleImageView avator = holder.getView(R.id.iv_avatar);
        GildeUtils.setImage(context ,avator ,dto.getPictureUrl());
        holder.setText(R.id.name , dto.getUser());
        holder.setText(R.id.title ,dto.getTitle());
        holder.setText(R.id.content ,dto.getDescription());
        holder.setText(R.id.praise_num ,dto.getLikeCount()+"");
        holder.setText(R.id.name, dto.getNickname());

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

    private void like(LostAndFoundDTO dto, ViewHolder holder, int position) {
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

    private void setImage(ViewHolder holder ,List<String> imgs){
        RecyclerView recyclerView = holder.getView(R.id.images);
        SocialImgAdapter socialImgAdapter = new SocialImgAdapter(context ,R.layout.item_social_img , imgs);
        LinearLayoutManager    linearLayoutManager = new LinearLayoutManager(context);
        socialImgAdapter.setPhotoClickListener(photoClickListener);
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
