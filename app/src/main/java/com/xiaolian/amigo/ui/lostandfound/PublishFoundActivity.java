package com.xiaolian.amigo.ui.lostandfound;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.ui.widget.dialog.DatePickerDialog;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 招领发布
 * @author zcd
 */

public class PublishFoundActivity extends LostAndFoundBaseActivity implements IPublishLostView {
    @Inject
    IPublishLostPresenter<IPublishLostView> presenter;

    /**
     * 提交
     */
    @BindView(R.id.bt_submit)
    Button bt_submit;

    /**
     * 标题
     */
    @BindView(R.id.et_title)
    EditText et_title;

    /**
     * 物品名称
     */
    @BindView(R.id.et_itemName)
    EditText et_itemName;

    /**
     * 地址
     */
    @BindView(R.id.et_location)
    EditText et_location;

    /**
     * 时间
     */
    @BindView(R.id.tv_lostTime)
    TextView tv_lostTime;

    /**
     * 联系方式
     */
    @BindView(R.id.et_mobile)
    EditText et_mobile;

    /**
     * 描述
     */
    @BindView(R.id.et_desc)
    EditText et_desc;

    @BindView(R.id.iv_first)
    ImageView iv_first;
    @BindView(R.id.iv_second)
    ImageView iv_second;
    @BindView(R.id.iv_third)
    ImageView iv_third;

    List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_found);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(PublishFoundActivity.this);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        finish();
    }


    @OnTextChanged({R.id.et_desc, R.id.et_itemName, R.id.et_mobile, R.id.et_location,
            R.id.et_title, R.id.tv_lostTime})
    void onTextChange() {
        toggleBtnStatus();
    }

    @Override
    public void toggleBtnStatus() {
        bt_submit.setEnabled(!TextUtils.isEmpty(et_title.getText())
                && !TextUtils.isEmpty(et_itemName.getText())
                && !TextUtils.isEmpty(et_location.getText())
                && !TextUtils.isEmpty(tv_lostTime.getText())
                && !TextUtils.isEmpty(et_mobile.getText())
                && !TextUtils.isEmpty(et_desc.getText()));
    }

    @OnClick(R.id.bt_submit)
    void publishFound() {
        presenter.publishLostAndFound(et_desc.getText().toString(),
                images, et_itemName.getText().toString(),et_location.getText().toString(),
                tv_lostTime.getText().toString(), et_mobile.getText().toString(),
                et_title.getText().toString(), 2);
    }

    @Override
    public void addImage(String url) {
        this.images.add(Constant.IMAGE_PREFIX + url);
    }

    @OnClick(R.id.ll_time)
    void onTimeChoose() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnItemSelectedListener(new DatePickerDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, String data, int position, DatePickerDialog.WheelType type) {
                tv_lostTime.setText(System.currentTimeMillis() + "");
            }
        });
        datePickerDialog.show();
    }

    @OnClick({R.id.iv_first, R.id.iv_second, R.id.iv_third})
    void chooseImage(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_first: {
                getImage(imageUri -> {

                    Glide.with(this).load(imageUri)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_first);
                    iv_first.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_second.setVisibility(View.VISIBLE);
                    presenter.uploadImage(imageUri);
                });
                break;
            }
            case R.id.iv_second: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_second);
                    iv_second.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_third.setVisibility(View.VISIBLE);
                    presenter.uploadImage(imageUri);
                });
                break;
            }
            case R.id.iv_third: {
                getImage(imageUri -> {
                    Glide.with(this).load(imageUri)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_third);
                    iv_third.setScaleType(ImageView.ScaleType.FIT_XY);
                    presenter.uploadImage(imageUri);
                });
                break;
            }
        }
    }
}
