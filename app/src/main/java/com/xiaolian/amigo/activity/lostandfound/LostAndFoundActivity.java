package com.xiaolian.amigo.activity.lostandfound;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.lostandfound.adaptor.LostAndFoundAdaptor;
import com.xiaolian.amigo.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.base.BaseActivity;
import com.xiaolian.amigo.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.common.util.ScreenUtils;
import com.xiaolian.amigo.component.dialog.SearchDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领
 * <p>
 * Created by caidong on 2017/9/13.
 */
public class LostAndFoundActivity extends BaseActivity {
    static List<LostAndFoundAdaptor.Info> infos = new ArrayList<LostAndFoundAdaptor.Info>() {
        {
            add(new LostAndFoundAdaptor.Info("", "", "", "", ""));
            add(new LostAndFoundAdaptor.Info("", "", "", "", ""));
            add(new LostAndFoundAdaptor.Info("", "", "", "", ""));
            add(new LostAndFoundAdaptor.Info("", "", "", "", ""));
            add(new LostAndFoundAdaptor.Info("", "", "", "", ""));
        }
    };

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    @BindView(R.id.rv_infos)
    RecyclerView rv_infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        ButterKnife.bind(this);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new LostAndFoundAdaptor(infos);
        rv_infos.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        rv_infos.setLayoutManager(manager);
        rv_infos.setAdapter(adapter);
    }

    // 点击搜索
    @OnClick(R.id.tv_search)
    void search() {
        SearchDialog dialog = new SearchDialog(this);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }
}
