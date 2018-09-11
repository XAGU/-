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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.washer.ScanActivity.KEY_TYPE;

/**
 * 展示二维码页面
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherQrCodeActivity extends WasherBaseActivity implements IWasherQrCodeView {

    @Inject
    IWasherQrCodePresenter<IWasherQrCodeView> presenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tip)
    TextView tip;
    private TextView tvTopBar;
    private ImageView ivQrCode;
    private String action;
    int type;
    private String mode;
    private String url;
    private String price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer_qrcode);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.onAttach(WasherQrCodeActivity.this);
        setUp();
        bindView();

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
        if (type == 4) {
            tvTitle.setText("洗衣机");
            tip.setText("洗衣机使用说明");
        } else {
            tvTitle.setText("烘干机");
            tip.setText("烘干机使用说明");
        }

        renderTopBar(price, mode);
        ivQrCode.post(() ->
                presenter.generateQRCode(url, ivQrCode.getWidth(), this));
    }

    @Override
    protected void setUp() {
        mode = getIntent().getStringExtra(WasherContent.KEY_MODE_DESC);
        price = getIntent().getStringExtra(WasherContent.KEY_PRICE);
        url = getIntent().getStringExtra(WasherContent.KEY_QR_CODE_URL);
        type = getIntent().getIntExtra(KEY_TYPE, 4);
        action = getIntent().getAction();

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
