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
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQrCodePresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherQrCodeView;
import com.xiaolian.amigo.ui.main.MainActivity;

import javax.inject.Inject;

/**
 * 展示二维码页面
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherQrCodeActivity extends WasherBaseActivity implements IWasherQrCodeView {

    @Inject
    IWasherQrCodePresenter<IWasherQrCodeView> presenter;
    private TextView tvTopBar;
    private ImageView ivQrCode;
    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer_qrcode);
        getActivityComponent().inject(this);
        presenter.onAttach(WasherQrCodeActivity.this);
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
        tvTopBar = findViewById(R.id.tv_top_bar);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
        ivQrCode = findViewById(R.id.iv_qr_code);
    }

    @Override
    protected void setUp() {
        String mode = getIntent().getStringExtra(WasherContent.KEY_MODE_DESC);
        String price = getIntent().getStringExtra(WasherContent.KEY_PRICE);
        String url = getIntent().getStringExtra(WasherContent.KEY_QR_CODE_URL);
        action = getIntent().getAction();
        renderTopBar(price, mode);
        ivQrCode.post(() ->
                presenter.generateQRCode(url, ivQrCode.getWidth()));
    }

    private void renderTopBar(String price, String mode) {
        tvTopBar.setText(getString(R.string.washer_qr_code_top_bar_tip, price, mode));
    }

    @Override
    public void renderQRCode(Bitmap bitmap) {
        ivQrCode.setImageBitmap(bitmap);
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
