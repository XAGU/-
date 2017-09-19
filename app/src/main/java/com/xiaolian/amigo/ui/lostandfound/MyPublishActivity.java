package com.xiaolian.amigo.ui.lostandfound;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.lostandfound.adaptor.LostAndFoundAdaptor;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的发布页面
 * @author zcd
 */

public class MyPublishActivity extends BaseActivity {

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

    /**
     * 发布列表
     */
    @BindView(R.id.rv_my_publish)
    RecyclerView rv_my_publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);
        ButterKnife.bind(this);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new LostAndFoundAdaptor(infos);
        rv_my_publish.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        rv_my_publish.setLayoutManager(manager);
        rv_my_publish.setAdapter(adapter);

    }
}
