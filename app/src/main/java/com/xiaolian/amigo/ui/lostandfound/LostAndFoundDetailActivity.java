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

public class LostAndFoundDetailActivity extends LostAndFoundBaseActivity implements ILostAndFoundDetailView {

    public static final String INTENT_KEY_LOST_AND_FOUND_DETAIL_ID = "intent_key_lost_and_found_detail_id";
    public static final String INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE = "intent_key_lost_and_found_detail_type";
    public static final int TYPE_LOST = 1;
    public static final int TYPE_FOUND = 2;
    private static final int FIRST_IMAGE_INDEX = 0;
    private static final int SECOND_IMAGE_INDEX = 1;
    private static final int THIRD_IMAGE_INDEX = 2;

    @Inject
    ILostAndFoundDetailPresenter<ILostAndFoundDetailView> presenter;

    @SuppressWarnings("FieldCanBeLocal")
    private Long id;

    private ArrayList<String> images = new ArrayList<>();

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tvTitle;

    /**
     * 内容标题
     */
    @BindView(R.id.tv_content_title)
    TextView tvContentTitle;

    /**
     * 内容描述
     */
    @BindView(R.id.tv_content_desc)
    TextView tvContentDesc;

    /**
     * 物品名称
     */
    @BindView(R.id.tv_itemName)
    TextView tvItemName;

    /**
     * 位置
     */
    @BindView(R.id.tv_location)
    TextView tvLocation;

    /**
     * 时间
     */
    @BindView(R.id.tv_lostTime)
    TextView tvLostTime;

    /**
     * 联系方式
     */
    @BindView(R.id.tv_mobile)
    TextView tvMobile;

    /**
     * 发布时间
     */
    @BindView(R.id.tv_createTime)
    TextView tvCreateTime;

    /**
     * 图片布局
     */
    @BindView(R.id.ll_images)
    LinearLayout llImages;

    @BindView(R.id.iv_first)
    ImageView ivFirst;

    @BindView(R.id.iv_second)
    ImageView ivSecond;

    @BindView(R.id.iv_third)
    ImageView ivThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(LostAndFoundDetailActivity.this);

        if (getIntent() != null) {
            if (getIntent().getIntExtra(INTENT_KEY_LOST_AND_FOUND_DETAIL_TYPE, TYPE_FOUND) == TYPE_LOST) {
                tvTitle.setText("失物详情");
            } else {
                tvTitle.setText("招领详情");
            }
            id = getIntent().getLongExtra(INTENT_KEY_LOST_AND_FOUND_DETAIL_ID, Constant.INVALID_ID);
            presenter.getLostAndFoundDetail(id);
        }
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void render(LostAndFound lostAndFound) {
        tvContentTitle.setText(lostAndFound.getTitle());
        tvContentDesc.setText(lostAndFound.getDescription());
        tvItemName.setText(lostAndFound.getItemName());
        tvLocation.setText(lostAndFound.getLocation());
        tvMobile.setText(lostAndFound.getMobile());
        tvLostTime.setText(getString(R.string.time_format,
                TimeUtils.convertTimestampToFormat(lostAndFound.getLostTime()),
                TimeUtils.millis2String(lostAndFound.getLostTime(), TimeUtils.MY_TIME_FORMAT)));
        tvCreateTime.setText(getString(R.string.publish_time,
                TimeUtils.convertTimestampToFormat(lostAndFound.getCreateTime()),
                TimeUtils.millis2String(lostAndFound.getCreateTime(), TimeUtils.MY_TIME_FORMAT)));
        List<String> images = lostAndFound.getImages();
        if (null != images) {
            this.images.clear();
            this.images.addAll(images);
            // 获取图片数量
            int num = images.size();
            RequestManager manager = Glide.with(this);
            // 渲染第一张图
            if (num > FIRST_IMAGE_INDEX) {
                llImages.setVisibility(View.VISIBLE);
                ivFirst.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(0)).into(ivFirst);
            }
            // 渲染第二张图
            if (num > SECOND_IMAGE_INDEX) {
                ivSecond.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(1)).into(ivSecond);
            }
            // 渲染第三张图
            if (num > THIRD_IMAGE_INDEX) {
                ivThird.setVisibility(View.VISIBLE);
                manager.load(Constant.IMAGE_PREFIX + images.get(2)).into(ivThird);
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
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
