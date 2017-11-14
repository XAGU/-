package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 9/27/17.
 */

public class EditAvatarAdaptor extends CommonAdapter<EditAvatarAdaptor.AvatarWrapper> {

    private Context context;

    public EditAvatarAdaptor(Context context, int layoutId, List<AvatarWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, AvatarWrapper avatarWrapper, int position) {
        Glide.with(context).load(Constant.IMAGE_PREFIX + avatarWrapper.getAvatarUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into((ImageView) holder.getView(R.id.iv_avatar));
        holder.getView(R.id.iv_selected).setVisibility(avatarWrapper.isSelected() ? View.VISIBLE : View.GONE);
    }

    @Data
    public static class AvatarWrapper {
        String avatarUrl;
        boolean isSelected;

        public AvatarWrapper(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            this.isSelected = false;
        }
    }

}
