package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundDetailActivity2 extends LostAndFoundBaseActivity implements ILostAndFoundDetailView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail2);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void render(LostAndFound lostAndFound) {
    }

    @Override
    protected void onDestroy() {
//        presenter.onDetach();
        super.onDestroy();
    }
}
