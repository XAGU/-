package com.xiaolian.amigo.ui.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.vo.LZTag;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostAndFoundView;
import com.xiaolian.amigo.ui.repair.adaptor.ImageAddAdapter;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.SoftInputUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

public class WriteLZActivity extends LostAndFoundBaseActivity implements IPublishLostAndFoundView {

    private static final String TAG = WriteLZActivity.class.getSimpleName();
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


    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_third)
    ImageView ivThird;


    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.topic)
    RecyclerView topic;
    @BindView(R.id.main_title)
    EditText mainTitle;
    @BindView(R.id.main_content)
    EditText mainContent;
    @BindView(R.id.scroll)
    ScrollView scroll;
    private ImageAddAdapter imageAddAdapter;
    List<ImageAddAdapter.ImageItem> addImages = new ArrayList<>();

    List<String> images = new ArrayList<>();

    private boolean allValidated = false;
    private List<EditText> viewList;
    private int type = -1;

    private List<LZTag> topics;

    private int screenWidth;
    private int imageWidth;


    private int lastTopPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writelz);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(WriteLZActivity.this);
        CommonUtil.showSoftInput(this, mainTitle);
        initImageAdd();
        initRecy();
        addListView();
    }


    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    private void initRecy() {
        topics = new ArrayList<>();
        if (presenter.getTopicList() != null) {
            for (BbsTopicListTradeRespDTO.TopicListBean topicListBean : presenter.getTopicList()) {
                topics.add(new LZTag(topicListBean.getTopicName(), false, topicListBean.getTopicId()));
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topic.setLayoutManager(linearLayoutManager);
        topic.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 10)));
        topic.setAdapter(new CommonAdapter<LZTag>(this, R.layout.item_socail_top, topics) {

            @Override
            protected void convert(ViewHolder holder, LZTag lzTag, int position) {
                TextView textView = holder.getView(R.id.topic_txt);
                if (lzTag.isCheck()) {
                    lastTopPosition = position;
                    textView.setBackgroundResource(R.drawable.social_top_sel);
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    textView.setBackgroundResource(R.drawable.socical_topic);
                    textView.setTextColor(getResources().getColor(R.color.colorDark9));
                }
                holder.setText(R.id.topic_txt, lzTag.getContent());
                textView.setOnClickListener(v -> {
                    if (!lzTag.isCheck()) {
                        lzTag.setCheck(true);

                        type = lzTag.getId();
                        if (lastTopPosition != -1 && lastTopPosition != position)
                            topics.get(lastTopPosition).setCheck(false);
                        notifyDataSetChanged();
                    }
                    onTextChange();
                    SoftInputUtils.hideSoftInputFromWindow(WriteLZActivity.this ,mainContent);
                    SoftInputUtils.hideSoftInputFromWindow(WriteLZActivity.this ,mainTitle);

                });
            }
        });
    }


    private void addListView() {
        viewList = new ArrayList<>();
        viewList.add(mainTitle);
        viewList.add(mainContent);
    }


    private void initImageAdd() {
        screenWidth = ScreenUtils.getScreenWidth(this);
        imageWidth = (screenWidth - ScreenUtils.dpToPxInt(this, 62)) / 3;
        addImages.add(new ImageAddAdapter.ImageItem());
        imageAddAdapter = new ImageAddAdapter(this, R.layout.item_image_add, addImages);
        imageAddAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (images.isEmpty() || (images.size() < IMAGE_COUNT && position == images.size())) {
                        getImage2(imagePath -> {
                                    presenter.uploadImage(WriteLZActivity.this, imagePath, position, OssFileType.FOUND);

                                }
                        );
                } else {
                    Intent intent = new Intent(WriteLZActivity.this, AlbumItemActivity.class);
                    intent.putExtra(AlbumItemActivity.INTENT_POSITION, position);
                    intent.putExtra(AlbumItemActivity.EXTRA_TYPE_LOCAL, images.get(position));
                    intent.putExtra(AlbumItemActivity.INTENT_ACTION, AlbumItemActivity.ACTION_DELETEABLE);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        imageAddAdapter.setViewWidth(imageWidth);
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


    @OnTextChanged({R.id.main_content,
            R.id.main_title})
    void onTextChange() {
        toggleBtnStatus();
    }

    public void toggleBtnStatus() {
        allValidated = !TextUtils.isEmpty(mainTitle.getText())
                && (!TextUtils.isEmpty(mainContent.getText()) || images.size() > 0) && type != -1;
        btSubmit.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    @OnClick(R.id.bt_submit)
    void publishLostAndFound() {
        if (!allValidated) {
            if (TextUtils.isEmpty(mainTitle.getText())) {
                onError(mainTitle.getHint().toString());
                return;
            }

            if (TextUtils.isEmpty(mainContent.getText()) && images.size() == 0) {
                onError("请先选择图片或者填写文字");
                return;
            }

            if (type == -1){
                onError("请先选择标签");
            }
        }else {

            presenter.publishLostAndFound(mainContent.getText().toString(),
                    images, mainTitle.getText().toString(), type);
        }
    }

    @Override
    public void addImage(String url, int position) {
        Log.d(TAG ,url);
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
        Log.d(TAG ,"click");
        switch (view.getId()) {
            case R.id.iv_first: {
//                getImage(imageUri -> {
//
//                    Glide.with(this).load(imageUri)
//                            .asBitmap()
//                            .placeholder(R.drawable.ic_picture_error)
//                            .error(R.drawable.ic_picture_error)
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivFirst);
//                    ivFirst.setScaleType(ImageView.ScaleType.FIT_XY);
//                    ivSecond.setVisibility(View.VISIBLE);
//                    presenter.uploadImage(WriteLZActivity.this,
//                            imageUri, 0, OssFileType.FOUND);
//                });

                getImage2(imagePath -> {
//                    Glide.with(this).load(imagePath)
//                            .asBitmap()
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivFirst);
//                    ivFirst.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivSecond.setVisibility(View.VISIBLE);
                    presenter.uploadImage(WriteLZActivity.this,
                            imagePath, 0, OssFileType.FOUND);
                });
                break;
            }
            case R.id.iv_second: {
//                getImage(imageUri -> {
//                    Glide.with(this).load(imageUri)
//                            .asBitmap()
//                            .placeholder(R.drawable.ic_picture_error)
//                            .error(R.drawable.ic_picture_error)
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivSecond);
//                    ivSecond.setScaleType(ImageView.ScaleType.FIT_XY);
//                    ivThird.setVisibility(View.VISIBLE);
//                    presenter.uploadImage(WriteLZActivity.this,
//                            imageUri, 1, OssFileType.FOUND);
//                });

                getImage2(imagePath -> {
//                                        Glide.with(this).load(imagePath)
//                            .asBitmap()
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivSecond);
//                    ivSecond.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivThird.setVisibility(View.VISIBLE);
                    presenter.uploadImage(WriteLZActivity.this,
                            imagePath, 1, OssFileType.FOUND);
                });

                break;
            }
            case R.id.iv_third: {
//                getImage(imageUri -> {
//                    Glide.with(this).load(imageUri)
//                            .asBitmap()
//                            .placeholder(R.drawable.ic_picture_error)
//                            .error(R.drawable.ic_picture_error)
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivThird);
//                    ivThird.setScaleType(ImageView.ScaleType.FIT_XY);
//                    presenter.uploadImage(WriteLZActivity.this,
//                            imageUri, 2, OssFileType.FOUND);
//                });

                getImage2(imagePath -> {
//                    Glide.with(this).load(imagePath)
//                            .asBitmap()
//                            .placeholder(R.drawable.ic_picture_error)
//                            .error(R.drawable.ic_picture_error)
//                            .skipMemoryCache(true)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(ivThird);
//                    ivThird.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    presenter.uploadImage(WriteLZActivity.this,
                            imagePath, 2, OssFileType.FOUND);
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
