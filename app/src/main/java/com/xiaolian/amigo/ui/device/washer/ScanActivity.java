package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.journeyapps.barcodescanner.camera.CameraThread;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.widget.qrcode.CustomCaptureManager;
import com.xiaolian.amigo.util.ScreenUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        findViewById(R.id.iv_flashlight).setOnClickListener(v -> {
            if (torchOn) {
                barcodeScannerView.setTorchOff();
            } else {
                barcodeScannerView.setTorchOn();
            }
        });
    }

    @SuppressWarnings("unused")
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
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setExposureEnabled(true);
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setBarcodeSceneModeEnabled(true);
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setMeteringEnabled(true);
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setFocusMode(CameraSettings.FocusMode.MACRO);
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setScanInverted(true);
//        barcodeScannerView.getBarcodeView().getCameraInstance().configureCamera();
//        barcodeScannerView.getBarcodeView().getCameraInstance().getCameraSettings().setContinuousFocusEnabled(true);
        barcodeScannerView.post(this::configCamera);
    }

    private void configCamera() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        int viewWidth = barcodeScannerView.getWidth();
        int viewHeight = barcodeScannerView.getHeight();
        Rect focusRect = calculateTapArea(screenWidth/2, screenHeight/2, 1f, viewWidth, viewHeight);
        Rect meteringRect = calculateTapArea(screenWidth/2, screenHeight/2, 1.5f, viewWidth, viewHeight);

        Camera camera = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraManager().getCamera();
        CameraThread cameraThread = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraThread();
        cameraThread.enqueue(() -> {
            if (camera == null) {
                return;
            }
            camera.cancelAutoFocus();
            Camera.Parameters params = camera.getParameters();

            if (params.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<>();
                meteringAreas.add(new Camera.Area(meteringRect, 800));
                params.setMeteringAreas(meteringAreas);
            } else {
                Log.i(TAG, "metering areas not supported");
            }

            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(focusRect, 800));
                params.setFocusAreas(focusAreas);
            } else {
                Log.i(TAG, "focus areas not supported");
            }
            final String currentFocusMode = params.getFocusMode();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
            camera.setParameters(params);

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                }
            });
        });
    }

    private void handleFocus(MotionEvent event) {
        int viewWidth = barcodeScannerView.getWidth();
        int viewHeight = barcodeScannerView.getHeight();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, viewWidth, viewHeight);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, viewWidth, viewHeight);

        Camera camera = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraManager().getCamera();
        CameraThread cameraThread = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraThread();
        cameraThread.enqueue(() -> {
            if (camera == null) {
                return;
            }
            camera.cancelAutoFocus();
            Camera.Parameters params = camera.getParameters();

            if (params.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<>();
                meteringAreas.add(new Camera.Area(meteringRect, 800));
                params.setMeteringAreas(meteringAreas);
            } else {
                Log.i(TAG, "metering areas not supported");
            }

            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(focusRect, 800));
                params.setFocusAreas(focusAreas);
            } else {
                Log.i(TAG, "focus areas not supported");
            }
            final String currentFocusMode = params.getFocusMode();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
            camera.setParameters(params);

            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                }
            });
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
//            handleFocus(event);
        }
        return true;
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(clamp(centerX - halfAreaSize, -1000, 1000)
                , clamp(centerY - halfAreaSize, -1000, 1000)
                , clamp(centerX + halfAreaSize, -1000, 1000)
                , clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
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
    @SuppressWarnings("unused")
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
        if (bonus == null) {
            startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                    .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo)
            );
        } else {
            startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                    .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo)
                    .putExtra(WasherContent.KEY_BONUS_ID, bonus.getId())
                    .putExtra(WasherContent.KEY_BONUS_AMOUNT, bonus.getAmount())
                    .putExtra(WasherContent.KEY_BONUS_DESC, bonus.getDescription())
            );
        }
        finish();
    }

    @Override
    public void resumeScan() {
        capture.onResume();
        capture.decode();
//        setRect();
    }
}
