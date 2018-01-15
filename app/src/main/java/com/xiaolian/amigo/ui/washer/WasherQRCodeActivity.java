package com.xiaolian.amigo.ui.washer;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.washer.intf.IWasherQRCodePresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherQRCodeView;

import javax.inject.Inject;

/**
 * 展示二维码页面
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherQRCodeActivity extends WasherBaseActivity implements IWasherQRCodeView {

    @Inject
    IWasherQRCodePresenter<IWasherQRCodeView> presenter;
    private TextView tv_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer_qrcode);
        getActivityComponent().inject(this);
        presenter.onAttach(WasherQRCodeActivity.this);
        bindView();
        setUp();
    }

    private void bindView() {
        tv_top_bar = findViewById(R.id.tv_top_bar);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    @Override
    protected void setUp() {
        String mode = getIntent().getStringExtra(WasherContent.INTENT_KEY_MODE);
        String price = getIntent().getStringExtra(WasherContent.INTENT_KEY_PRICE);
        renderTopBar(price, mode);
        presenter.generateQRCode(price, mode);
    }

    private void renderTopBar(String price, String mode) {
        tv_top_bar.setText(getString(R.string.washer_qr_code_top_bar_tip, price, mode));
    }
}
