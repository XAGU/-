package com.xiaolian.amigo.ui.device.geyser;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.wallet.RechargeActivty;
import com.xiaolian.amigo.tmp.component.BezierWaveView;
import com.xiaolian.amigo.tmp.component.DotFlashView;
import com.xiaolian.amigo.tmp.component.dialog.ActionSheetDialog;
import com.xiaolian.amigo.tmp.component.dialog.IOSAlertDialog;
import com.xiaolian.amigo.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热水器设备页
 * @author zcd
 */
public class GeyserActivity extends BaseActivity {

    /**
     * 跳转到选择红包页面的request code
     */
    private static final int CHOOSE_BONUS_CODE = 0x0010;


    /**
     * 使用倒计时
     */
    private CountDownTimer countDownTimer;

    /**
     * 确认支付
     */
    @BindView(R.id.bt_pay)
    Button bt_pay;

    /**
     * 放水进度
     */
    @BindView(R.id.tv_shower_process)
    TextView tv_shower_process;

    /**
     * 正常页面
     */
    @BindView(R.id.ll_content_normal)
    LinearLayout ll_content_normal;

    /**
     * 未连接页面
     */
    @BindView(R.id.ll_content_unconnected)
    LinearLayout ll_content_unconnected;

    /**
     * 开始使用页面
     */
    @BindView(R.id.ll_content_shower)
    LinearLayout ll_content_shower;

    /**
     * 结束洗澡
     */
    @BindView(R.id.bt_stop_shower)
    Button bt_stop_shower;

    /**
     * 重新连接
     */
    @BindView(R.id.bt_reconnect)
    Button bt_reconnect;

    /**
     * 布局头部
     */
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;

    /**
     * 余额支付
     */
    @BindView(R.id.tv_money_pay)
    TextView tv_money_pay;

    /**
     * 红包支付
     */
    @BindView(R.id.tv_bonus_pay)
    TextView tv_bonus_pay;

    /**
     * 预计用水量
     */
    @BindView(R.id.rl_pay_way)
    RelativeLayout rl_pay_way;

    /**
     * 支付方式左侧textview
     */
    @BindView(R.id.tv_water_left)
    TextView tv_water_left;

    /**
     * 支付方式右侧textview
     */
    @BindView(R.id.tv_water_right)
    TextView tv_water_right;

    /**
     * 波浪控件
     */
    @BindView(R.id.bsv_wave)
    BezierWaveView bsv_wave;

    /**
     * 加载进度控件
     */
    @BindView(R.id.dfv_dot)
    DotFlashView dfv_dot;

    /**
     * 设备名称
     */
    @BindView(R.id.tv_device_name)
    TextView tv_device_name;

    /**
     * 预计用水量 选中项
     */
    private int mItemIndex;

    /**
     * 支付方式状态
     */
    private boolean isMoneyPay = true;

    /**
     * 结束洗澡
     */
    @OnClick(R.id.bt_stop_shower)
    void stopShower() {
        endShower();
    }

    /**
     * 点击选择用水量或选择红包
     */
    @OnClick(R.id.rl_pay_way)
    void onPayWayClick() {
        if (isMoneyPay) {
            new ActionSheetDialog(this).builder()
                    .addSheetItem("预付5元／1吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 0;
                            tv_water_right.setText("预付5元／1吨水");
                        }
                    })
                    .addSheetItem("预付10元／2吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 1;
                            tv_water_right.setText("预付10元／2吨水");
                        }
                    }).setTitle("选择水量上限")
                    .setItemGravity(Gravity.LEFT)
                    .setShowCanceleButton(false)
                    .addFooter(R.layout.view_actionsheet_foot)
                    .setSelectItem(mItemIndex).show();
        } else {
            startActivityForResult(new Intent(this, ChooseBonusActivity.class), CHOOSE_BONUS_CODE);
        }
    }

    /**
     * 选择余额支付
     */
    @OnClick(R.id.tv_money_pay)
    void chooseMoneyPay() {
        clearPayTabStatus();
        isMoneyPay = true;
        tv_money_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_red);
        tv_money_pay.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv_money_pay.setTypeface(tv_money_pay.getTypeface(), Typeface.BOLD);
        tv_water_left.setText(getString(R.string.excepted_water));
        tv_water_right.setText("预付5元／1顿水");
    }

    /**
     * 选择红包支付
     */
    @OnClick(R.id.tv_bonus_pay)
    void chooseBonusPay() {
        clearPayTabStatus();
        isMoneyPay = false;
        tv_bonus_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.line_red);
        tv_bonus_pay.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv_bonus_pay.setTypeface(tv_bonus_pay.getTypeface(), Typeface.BOLD);
        tv_water_left.setText(getString(R.string.choose_bonus));
        tv_water_right.setText("2个可用");
    }

    /**
     * 清除状态
     */
    void clearPayTabStatus() {
        tv_money_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_bonus_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_money_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_bonus_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_money_pay.setTypeface(null, Typeface.NORMAL);
        tv_bonus_pay.setTypeface(null, Typeface.NORMAL);
    }

    /**
     * 隐藏底部布局
     */
    void hideBottomLayout() {
        ll_content_normal.setVisibility(View.GONE);
        ll_content_shower.setVisibility(View.GONE);
        ll_content_unconnected.setVisibility(View.GONE);
    }

    /**
     * 确认支付点击事件
     */
    @OnClick(R.id.bt_pay)
    void onOkButtonClick() {
        if (!isMoneyPay) {
            startShower();
        } else {
            new IOSAlertDialog(this).builder()
                    .setMsg("sorry,您的账户余额不足xx元~")
                    .setPositiveButton("前往充值", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), RechargeActivty.class));
                        }
                    })
                    .setNegativeClickListener("取消", new IOSAlertDialog.OnDialogClickListener() {
                        @Override
                        public void onDialogClickListener(IOSAlertDialog iosAlertDialog) {
                            iosAlertDialog.dismiss();
                        }
                    }).show();
        }
    }

    private void startShower() {
        hideBottomLayout();
        ll_content_shower.setVisibility(View.VISIBLE);

        if (countDownTimer != null) {
            countDownTimer.start();
        } else {
            countDownTimer = new CountDownTimer(10 * 1000, 1 * 1000 - 10) {

                @Override
                public void onTick(long time) {
                    tv_shower_process.setText(getString(R.string.shower_process, 10, (time / 1000)));
                }

                @Override
                public void onFinish() {
                    endShower();
                }
            };
            countDownTimer.start();
        }

    }

    private void endShower() {
        hideBottomLayout();
        ll_content_normal.setVisibility(View.VISIBLE);
    }

    /**
     * 重新连接
     */
    @OnClick(R.id.bt_reconnect)
    void onReconnectClick() {
        dfv_dot.startAnimation();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_geyser);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_device_name.setText("3栋－5楼－510");
        if (bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_BONUS_CODE) {

            }
        }
    }

    @Override
    protected void setUp() {

    }
}
