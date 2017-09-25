package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 统一添加toolbar的ListActivity
 * 未完成
 * <p>
 * Created by zcd on 9/25/17.
 */

public abstract class BaseToolBarListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private BGARefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.refreshLayout);
//        initInject();
//        initPresenter();
//        initRecyclerView();
//        setupPageLoader();
//        initRefreshLayout();
//        initData();
    }
}
