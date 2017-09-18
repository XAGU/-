package com.xiaolian.amigo.ui.favorite;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.ui.order.OrderActivity;
import com.xiaolian.amigo.ui.order.adaptor.OrderAdaptor;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/18.
 */

public class FavoriteActivity extends FavoriteBaseActivity {

    List<FavoriteAdaptor.FavoriteWrapper> favorites = new ArrayList<FavoriteAdaptor.FavoriteWrapper>() {
        {
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
            add(new FavoriteAdaptor.FavoriteWrapper(1, "xxxxxxx"));
        }
    };

    @BindView(R.id.rv_favorites)
    RecyclerView rv_favorites;

    // 收藏设备recycleView适配器
    FavoriteAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

//        presenter.onAttach(this);

        adaptor = new FavoriteAdaptor(favorites);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_favorites.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        rv_favorites.setLayoutManager(manager);
        rv_favorites.setAdapter(adaptor);

//        presenter.requestNetWork(Constant.PAGE_START_NUM);
    }

    @Override
    protected void setUp() {

    }
}
