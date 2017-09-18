package com.xiaolian.amigo.tmp.activity.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.lostandfound.adaptor.LostAndFoundAdaptor;
import com.xiaolian.amigo.tmp.activity.repair.adaptor.RepairAdaptor;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.tmp.component.dialog.SearchDialog;

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

//    @BindView(R.id.rv_infos)
    RecyclerView rv_infos;

    /**
     * 发布招领
     */
    @BindView(R.id.tv_publish_found)
    TextView tv_publish_found;

    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_publish_found)
    void gotoPublishFound() {
        startActivity(new Intent(this, PublishFoundActivity.class));
    }

    /**
     * 发布失物
     */
    @BindView(R.id.tv_publish_lost)
    TextView tv_publish_lost;

    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_publish_lost)
    void gotoPublishLost() {
        startActivity(new Intent(this, PublishLostActivity.class));
    }
    /**
     * 我的发布
     */
    @BindView(R.id.tv_my_publish)
    TextView tv_my_publish;
    /**
     * 打开发布招领页面
     */
    @OnClick(R.id.tv_my_publish)
    void gotoMyPublish() {
        startActivity(new Intent(this, MyPublishActivity.class));
    }

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
