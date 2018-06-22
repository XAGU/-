package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.BannerType;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.TimeUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 失物招领详情内容
 *
 * @author zcd
 * @date 18/5/14
 */

public class LostAndFoundDetailContentDelegate
        implements ItemViewDelegate<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> {
    private static final String TAG = LostAndFoundDetailContentDelegate.class.getSimpleName();
    private static final int FIRST_IMAGE_INDEX = 0;
    private static final int SECOND_IMAGE_INDEX = 1;
    private static final int THIRD_IMAGE_INDEX = 2;

    private Context context;
    private ArrayList<String> images = new ArrayList<>();
    private OnLikeClickListener likeClickListener;
    private boolean animating = false;
    private Animation animation;

    public LostAndFoundDetailContentDelegate(Context context, OnLikeClickListener likeClickListener) {
        this.context = context;
        this.likeClickListener = likeClickListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lost_and_found_detail_top;
    }

    @Override
    public boolean isForViewType(LostAndFoundDetailAdapter.LostAndFoundDetailWrapper item, int position) {
        if (item == null) {
            return false;
        }
        return ObjectsCompat.equals(item.getItemType(),
                LostAndFoundDetailAdapter.LostAndFoundDetailItemType.CONTENT);
    }

    @Override
    public void convert(ViewHolder holder,
                        LostAndFoundDetailAdapter.LostAndFoundDetailWrapper lostAndFoundDetailWrapper, int position) {

        Glide.with(context).load(Constant.IMAGE_PREFIX + lostAndFoundDetailWrapper.getAvatar())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
        holder.setText(R.id.tv_content_author, lostAndFoundDetailWrapper.getContentAuthor());

        holder.setImageResource(R.id.iv_owner,
                ObjectsCompat.equals(lostAndFoundDetailWrapper.getType(), LostAndFound.LOST) ?
                        R.drawable.ic_lost_owner : R.drawable.ic_found_owner);

        holder.setText(R.id.tv_like_count, String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
        holder.setImageResource(R.id.iv_like,
                lostAndFoundDetailWrapper.isLiked() ?
                        R.drawable.ic_like : R.drawable.ic_unlike);
        holder.getView(R.id.iv_like).setOnClickListener(v -> {
            if (animating) {
                return;
            }
            if (likeClickListener != null) {
                Integer likeCount = lostAndFoundDetailWrapper.getLikeCount();
                if (lostAndFoundDetailWrapper.isLiked()) {
                    lostAndFoundDetailWrapper.setLiked(false);
                    lostAndFoundDetailWrapper.setLikeCount(likeCount - 1);
                    holder.setImageResource(R.id.iv_like, R.drawable.ic_unlike);
                    holder.setText(R.id.tv_like_count,
                            String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
                    likeClickListener.onLikeClick(position, lostAndFoundDetailWrapper.getId(), true);
                } else {
                    lostAndFoundDetailWrapper.setLiked(true);
                    lostAndFoundDetailWrapper.setLikeCount(likeCount + 1);
                    holder.setImageResource(R.id.iv_like, R.drawable.ic_like);
                    holder.setText(R.id.tv_like_count,
                            String.valueOf(lostAndFoundDetailWrapper.getLikeCount()));
                    likeClickListener.onLikeClick(position, lostAndFoundDetailWrapper.getId(), false);
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
                    holder.getView(R.id.iv_like).startAnimation(animation);
                }
            }
        });

        holder.setText(R.id.tv_content_title, lostAndFoundDetailWrapper.getContentTitle());
        holder.setText(R.id.tv_content_desc, lostAndFoundDetailWrapper.getContent());
        holder.setText(R.id.tv_time,
                TimeUtils.lostAndFoundTimestampFormat(lostAndFoundDetailWrapper.getTime()));
        setStat(holder.getView(R.id.tv_stat), lostAndFoundDetailWrapper.getViewCount(),
                lostAndFoundDetailWrapper.getCommentCount(), lostAndFoundDetailWrapper.isCommentEnable());
        LinearLayout llImages = holder.getView(R.id.ll_images);
        ImageView ivFirst = holder.getView(R.id.iv_first);
        ImageView ivSecond = holder.getView(R.id.iv_second);
        ImageView ivThird = holder.getView(R.id.iv_third);

        List<String> fetchedImages = lostAndFoundDetailWrapper.getImage();
        if (null != images) {
            this.images.clear();
            this.images.addAll(fetchedImages);
            // 获取图片数量
            int num = images.size();
            RequestManager manager = Glide.with(context);
            // 渲染第一张图
            if (num > FIRST_IMAGE_INDEX) {
                llImages.setVisibility(View.VISIBLE);
                ivFirst.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(0)).into(ivFirst);
            }
            // 渲染第二张图
            if (num > SECOND_IMAGE_INDEX) {
                ivSecond.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(1)).into(ivSecond);
            }
            // 渲染第三张图
            if (num > THIRD_IMAGE_INDEX) {
                ivThird.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(2)).into(ivThird);
            }

            holder.getView(R.id.iv_first).setOnClickListener(v -> {
                if (images != null) {
                    Intent intent = new Intent(context, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 0);
                    intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
                    context.startActivity(intent);
                }
            });
            holder.getView(R.id.iv_second).setOnClickListener(v -> {
                if (images != null) {
                    Intent intent = new Intent(context, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 1);
                    intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
                    context.startActivity(intent);
                }
            });
            holder.getView(R.id.iv_third).setOnClickListener(v -> {
                if (images != null) {
                    Intent intent = new Intent(context, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 2);
                    intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
                    context.startActivity(intent);
                }
            });
        } else {
            llImages.setVisibility(View.GONE);
        }
    }

    private void setStat(TextView textView, Integer viewCount, Integer commentCount, boolean commentEnable) {
        if (commentEnable) {
            textView.setText(String.format(Locale.getDefault(), "%d查看·%d回复", viewCount, commentCount));
        } else {
            textView.setText(String.format(Locale.getDefault(), "%d查看", viewCount));
        }
    }

    public void setOnLikeClickListener(OnLikeClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
    }

    public interface OnLikeClickListener {
        void onLikeClick(int position, long id, boolean like);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (path != null && path instanceof String) {
                Glide.with(context).load((String) path)
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(imageView);
            }
        }

    }
}
