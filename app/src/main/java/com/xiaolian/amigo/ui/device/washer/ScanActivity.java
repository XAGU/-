package com.xiaolian.amigo.ui.device.washer;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoderThread;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraThread;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserCategory;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.device.BriefDeviceDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.dryer.DryerActivity;
import com.xiaolian.amigo.ui.device.washer.intf.IScanPresenter;
import com.xiaolian.amigo.ui.device.washer.intf.IScanView;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.widget.ScanDialog;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.ui.widget.qrcode.CustomCaptureManager;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.RxHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static com.xiaolian.amigo.data.enumeration.Device.HEATER;
import static com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity.CONN_TYPE;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_DEVICE_TYPE;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_LOCATION;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_MAC_ADDRESS;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_RESIDENCE_ID;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_SUPPLIER_ID;

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

    public static final String SCAN_TYPE= "SCAN_TYPE"  ;   // 扫描类型

    public static final String IS_SACN = "IS_SCAN" ; // 是否是扫描


    private OrderPreInfoDTO orderPreInfo ;
    @Inject
    IScanPresenter<IScanView> presenter;
    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView zxingBarcodeScanner;
    @BindView(R.id.zxing_status_view)
    TextView zxingStatusView;
    @BindView(R.id.iv_flashlight)
    ImageView ivFlashlight;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private CustomCaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean torchOn = false;


    /*****  扫一扫中处理， 留做以后代码重构提取出来构造子类*****/
    private int scanType ;
    private boolean scan  = false ;
    private AvailabilityDialog availabilityDialog;

    private int heaterOrderSize;

    private ScanDialog scanDialog ;

    /*****      ***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        setUp();
        init();
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
                        if (scan){
                            handleScanContent(result.getContents());
                        }else {
                            presenter.scanCheckout(result.getContents());
                        }
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

    /**
     * 处理扫一扫进入的扫描内容 ； 内容格式为 （type , mac , siBle）
     */
    private void handleScanContent(String scanContent){
        String[] contents = scanContent.split(",");
        int type = -1 ;
        String mac = "";
        boolean isBle = true ;
        if (contents.length == 3) {
            try {
                mac = contents[1];
                isBle = "1".equals(contents[2]) ? true : false;
                type = Integer.parseInt(contents[0]);
                presenter.checkDeviceUseage(type, mac, isBle);
                capture.isCanPause = true ;
            } catch (Exception e) {
                Log.d(TAG, "scanContent: " + scanContent);
                capture.isCanPause = false ;
                resumeScan();
            }
        }else{
//            RxHelper.delay(1, new Action1<Long>() {
//                @Override
//                public void call(Long aLong) {
//                    resumeScan();
//                }
//            });

            capture.isCanPause = false ;
            resumeScan();
        }

    }

    private void init(){
        if (zxingBarcodeScanner != null) zxingBarcodeScanner.setType(scanType);
        if (scanType == 1){
            tvTitle.setText("洗衣机扫描");
        }else{
            tvTitle.setText("扫一扫");
        }
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
        if (getIntent() != null){
            scanType = getIntent().getIntExtra(SCAN_TYPE , 1);
            scan = getIntent().getBooleanExtra(IS_SACN , false);
        }
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



    /****     扫一扫中扫描设备地址的处理  ，留做以后代码重构后做成父类处理   *****/
    @Override
    public void showDeviceUsageDialog(int type, DeviceCheckRespDTO data ,String mac ,boolean isBle) {
            Log.d(TAG, "showDeviceUsageDialog: " + type);
            if (data == null || data.getBalance() == null
                    || data.getPrepay() == null || data.getMinPrepay() == null
                    || data.getTimeValid() == null) {
                onError("服务器飞走啦，努力修复中");
                return;
            }
            if (orderPreInfo == null) {
                orderPreInfo = new OrderPreInfoDTO();
            }
            orderPreInfo.setBalance(data.getBalance());
            orderPreInfo.setBonus(data.getBonus());
            orderPreInfo.setCsMobile(data.getCsMobile());
            orderPreInfo.setMinPrepay(data.getMinPrepay());
            orderPreInfo.setPrepay(data.getPrepay());
            orderPreInfo.setPrice(data.getPrice());
            // 2小时内存在未找零订单，弹窗提示需要结账
            if (data.getExistsUnsettledOrder() != null && data.getExistsUnsettledOrder()) {
                    showScanDialog(type ,data ,orderPreInfo);
            } else {

                // 调用one
                        presenter.getDeviceDetail(data.getTimeValid(),type , mac , isBle);
                    // 如果热水澡 检查默认宿舍

            }
    }


    @Override
    public void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data) {
        com.xiaolian.amigo.util.Log.d(TAG, "showTimeValidDialog: " + data.getTitle() + "->" + data.getRemark() + "->" + deviceType);
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            if (availabilityDialog.getType() == AvailabilityDialog.Type.TIME_VALID) {
                return;
            } else {
                availabilityDialog.dismiss();
            }
        }
        availabilityDialog.setType(AvailabilityDialog.Type.TIME_VALID);
        availabilityDialog.setOkText(getString(R.string.keep_use_cold_water));
        availabilityDialog.setTitle(data.getTitle());
        availabilityDialog.setTip(data.getRemark());
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            if (deviceType == Device.HEATER.getType()) {
                presenter.gotoHeaterDevice(data.getDefaultMacAddress(),
                        data.getDefaultSupplierId(), data.getLocation(),data.getResidenceId()
                         );
            } else if (deviceType == Device.DISPENSER.getType()) {
                gotoDispenser(data.getUnsettledMacAddress(), data.getUnsettledSupplierId(),
                        data.getLocation(), data.getResidenceId(),
                        data.getFavor(), (data.getCategory() != null
                                && DispenserCategory.MULTI.getType() == data.getCategory())
                                ? Integer.valueOf(DispenserWater.ALL.getType()) : data.getUsefor(), true);
            } else if (deviceType == Device.DRYER.getType()) {
                gotoDryer(data.getUnsettledMacAddress(), data.getUnsettledSupplierId(),
                        data.getLocation(), data.getResidenceId(),
                        data.getFavor(), true);
            }
        });
        availabilityDialog.show();
    }

    @Override
    public void showNoDeviceDialog() {

    }

    @Override
    public void showScanDialog(int type , DeviceCheckRespDTO data , OrderPreInfoDTO orderPreInfoDTO) {
        if (scanDialog == null) {
            scanDialog = new ScanDialog(this);
        }
        if (scanDialog.isShowing()) {
            return;
        }

        scanDialog.setOnCancelClickListener(new ScanDialog.OnCancelClickListener() {
            @Override
            public void onCancelClick(Dialog dialog) {
                if (data.getTimeValid()) {
                    // 1 表示热水澡 2 表示饮水机
                    if (type == Device.HEATER.getType()) {
                        // 直接前往热水澡处理找零
                        gotoDevice(HEATER, data.getUnsettledMacAddress(),
                                data.getUnsettledSupplierId(), data.getLocation(),
                                data.getResidenceId(), true);
                    } else if (type == Device.DISPENSER.getType()) {
                        gotoDispenser(data.getUnsettledMacAddress(), data.getUnsettledSupplierId(),
                                data.getLocation(), data.getResidenceId(),
                                data.getFavor(), (data.getCategory() != null
                                        && DispenserCategory.MULTI.getType() == data.getCategory())
                                        ? Integer.valueOf(DispenserWater.ALL.getType()) : data.getUsefor(), true);
                    } else if (type == Device.DRYER.getType()) {
                        gotoDryer(data.getUnsettledMacAddress(), data.getUnsettledSupplierId(),
                                data.getLocation(), data.getResidenceId(),
                                data.getFavor(), true);
                    }
                }else{
                    showTimeValidDialog(type, data);
                }
                dialog.dismiss();
            }
        });

        scanDialog.setOnOkClickListener(new ScanDialog.OnOkClickListener() {
            @Override
            public void onOkClick(Dialog dialog) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void goToBleDevice(boolean isTimeValid, int type, String macAddress, BriefDeviceDTO data, boolean isBle) {
        if (type == Device.HEATER.getType()) {

                // 前往热水澡
                gotoDevice(HEATER, macAddress,
                        data.getSupplierId(), data.getLocation(),
                        data.getResidenceId(), false);
        } else if (type == Device.DISPENSER.getType()) {
            // 进入饮水机

            gotoDispenser(macAddress ,data.getSupplierId() ,data.getLocation() ,
                    data.getResidenceId() ,data.isFavor() ,0 ,false);
//
        } else if (type == Device.DRYER.getType()) {
            // 进入吹风机

            gotoDryer(macAddress , data.getSupplierId() , data.getLocation() ,
                    data.getResidenceId(), data.isFavor()  , false);

        }
    }

    public void gotoDevice(Device device, String macAddress, Long supplierId,
                           String location, Long residenceId, boolean recovery) {
        com.xiaolian.amigo.util.Log.d(TAG, "gotoDevice: " + device.getDesc() + "->" + macAddress + "->" + supplierId + "->" + location + "->" + residenceId);
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, device.getClz());
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_SUPPLIER_ID, supplierId);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, device.getType());
            intent.putExtra(INTENT_KEY_RESIDENCE_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }


    public void gotoDevice(Device device, String macAddress, Long supplierId,
                           String location, boolean recovery) {
//        com.xiaolian.amigo.util.Log.d(TAG, "gotoDevice: " + device.getDesc() + "->" + macAddress + "->" + supplierId + "->" + location + "->" + residenceId);
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, device.getClz());
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_SUPPLIER_ID, supplierId);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, device.getType());
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }


    public void gotoDryer(String macAddress, Long supplierId, String location, Long residenceId,
                          boolean favor, boolean recovery) {
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, DryerActivity.class);
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_SUPPLIER_ID, supplierId);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, Device.DRYER.getType());
            intent.putExtra(DispenserActivity.INTENT_KEY_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(DispenserActivity.INTENT_KEY_FAVOR, favor);
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }

    public void gotoDispenser(String macAddress, Long supplierId, String location, Long residenceId,
                              boolean favor, int usefor,
                              boolean recovery) {
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, DispenserActivity.class);
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_SUPPLIER_ID, supplierId);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType());
            intent.putExtra(DispenserActivity.INTENT_KEY_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(DispenserActivity.INTENT_KEY_FAVOR, favor);
            intent.putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, String.valueOf(usefor));
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }


    @Override
    public void showBindDormitoryDialog() {
        com.xiaolian.amigo.util.Log.d(TAG, "showBindDormitoryDialog");
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()
                && availabilityDialog.getType() == AvailabilityDialog.Type.BIND_DORMITORY) {
            return;
        }
        availabilityDialog.setType(AvailabilityDialog.Type.BIND_DORMITORY);
        availabilityDialog.setOkText("前往绑定");
        availabilityDialog.setTitle(AvailabilityDialog.Type.BIND_DORMITORY.getTitle());
        availabilityDialog.setTip("热水澡需要先绑定宿舍");
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            Intent intent;
            intent = new Intent(this, ListChooseActivity.class);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_BUILDING);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.HEATER_TO_BATHROOM);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
            startActivity(intent);
        });
        availabilityDialog.show();
    }
}

