package com.xiaolian.amigo.ble;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaolian.amigo.ble.intf.IBLEManager;
import com.xiaolian.amigo.ui.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by caidong on 2017/9/22.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEManager implements IBLEManager {

    private static final String TAG = BLEManager.class.getSimpleName();
    private BaseActivity activity;
    private Handler handler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    private BLEManager(BaseActivity activity) {
        this.activity = activity;

        HandlerThread thread = new HandlerThread("BLEManager");
        thread.start();
        this.handler = new Handler(thread.getLooper());

        check(IBLEManager::open);
    }

    @Override
    public void open() {
        runOn(() -> {
            if (null == bluetoothManager) {
                bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
                if (bluetoothManager == null) {
                    String msg = "初始化bluetoothManager失败";
                    Log.e(TAG, msg);
                    throw new BLEException(msg);
                }
            }

            if (null == bluetoothAdapter) {
                bluetoothAdapter = bluetoothManager.getAdapter();
                if (null == bluetoothAdapter) {
                    String msg = "初始化bluetoothAdapter失败";
                    Log.e(TAG, msg);
                    throw new BLEException(msg);
                }
            }

            if (!bluetoothAdapter.isEnabled() || !bluetoothAdapter.isEnabled()) {
                throw new BLEException("蓝牙打开失败");
            }
        });
    }

    @Override
    public void check(Consumer<IBLEManager> consumer) {
        Log.i(TAG, "确认设备是否已打开蓝牙设备......");
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean allow) throws Exception {
                                   if (allow) {
                                       Log.i(TAG, "打开蓝牙中......！");
                                       consumer.accept(BLEManager.this);
                                   } else {
                                       Log.i(TAG, "设备蓝牙未打开，操作无法继续！");
                                       activity.onBLENotOpen();
                                   }
                               }
                           }
                );
    }

    @Override
    public Observable<BLEDevice> search(long millis) {
        return null;
    }

    // 使用工作线程处理蓝牙连接和数据传输相关任务
    private void runOn(Runnable runnable) {
        handler.post(runnable);
    }
}
