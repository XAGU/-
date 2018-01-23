package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IWasherView;

import javax.inject.Inject;

/**
 * 洗衣机首页
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherActivity extends WasherBaseActivity implements IWasherView {
    private static final String TAG = WasherActivity.class.getSimpleName();
    @Inject
    IWasherPresenter<IWasherView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer);
        bindView();
    }

    private void bindView() {
        LinearLayout ll_start_wash = findViewById(R.id.ll_start_wash);
        ll_start_wash.setOnClickListener(v ->
                scanQRCode());
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    private void scanQRCode() {
//        startActivity(new Intent(this, CaptureActivity.class));
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt(""); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.addExtra(DecodeHintType.CHARACTER_SET.name(), "utf-8");
        integrator.addExtra(DecodeHintType.TRY_HARDER.name(), Boolean.TRUE);
        integrator.addExtra(DecodeHintType.POSSIBLE_FORMATS.name(), BarcodeFormat.QR_CODE);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if(result != null) {
//            if(result.getContents() == null) {
//                Log.d(TAG, "Cancelled");
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//            } else {
//                Log.d(TAG, "Scanned: " + result.getContents());
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//            }
//        }
    }

    @Override
    protected void setUp() {

    }

}
