package com.xiaolian.amigo.ui.ble;

/**
 * 测试BLE
 * <p>
 * Created by caidong on 2017/9/21.
 */

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ble.RxBle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.functions.Consumer;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEActivity extends AppCompatActivity {

    @BindView(R.id.tv_receive)
    TextView sendTv;

    @BindView(R.id.et_send)
    EditText sendEt;

    private String mMsgSend;

    private static final String TAG = BLEActivity.class.getSimpleName();

    private RxBle mRxBle;

    private StringBuffer mStringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        ButterKnife.bind(this);
        mStringBuffer = new StringBuffer();
        mRxBle = RxBle.getInstance().setTargetDevice("Test");
        mRxBle.openBle(this);
        mRxBle.scanBleDevices(true);
        mRxBle.receiveData().subscribe(new Consumer<String>() {
            @Override
            public void accept(String receiveData) {
                sendTv.setText(mStringBuffer.append(receiveData).append("\n"));
            }
        });
        mRxBle.setScanListener(new RxBle.BleScanListener() {
            @Override
            public void onBleScan(BluetoothDevice bleDevice, int rssi, byte[] scanRecord) {
                // Get list of devices and other information
            }
        });
    }

    @OnClick(R.id.btn_send)
    public void sendMessage(View view) {
        if (!TextUtils.isEmpty(mMsgSend)) {
            Log.d(TAG, "sendMessage: " + mMsgSend);
            mRxBle.sendData(mMsgSend, 0);
        }
    }

    @OnTextChanged(R.id.et_send)
    public void onTextChanged(CharSequence text) {
        mMsgSend = text.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxBle.closeBle();
    }
}