package com.xiaolian.amigo.activity.favorite;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.favorite.adaptor.FavoriteAdaptor;
import com.xiaolian.amigo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caidong on 2017/9/11.
 */

public class FavoriteActivity extends BaseActivity {

    static List<FavoriteAdaptor.Favorite> favorites = new ArrayList<FavoriteAdaptor.Favorite>() {
        {
            add(new FavoriteAdaptor.Favorite(1, "xxxxxxx"));
            add(new FavoriteAdaptor.Favorite(1, "xxxxxxx"));
            add(new FavoriteAdaptor.Favorite(1, "xxxxxxx"));
        }
    };

    @BindView(R.id.lv_collections)
    ListView lv_collections;

    FavoriteAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        adapter = new FavoriteAdaptor(this, favorites);
        lv_collections.setAdapter(adapter);
//        adapter.setMode(Attributes.Mode.Single);
    }
}
