package com.xiaolian.amigo.ui.device.washer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraThread;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.widget.qrcode.CustomCaptureManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 二维码扫描页面
 *
 * @author zcd
 * @date 18/1/15
 */

public class ScanActivity extends WasherBaseActivity
        implements DecoratedBarcodeView.TorchListener, IScanView {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int FRAMING_SIZE_DIVISOR = 4;

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

        capture = new CustomCaptureManager(this, barcodeScannerView);
        capture.setResultCallback(new CustomCaptureManager.ResultCallback() {
            @Override
            public void callback(int requestCode, int resultCode, Intent intent) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (result != null) {
                    if (result.getContents() == null) {
                        Log.d(TAG, "Cancelled");
                    } else {
                        Log.d(TAG, "Scanned: " + result.getContents());
                        presenter.scanCheckout(result.getContents());
                    }
                }
            }

            @Override
            public void possibleCallback(List<ResultPoint> resultPoint) {
                try {
                    int distance = getMaxDistanceOfPoints(resultPoint);
                    if (distance != 0 && distance < barcodeScannerView.getBarcodeView().getFramingRectSize().width / FRAMING_SIZE_DIVISOR) {
                        Log.d("Point", "should zoom" + "distance: " + distance);
                        zoomCamera();
                    }
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }
        });
        capture.initializeFromIntent(getIntent(), savedInstanceState);

        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        setFlashlight();

        barcodeScannerView.getBarcodeView().addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
            }

            @Override
            public void previewStarted() {
                try {
                    configCamera();
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }

            @Override
            public void previewStopped() {
            }

            @Override
            public void cameraError(Exception error) {
            }

            @Override
            public void cameraClosed() {
            }
        });
    }

    private void setFlashlight() {
        findViewById(R.id.iv_flashlight).setOnClickListener(v -> {
            if (torchOn) {
                barcodeScannerView.setTorchOff();
            } else {
                barcodeScannerView.setTorchOn();
            }
        });
//        // if the device does not have flashlight in its camera,
//        // then remove the switch flashlight button...
        if (!hasFlash()) {
            findViewById(R.id.iv_flashlight).setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    private void zoomCamera() {
        if (barcodeScannerView.getBarcodeView().getCameraInstance() == null) {
            return;
        }
        CameraThread cameraThread = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraThread();
        cameraThread.enqueue(() -> {
            Camera camera = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraManager().getCamera();
            Camera.Parameters parameters = camera.getParameters();
            final int maxZoom = parameters.getMaxZoom();
            Log.d("Point", "maxZoom: " + maxZoom);
            int zoom = parameters.getZoom();
            if (parameters.isZoomSupported()) {
                if (zoom == 0) {
                    zoom = maxZoom / 3;
                } else {
//                    zoom = zoom + 5;
                    zoom = zoom + maxZoom / 27;
                }
                if (zoom > maxZoom) {
                    zoom = maxZoom;
                }
                parameters.setZoom(zoom);
                camera.setParameters(parameters);
            }

        });
    }

    private int getMaxDistanceOfPoints(List<ResultPoint> resultPoint) {
        if (resultPoint == null) {
            return 0;
        }
        int max = 0;
        for (int i = 0; i < resultPoint.size() - 1; i++) {
            for (int j = i + 1; j < resultPoint.size(); j++) {
                int distance = (int) Math.hypot(resultPoint.get(i).getX() - resultPoint.get(j).getX(), resultPoint.get(i).getY() - resultPoint.get(j).getY());
                if (distance > max) {
                    max = distance;
                }
            }
        }
        return max;
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
        capture.decode();
    }

    @SuppressWarnings("deprecation")
    private void configCamera() {
        if (barcodeScannerView.getBarcodeView().getCameraInstance() == null) {
            return;
        }

        Camera camera = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraManager().getCamera();
        CameraThread cameraThread = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraThread();
        cameraThread.enqueue(() -> {
            if (camera == null) {
                return;
            }
            Camera.Parameters params = camera.getParameters();

            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                // 在图像的中心指定一个区域
                Rect areaRect1 = new Rect(-150, -150, 150, 150);
                focusAreas.add(new Camera.Area(areaRect1, 800));
                params.setFocusAreas(focusAreas);
            } else {
                Log.i(TAG, "focus areas not supported");
            }

            // 检查是否支持测光区域
            if (params.getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> meteringAreas = new ArrayList<>();
                // 在图像的中心指定一个区域
                Rect areaRect1 = new Rect(-150, -150, 150, 150);
                meteringAreas.add(new Camera.Area(areaRect1, 800));
                params.setMeteringAreas(meteringAreas);
            }

            camera.setParameters(params);
        });
    }

    @SuppressWarnings("deprecation")
    private void handleFocus(MotionEvent event) {
        if (barcodeScannerView.getBarcodeView().getCameraInstance() == null) {
            return;
        }

        Camera camera = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraManager().getCamera();
        CameraThread cameraThread = barcodeScannerView.getBarcodeView().getCameraInstance().getCameraThread();

        int viewWidth = barcodeScannerView.getWidth();
        int viewHeight = barcodeScannerView.getHeight();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, viewWidth, viewHeight);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, viewWidth, viewHeight);

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

            camera.autoFocus((success, camera1) -> {
                Camera.Parameters params1 = camera1.getParameters();
                params1.setFocusMode(currentFocusMode);
                camera1.setParameters(params1);
            });
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            handleFocus(event);
        }
        return true;
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(MathUtils.clamp(centerX - halfAreaSize, -1000, 1000)
                , MathUtils.clamp(centerY - halfAreaSize, -1000, 1000)
                , MathUtils.clamp(centerX + halfAreaSize, -1000, 1000)
                , MathUtils.clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
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
     *
     * @return true if there is Flashlight, otherwise false.
     */
    @SuppressWarnings("unused")
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        torchOn = true;
    }

    @Override
    public void onTorchOff() {
        torchOn = false;
    }

    @Override
    public void gotoChooseModeView(Bonus bonus, Double balance, String deviceNo) {
        if (bonus == null) {
            startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                    .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo)
                    .putExtra(WasherContent.KEY_BALANCE, balance)
            );
        } else {
            startActivity(new Intent(ScanActivity.this, ChooseWashModeActivity.class)
                    .putExtra(WasherContent.KEY_DEVICE_NO, deviceNo)
                    .putExtra(WasherContent.KEY_BONUS_ID, bonus.getId())
                    .putExtra(WasherContent.KEY_BONUS_AMOUNT, bonus.getAmount())
                    .putExtra(WasherContent.KEY_BONUS_DESC, bonus.getDescription())
                    .putExtra(WasherContent.KEY_BALANCE, balance)
            );
        }
        finish();
    }

    @Override
    public void resumeScan() {
        capture.onResume();
        capture.decode();
    }
}
