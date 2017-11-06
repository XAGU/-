package com.xiaolian.amigo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.favorite.FavoriteActivity;
import com.xiaolian.amigo.ui.main.adaptor.ProfileAdaptor;
import com.xiaolian.amigo.ui.more.MoreActivity;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.repair.RepairNavActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.wallet.WalletActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class ProfileFragment2 extends Fragment {
    ProfileAdaptor.Item wallet = new ProfileAdaptor.Item(R.drawable.profile_wallet, "我的钱包", WalletActivity.class);
    ProfileAdaptor.Item bonus = new ProfileAdaptor.Item(R.drawable.profile_luck, "我的红包", BonusActivity.class);
    ProfileAdaptor.Item repair = new ProfileAdaptor.Item(R.drawable.profile_repair, "设备报修", RepairNavActivity.class);

    List<ProfileAdaptor.Item> items = new ArrayList<ProfileAdaptor.Item>() {
        {
            add(new ProfileAdaptor.Item(R.drawable.profile_edit, "编辑个人信息", EditProfileActivity.class));
            add(wallet);
            add(bonus);
            add(new ProfileAdaptor.Item(R.drawable.profile_order, "消费记录", OrderActivity.class));
            add(new ProfileAdaptor.Item(R.drawable.profile_favorite, "我收藏的设备", FavoriteActivity.class));
            add(repair);
            add(new ProfileAdaptor.Item(R.drawable.profile_more, "更多", MoreActivity.class));
        }
    };

    ProfileAdaptor adaptor;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptor = new ProfileAdaptor(getActivity(), R.layout.item_profile, items);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.START_ACTIVITY,
                        items.get(position).getActivityClazz()));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_left);
        recyclerView.setLayoutAnimation(animation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PersonalExtraInfoDTO data) {
        wallet.setBalance(data.getBalance());
        bonus.setBonusAmount(data.getBonusAmount());
        if (data.isNeedShowDot()) {
            repair.setShowDot(true);
        } else {
            repair.setShowDot(false);
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adaptor);
        } else {
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.REFRESH_NOTICE));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
