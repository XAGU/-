package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQRCodePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQRCodeView;
import com.xiaolian.amigo.ui.main.MainActivity;

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
    private ImageView iv_qr_code;
    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer_qrcode);
        getActivityComponent().inject(this);
        presenter.onAttach(WasherQRCodeActivity.this);
        bindView();
        setUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void bindView() {
        tv_top_bar = findViewById(R.id.tv_top_bar);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
        iv_qr_code = findViewById(R.id.iv_qr_code);
    }

    @Override
    protected void setUp() {
        String mode = getIntent().getStringExtra(WasherContent.KEY_MODE_DESC);
        String price = getIntent().getStringExtra(WasherContent.KEY_PRICE);
        String url = getIntent().getStringExtra(WasherContent.KEY_QR_CODE_URL);
        action = getIntent().getAction();
        renderTopBar(price, mode);
        iv_qr_code.post(() ->
                presenter.generateQRCode(url, iv_qr_code.getWidth()));
    }

    private void renderTopBar(String price, String mode) {
        tv_top_bar.setText(getString(R.string.washer_qr_code_top_bar_tip, price, mode));
    }

    @Override
    public void renderQRCode(Bitmap bitmap) {
        iv_qr_code.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(action)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
