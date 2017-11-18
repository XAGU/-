package com.xiaolian.amigo.ui.user;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarVIew;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更换头像
 * <p>
 * Created by zcd on 9/27/17.
 */

public class EditAvatarActivity extends UserBaseActivity implements IEditAvatarVIew {
    public static final String INTENT_KEY_CURRENT_AVATAR = "intent_key_current_avatar";

    @Inject
    IEditAvatarPresenter<IEditAvatarVIew> presenter;

    List<EditAvatarAdaptor.AvatarWrapper> avatars = new ArrayList<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_current_avatar)
    ImageView iv_current_avatar;

    @BindView(R.id.bt_submit)
    Button bt_submit;

    EditAvatarAdaptor adaptor;
    private String avatarUrl;
    private MultiItemTypeAdapter.OnItemClickListener itemClickListener;
    public int lastSelectedPosition = -1;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditAvatarActivity.this);

        if (getIntent() != null) {
            avatarUrl = getIntent().getStringExtra(INTENT_KEY_CURRENT_AVATAR);
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(this).load(Constant.IMAGE_PREFIX + avatarUrl)
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(iv_current_avatar);
            } else {
                iv_current_avatar.setImageResource(R.drawable.ic_picture_error);
            }
        }

        adaptor = new EditAvatarAdaptor(this, R.layout.item_avatar, avatars);
        itemClickListener = new MultiItemTypeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (lastSelectedPosition != -1) {
                    avatars.get(lastSelectedPosition).setSelected(false);
                }
                lastSelectedPosition = position;
                avatars.get(position).setSelected(true);
                avatarUrl = avatars.get(position).getAvatarUrl();
                if (!TextUtils.isEmpty(avatarUrl)) {
                    Glide.with(EditAvatarActivity.this).load(Constant.IMAGE_PREFIX + avatarUrl)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .into(iv_current_avatar);
                }
                toggleSubmitBtnStatus();
                adaptor.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        };
        adaptor.setOnItemClickListener(itemClickListener);
        recyclerView.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 21), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adaptor);

        presenter.getAvatarList();
    }

    @Override
    protected int setTitle() {
        return R.string.change_avatar;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_edit_avatar;
    }

    @OnClick(R.id.iv_current_avatar)
    void setCustomAvatar() {
        getImage(imageUri -> {
            // 在上次选择默认头像的情况下，这次选择自定义头像，则取消上次的选中状态
            if (lastSelectedPosition != -1) {
                avatars.get(lastSelectedPosition).setSelected(false);
                lastSelectedPosition = -1;
                adaptor.notifyDataSetChanged();
            }
            presenter.uploadImage(EditAvatarActivity.this, imageUri);
        });
    }

    @Override
    public void setAvatar(String pictureUrl) {
        if (!TextUtils.isEmpty(pictureUrl)) {
            avatarUrl = pictureUrl;
            Glide.with(this).load(Constant.IMAGE_PREFIX + pictureUrl).asBitmap().into(iv_current_avatar);
        }
        toggleSubmitBtnStatus();
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void addAvatar(List<EditAvatarAdaptor.AvatarWrapper> avatar) {
        for (int i = 0; i < avatar.size(); i++) {
            if (TextUtils.equals(avatar.get(i).getAvatarUrl(), avatarUrl)) {
                avatar.get(i).setSelected(true);
                lastSelectedPosition = i;
            }
        }
        for (EditAvatarAdaptor.AvatarWrapper wrapper : avatar) {
            if (TextUtils.equals(wrapper.getAvatarUrl(), avatarUrl)) {
                wrapper.setSelected(true);
            }
        }
        avatars.addAll(avatar);
        adaptor.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_submit)
    void changeAvatar() {
        presenter.updateAvatarUrl(avatarUrl);
    }


    /**
     * 触发提交按钮状态更新
     */
    public void toggleSubmitBtnStatus() {
        boolean condition = !TextUtils.isEmpty(avatarUrl);
        bt_submit.setEnabled(condition);
        bt_submit.getBackground().setAlpha(condition ? 255 : 100);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
