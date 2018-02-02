package com.xiaolian.amigo.ui.ble;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractivePresenter;
import com.xiaolian.amigo.ui.ble.intf.IBleInteractiveView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author caidong
 * @date 17/9/23
 */
public class BleInteractiveActivity extends BleBaseActivity implements IBleInteractiveView {

    private static final String TAG = BleInteractiveActivity.class.getSimpleName();
    String mac;

    @Inject
    IBleInteractivePresenter<IBleInteractiveView> presenter;
    @BindView(R.id.receive_content)
    TextView receiveContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_interactive);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);
        presenter.onConnect(mac);
    }

    @Override
    protected void setUp() {
        Intent intent = getIntent();
        mac = intent.getStringExtra(Constant.MAC);
    }

    @Override
    protected void onDestroy() {
        presenter.onDisConnect();
        super.onDestroy();
    }

    @OnClick(R.id.connect)
    void connect() {
        presenter.onWrite("A70108f778a9a00000000068");
    }

    @Override
    public void onShow(String content) {
        receiveContent.setText(content);
    }

    @Override
    public void onConnectError() {
        Log.e(TAG, "连接设备失败");
    }

    @Override
    public void onWriteError() {
        Log.e(TAG, "写设备失败");
    }

    @Override
    public void onNotifyError() {
        Log.e(TAG, "读设备失败");
    }

    @Override
    public void onStatusError() {
        Log.e(TAG, "设备连接已断开");
    }
}
