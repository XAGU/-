package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.enumeration.annotation.LostAndFound;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 失物招领发布
 *
 * @author zcd
 * @date 17/9/21
 */
@Deprecated
public class PublishLostAndFoundActivity extends LostAndFoundBaseActivity implements IPublishLostAndFoundView {
    private static final int REQUEST_IMAGE = 0x3302;
    private static final int IMAGE_COUNT = 3;
    public static final String KEY_TYPE = "publish_lost_and_found_key_type";
    @Inject
    IPublishLostAndFoundPresenter<IPublishLostAndFoundView> presenter;

    /**
     * 提交
     */
    @BindView(R.id.bt_submit)
    Button btSubmit;

    /**
     * 标题
     */
    @BindView(R.id.et_title)
    EditText etTitle;

    @BindView(R.id.tv_page_name)
    TextView tvPageName;

    /**
     * 描述
     */
    @BindView(R.id.et_desc)
    EditText etDesc;

    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_third)
    ImageView ivThird;

    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    private ImageAddAdapter imageAddAdapter;
    List<ImageAddAdapter.ImageItem> addImages = new ArrayList<>();

    List<String> images = new ArrayList<>();

    private boolean allValidated = false;
    private List<TextView> viewList;
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_lost_and_found);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(PublishLostAndFoundActivity.this);

        viewList = new ArrayList<TextView>() {
            {
                add(etTitle);
                add(etDesc);
            }
        };

        CommonUtil.showSoftInput(this, etTitle);
        initImageAdd();
        if (getIntent() != null) {
            if (getIntent().getIntExtra(KEY_TYPE, LostAndFound.LOST) == LostAndFound.LOST) {
                showPublishLost();
            } else {
                showPublishFound();
            }
        }
    }

    private void showPublishFound() {
        type = LostAndFound.FOUND;
        tvPageName.setText("发布招领");
    }

    private void showPublishLost() {
        type = LostAndFound.LOST;
        tvPageName.setText("发布失物");
    }

    private void initImageAdd() {
        addImages.add(new ImageAddAdapter.ImageItem());
        imageAddAdapter = new ImageAddAdapter(this, R.layout.item_image_add, addImages);
        imageAddAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (images.isEmpty() || (images.size() < IMAGE_COUNT && position == images.size())) {
                    getImage(imageUri ->
                            presenter.uploadImage(PublishLostAndFoundActivity.this,
                                    imageUri, position, OssFileType.FOUND));
                } else {
                    Intent intent = new Intent(PublishLostAndFoundActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, position);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_SINGLE, images.get(position));
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImage.addItemDecoration(new GridSpacesItemDecoration(3, ScreenUtils.dpToPxInt(this, 10), false));
        rvImage.setAdapter(imageAddAdapter);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }


    @OnTextChanged({R.id.et_desc,
            R.id.et_title})
    void onTextChange() {
        toggleBtnStatus();
    }

    public void toggleBtnStatus() {
        allValidated = !TextUtils.isEmpty(etTitle.getText())
                && !TextUtils.isEmpty(etDesc.getText());
        btSubmit.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    @OnClick(R.id.bt_submit)
    void publishLostAndFound() {
        if (!allValidated) {
            for (TextView view : viewList) {
                if (TextUtils.isEmpty(view.getText())) {
                    onError(view.getHint().toString());
                    return;
                }
            }
        }
        presenter.publishLostAndFound(etDesc.getText().toString(),
                images, etTitle.getText().toString(), type);
    }

    @Override
    public void addImage(String url, int position) {
        if (this.images.size() > position) {
            this.images.remove(position);
            this.images.add(position, url);
        } else {
            this.images.add(url);
        }
        refreshAddImage();
    }

    private void refreshAddImage() {
        addImages.clear();
        for (String image : images) {
            addImages.add(new ImageAddAdapter.ImageItem(image));
        }
        if (images.size() < IMAGE_COUNT) {
            addImages.add(new ImageAddAdapter.ImageItem());
        }
        imageAddAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    void chooseImage(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_first: {
                getImage(imageUri -> {

                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivFirst);
                    ivFirst.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivSecond.setVisibility(View.VISIBLE);
                    presenter.uploadImage(PublishLostAndFoundActivity.this,
                            imageUri, 0, OssFileType.FOUND);
                });
                break;
            }
            case R.id.iv_second: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivSecond);
                    ivSecond.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivThird.setVisibility(View.VISIBLE);
                    presenter.uploadImage(PublishLostAndFoundActivity.this,
                            imageUri, 1, OssFileType.FOUND);
                });
                break;
            }
            case R.id.iv_third: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivThird);
                    ivThird.setScaleType(ImageView.ScaleType.FIT_XY);
                    presenter.uploadImage(PublishLostAndFoundActivity.this,
                            imageUri, 2, OssFileType.FOUND);
                });
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            int position = data.getIntExtra(AlbumItemActivity.INTENT_POSITION, -1);
            if (position != -1) {
                images.remove(position);
                refreshAddImage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
