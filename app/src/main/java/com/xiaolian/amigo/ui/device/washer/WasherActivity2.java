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

import static com.xiaolian.amigo.ui.device.washer.ScanActivity.KEY_TYPE;

/**
 * 烘干机
 *
 * @author zcd
 * @date 18/1/12
 */

public class WasherActivity2 extends WasherBaseActivity implements IWasherView {
    @SuppressWarnings("unused")
    private static final String TAG = WasherActivity2.class.getSimpleName();
    @Inject
    IWasherPresenter<IWasherView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer2);
        bindView();
    }

    private void bindView() {
        LinearLayout llStartWash = findViewById(R.id.ll_start_wash);
        llStartWash.setOnClickListener(v ->
                scanQRCode());
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    private void scanQRCode() {
//        startActivity(new Intent(this, CaptureActivity.class));
        IntentIntegrator integrator = new IntentIntegrator(this);
        //底部的提示文字，设为""可以置空
        integrator.setPrompt("");
        //前置或者后置摄像头
        integrator.setCameraId(0);
        //扫描成功的「哔哔」声，默认开启
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.addExtra(DecodeHintType.CHARACTER_SET.name(), "utf-8");
        integrator.addExtra(DecodeHintType.TRY_HARDER.name(), Boolean.TRUE);
        integrator.addExtra(KEY_TYPE , 6);  //  6 为烘干机
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
