package com.xiaolian.amigo.ui.lostandfound;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IPublishLostView;
import com.xiaolian.amigo.ui.widget.dialog.DatePickerDialog;
import com.xiaolian.amigo.ui.widget.wheelpicker.WheelDateTimePicker;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 失物发布
 * @author zcd
 */

public class PublishLostActivity extends LostAndFoundBaseActivity implements IPublishLostView {

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
    private boolean allValidated = false;
    private List<TextView> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_lost);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(PublishLostActivity.this);

        viewList = new ArrayList<TextView>(){
            {
                add(et_title);
                add(et_itemName);
                add(et_location);
                add(tv_lostTime);
                add(et_mobile);
                add(et_desc);
            }
        };

        CommonUtil.showSoftInput(this, et_title);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @OnTextChanged({R.id.et_desc, R.id.et_itemName, R.id.et_mobile, R.id.et_location,
        R.id.et_title, R.id.tv_lostTime})
    void onTextChange() {
        toggleBtnStatus();
    }

    @Override
    public void toggleBtnStatus() {
        allValidated = !TextUtils.isEmpty(et_title.getText())
                && !TextUtils.isEmpty(et_itemName.getText())
                && !TextUtils.isEmpty(et_location.getText())
                && !TextUtils.isEmpty(tv_lostTime.getText())
                && !TextUtils.isEmpty(et_mobile.getText())
                && !TextUtils.isEmpty(et_desc.getText());
        bt_submit.setBackgroundResource(allValidated ?
                R.drawable.button_enable : R.drawable.button_disable);
    }

    @OnClick(R.id.bt_submit)
    void publishLost() {
        if (!allValidated) {
            for (TextView view : viewList) {
                if (TextUtils.isEmpty(view.getText())) {
                    onError(view.getHint().toString());
                    return;
                }
            }
        }
        presenter.publishLostAndFound(et_desc.getText().toString(),
                images, et_itemName.getText().toString(),et_location.getText().toString(),
                (Long) tv_lostTime.getTag(R.id.timestamp), et_mobile.getText().toString(),
                et_title.getText().toString(), 1);
    }

    @Override
    public void addImage(String url, int position) {
        if (this.images.size() > position) {
            this.images.add(position, url);
        } else {
            this.images.add(url);
        }
    }

    @OnClick(R.id.ll_time)
    void onTimeChoose() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnItemSelectedListener(new DatePickerDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelDateTimePicker picker, Date date) {
                tv_lostTime.setTag(R.id.timestamp, TimeUtils.date2Millis(date));
                tv_lostTime.setText(TimeUtils.date2String(date, TimeUtils.MY_DATE_TIME_FORMAT));
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
                            .asBitmap()
                            .placeholder(R.drawable.ic_picture_error)
                            .error(R.drawable.ic_picture_error)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(iv_first);
                    iv_first.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_second.setVisibility(View.VISIBLE);
                    presenter.uploadImage(PublishLostActivity.this,
                            imageUri, 0, OssFileType.LOST);
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
                            .into(iv_second);
                    iv_second.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv_third.setVisibility(View.VISIBLE);
                    presenter.uploadImage(PublishLostActivity.this,
                            imageUri, 1, OssFileType.LOST);
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
                            .into(iv_third);
                    iv_third.setScaleType(ImageView.ScaleType.FIT_XY);
                    presenter.uploadImage(PublishLostActivity.this,
                            imageUri, 2, OssFileType.LOST);
                });
                break;
            }
        }
    }
}
