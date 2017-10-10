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
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 失物招领详情
 * <p>
 * Created by zcd on 9/21/17.
 */

public class LostAndFoundDetailActivity extends LostAndFoundBaseActivity implements ILostAndFoundDetailView {

    public static final String INTENT_KEY_LOST_AND_FOUND_DETAIL_ID = "intent_key_lost_and_found_detail_id";
    public static final String INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE = "intent_key_lost_and_found_detail_type";
    public static final int TYPE_LOST = 1;
    public static final int TYPE_FOUND = 2;

    @Inject
    ILostAndFoundDetailPresenter<ILostAndFoundDetailView> presenter;

    private Long id;

    private ArrayList<String> images = new ArrayList<>();

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 内容标题
     */
    @BindView(R.id.tv_content_title)
    TextView tv_content_title;

    /**
     * 内容描述
     */
    @BindView(R.id.tv_content_desc)
    TextView tv_content_desc;

    /**
     * 物品名称
     */
    @BindView(R.id.tv_itemName)
    TextView tv_itemName;

    /**
     * 位置
     */
    @BindView(R.id.tv_location)
    TextView tv_location;

    /**
     * 时间
     */
    @BindView(R.id.tv_lostTime)
    TextView tv_lostTime;

    /**
     * 联系方式
     */
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    /**
     * 发布时间
     */
    @BindView(R.id.tv_createTime)
    TextView tv_createTime;

    /**
     * 图片布局
     */
    @BindView(R.id.ll_images)
    LinearLayout ll_images;

    @BindView(R.id.iv_first)
    ImageView iv_first;

    @BindView(R.id.iv_second)
    ImageView iv_second;

    @BindView(R.id.iv_third)
    ImageView iv_third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(LostAndFoundDetailActivity.this);

        if (getIntent() != null) {
            if (getIntent().getExtras().getInt(INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE) == TYPE_LOST) {
                tv_title.setText("失物详情");
            } else {
                tv_title.setText("招领详情");
            }
            id = getIntent().getExtras().getLong(INTENT_KEY_LOST_AND_FOUND_DETAIL_ID);
            presenter.getLostAndFoundDetail(id);
        }
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void render(LostAndFound lostAndFound) {
        tv_content_title.setText(lostAndFound.getTitle());
        tv_content_desc.setText(lostAndFound.getDescription());
        tv_itemName.setText(lostAndFound.getItemName());
        tv_location.setText(lostAndFound.getLocation());
        tv_mobile.setText(lostAndFound.getMobile());
        tv_lostTime.setText(TimeUtils.convertTimestampToFormat(lostAndFound.getLostTime()) +
                "/" + TimeUtils.millis2String(lostAndFound.getLostTime(), TimeUtils.MY_TIME_FORMAT));
        tv_createTime.setText(TimeUtils.convertTimestampToFormat(lostAndFound.getCreateTime()) +
                "/" + TimeUtils.millis2String(lostAndFound.getCreateTime(), TimeUtils.MY_TIME_FORMAT));
        List<String> images = lostAndFound.getImages();
        if(null != images){
            this.images.clear();
            this.images.addAll(images);
            // 获取图片数量
            int num = images.size();
            RequestManager manager = Glide.with(this);
            // 渲染第一张图
            if (num > 0) {
                ll_images.setVisibility(View.VISIBLE);
                iv_first.setVisibility(View.VISIBLE);
                manager.load(images.get(0)).into(iv_first);
            }
            // 渲染第二张图
            if (num > 1) {
                iv_second.setVisibility(View.VISIBLE);
                manager.load(images.get(1)).into(iv_second);
            }
            // 渲染第三张图
            if (num > 2) {
                iv_third.setVisibility(View.VISIBLE);
                manager.load(images.get(2)).into(iv_third);
            }
        }
    }

    @OnClick(R.id.iv_first)
    void onFirstImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 0);
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

    @OnClick(R.id.iv_second)
    void onSecondImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 1);
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

    @OnClick(R.id.iv_third)
    void onThirdImageClick() {
        if (images != null) {
            Intent intent = new Intent(this, AlbumItemActivity.class);
            intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, 2);
            intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, images);
            startActivity(intent);
        }
    }

    @Override
    protected boolean isShowLoading() {
        return true;
    }
}
