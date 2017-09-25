package com.xiaolian.amigo.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.favorite.FavoriteActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.repair.RepairNavActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by yik on 2017/9/5.
 */

public class ProfileFragment extends Fragment {


    @BindView(R.id.profileMenuListView)
    ListView profileMenuListView;
    List<MenuListViewAdapter.Item> listData = new ArrayList<>();
    boolean inited = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View homeView = inflater.inflate(R.layout.activity_tab_profile, container, false);
        ButterKnife.bind(this, homeView);

        if (!inited) {
            MenuListViewAdapter.Item editItem = new MenuListViewAdapter.Item(R.drawable.profile_edit, "编辑个人信息", com.xiaolian.amigo.ui.user.EditProfileActivity.class);
            listData.add(editItem);

            MenuListViewAdapter.Item walletItem = new MenuListViewAdapter.Item(R.drawable.profile_wallet, "我的钱包", com.xiaolian.amigo.ui.wallet.WalletActivity.class);
            listData.add(walletItem);

            MenuListViewAdapter.Item BonusItem = new MenuListViewAdapter.Item(R.drawable.profile_luck, "我的红包", com.xiaolian.amigo.ui.bonus.BonusActivity.class);
            listData.add(BonusItem);

            MenuListViewAdapter.Item orderItem = new MenuListViewAdapter.Item(R.drawable.profile_order, "消费记录", OrderActivity.class);
            listData.add(orderItem);

            MenuListViewAdapter.Item collectionItem = new MenuListViewAdapter.Item(R.drawable.profile_favorite, "我收藏的设备", FavoriteActivity.class);
            listData.add(collectionItem);

            MenuListViewAdapter.Item repairItem = new MenuListViewAdapter.Item(R.drawable.profile_repair, "设备报修", RepairNavActivity.class);
            listData.add(repairItem);

            MenuListViewAdapter.Item moreItem = new MenuListViewAdapter.Item(R.drawable.profile_more, "更多", this.getActivity().getClass());
            listData.add(moreItem);
            
            inited = true;
        }

        MenuListViewAdapter menuListViewAdapter = new MenuListViewAdapter(this.getActivity().getApplicationContext(), listData);
        profileMenuListView.setAdapter(menuListViewAdapter);

        return homeView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnItemClick(R.id.profileMenuListView)
    void forward(AdapterView<?> parent, View view, int position, long id) {
        if (position == 6) {
            ((MainActivity)getActivity()).logout();
            ((MainActivity)getActivity()).startActivity(LoginActivity.class);
        } else {
            ((MainActivity)getActivity()).startActivity(listData.get(position).activityClazz);
        }
    }


}
