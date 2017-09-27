package com.xiaolian.amigo.ui.user;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditAvatarVIew;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.pageloader.WrapperAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更换头像
 * <p>
 * Created by zcd on 9/27/17.
 */

public class EditAvatarActivity extends UserBaseActivity implements IEditAvatarVIew{
    public static final String INTENT_KEY_CURRENT_AVATAR = "intent_key_current_avatar";

    @Inject
    IEditAvatarPresenter<IEditAvatarVIew> presenter;

    List<EditAvatarAdaptor.AvatarWrapper> avatars = new ArrayList<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_current_avatar)
    ImageView iv_current_avatar;

    EditAvatarAdaptor adaptor;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditAvatarActivity.this);

        if (getIntent() != null) {
            String avatarUrl = getIntent().getStringExtra(INTENT_KEY_CURRENT_AVATAR);
            if (!TextUtils.isEmpty(avatarUrl)) {
                Glide.with(this).load(avatarUrl).asBitmap().into(iv_current_avatar);
            } else {
                iv_current_avatar.setImageResource(R.drawable.ic_picture_error);
            }
        }

        adaptor = new EditAvatarAdaptor(this, R.layout.item_avatar, avatars);
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

    @Override
    public void addAvatar(List<EditAvatarAdaptor.AvatarWrapper> avatar) {
        avatars.addAll(avatar);
        adaptor.notifyDataSetChanged();
    }
}
