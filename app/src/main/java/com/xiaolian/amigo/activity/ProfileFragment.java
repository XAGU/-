package com.xiaolian.amigo.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.bonus.BonusActivity;
import com.xiaolian.amigo.activity.order.OrderActivity;
import com.xiaolian.amigo.activity.wallet.WalletActivty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

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
            MenuListViewAdapter.Item editItem = new MenuListViewAdapter.Item(R.drawable.profile_edit, "编辑个人信息", EditProfileActivity.class);
            listData.add(editItem);

            MenuListViewAdapter.Item walletItem = new MenuListViewAdapter.Item(R.drawable.profile_wallet, "我的钱包", WalletActivty.class);
            listData.add(walletItem);

            MenuListViewAdapter.Item BonusItem = new MenuListViewAdapter.Item(R.drawable.profile_luck, "我的红包", BonusActivity.class);
            listData.add(BonusItem);

            MenuListViewAdapter.Item orderItem = new MenuListViewAdapter.Item(R.drawable.profile_order, "消费记录", OrderActivity.class);
            listData.add(orderItem);
            
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
        Intent intent = new Intent(getActivity().getApplicationContext(), listData.get(position).activityClazz);
        startActivity(intent);
    }


}
