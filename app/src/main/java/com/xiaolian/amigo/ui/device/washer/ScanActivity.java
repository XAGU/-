package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.Size;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.widget.qrcode.CustomCaptureManager;

import java.lang.reflect.Field;

import javax.inject.Inject;

/**
 * <p>
 * Created by zcd on 18/1/15.
 */

public class ScanActivity extends WasherBaseActivity
    implements DecoratedBarcodeView.TorchListener, IScanView {
    private static final String TAG = ScanActivity.class.getSimpleName();

    @Inject
    IScanPresenter<IScanView> presenter;

    private CustomCaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean torchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        barcodeScannerView.setOnClickListener(v -> {
            if (torchOn) {
                barcodeScannerView.setTorchOff();
            } else {
                barcodeScannerView.setTorchOn();
            }
        });

//        switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);
//
//        // if the device does not have flashlight in its camera,
//        // then remove the switch flashlight button...
//        if (!hasFlash()) {
//            switchFlashlightButton.setVisibility(View.GONE);
//        }

        capture = new CustomCaptureManager(this, barcodeScannerView);
        capture.setResultCallback((requestCode, resultCode, intent) -> {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.d(TAG, "Cancelled");
//                    Toast.makeText(ScanActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Scanned: " + result.getContents());
                    presenter.scanCheckout(result.getContents());
//                    Toast.makeText(ScanActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

        });
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
    }

    private void setRect() {
        Class decoderThreadClz = DecoderThread.class;
        Class barcodeViewClz = BarcodeView.class;
        try {
            Field decoderThreadField = barcodeViewClz.getDeclaredField("decoderThread");
            decoderThreadField.setAccessible(true);
            DecoderThread decoderThread = (DecoderThread) decoderThreadField.get(barcodeScannerView.getBarcodeView());
            if (decoderThread == null) {
                return;
            }
            Field cropRectField = decoderThreadClz.getDeclaredField("cropRect");
            cropRectField.setAccessible(true);
            Rect rect = (Rect) cropRectField.get(decoderThread);
            if (rect == null) {
                rect = new Rect();
                rect.top = 0;
                rect.left = 0;
                cropRectField.set(decoderThread, rect);
            } else {
                rect.left = 0;
                rect.top = 0;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

//    public void switchFlashlight(View view) {
//        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
//            barcodeScannerView.setTorchOn();
//        } else {
//            barcodeScannerView.setTorchOff();
//        }
//    }

    @Override
    public void onTorchOn() {
        torchOn = true;
//        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        torchOn = false;
//        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }

    @Override
    public void gotoChooseModeView(Bonus bonus, String deviceNo) {
        startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo)
                .putExtra(WasherContent.KEY_BONUS_ID, bonus.getId())
                .putExtra(WasherContent.KEY_BONUS_AMOUNT, bonus.getAmount())
                .putExtra(WasherContent.KEY_BONUS_DESC, bonus.getDescription())
        );
        finish();
    }

    @Override
    public void resumeScan() {
        capture.onResume();
        capture.decode();
//        setRect();
    }
}
