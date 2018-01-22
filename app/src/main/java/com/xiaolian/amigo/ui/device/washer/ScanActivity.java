package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.widget.qrcode.CustomCaptureManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

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
                    Toast.makeText(ScanActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Scanned: " + result.getContents());
                    presenter.scanCheckout(result.getContents());
                    Toast.makeText(ScanActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

        });
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
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
//        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
//        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }

    @Override
    public void gotoChooseModeView(String deviceNo) {
        startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo));
        finish();
    }

    @Override
    public void resumeScan() {
        capture.onResume();
        capture.decode();
    }
}
