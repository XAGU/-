package com.xiaolian.amigo.tmp.activity.device.waterfountain;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.device.geyser.ChooseBonusActivity;
import com.xiaolian.amigo.tmp.activity.wallet.RechargeActivty;
import com.xiaolian.amigo.tmp.base.BaseActivity;
import com.xiaolian.amigo.tmp.component.BezierWaveView;
import com.xiaolian.amigo.tmp.component.DotFlashView;
import com.xiaolian.amigo.tmp.component.dialog.ActionSheetDialog;
import com.xiaolian.amigo.tmp.component.dialog.IOSAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 饮水机页面
 * @author zcd
 */
public class WaterFountainActivity extends BaseActivity {

    /**
     * 跳转到选择红包的request code
     */
    private static final int CHOOSE_BONUS_CODE = 0x0011;
    @BindView(R.id.rl_header)
    RelativeLayout rl_header;

    /**
     * 波浪控件
     */
    @BindView(R.id.bsv_wave)
    BezierWaveView bsv_wave;

    /**
     * 设备名称
     */
    @BindView(R.id.tv_device_name)
    TextView tv_device_name;

    /**
     * 收藏
     */
    @BindView(R.id.iv_collect)
    ImageView iv_collect;

    /**
     * 选择水温
     */
    @BindView(R.id.rl_water_temp)
    RelativeLayout rl_water_temp;

    /**
     * 选择水温
     */
    @OnClick(R.id.rl_water_temp)
    void chooseWaterTemp() {

    }

    /**
     * 加载进度控件
     */
    @BindView(R.id.dfv_dot)
    DotFlashView dfv_dot;

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
     * 支付方式左侧textview1
     */
    @BindView(R.id.tv_water_left1)
    TextView tv_water_left1;

    /**
     * 支付方式右侧textview1
     */
    @BindView(R.id.tv_water_right1)
    TextView tv_water_right1;

    /**
     * 支付方式左侧textview2
     */
    @BindView(R.id.tv_water_left2)
    TextView tv_water_left2;

    /**
     * 支付方式右侧textview2
     */
    @BindView(R.id.tv_water_right2)
    TextView tv_water_right2;
    /**
     * 支付方式状态
     */
    private boolean isMoneyPay = true;

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
        tv_water_left1.setText("请选择水温:");
        tv_water_right1.setText("冰水");
        tv_water_left2.setText("预计用水量:");
        tv_water_right2.setText("预付2元／1升");
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
        tv_water_left1.setText("选择水温:");
        tv_water_right1.setText("冰水");
        tv_water_left2.setText("选择红包:");
        tv_water_right2.setText("2个可用");
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
     * 选中的item
     */
    private int mItemIndex = 0;

    @OnClick(R.id.rl_water_amount)
    void onWaterAmountClick() {
        if (isMoneyPay) {
            new ActionSheetDialog(this).builder()
                    .addSheetItem("预付5元／x吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 0;
                        }
                    })
                    .addSheetItem("预付5元／x吨水", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            mItemIndex = 1;
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
     * 确认支付点击事件
     */
    @OnClick(R.id.bt_pay)
    void onOkButtonClick() {
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
    }

    /**
     * 重新连接
     */
    @OnClick(R.id.bt_reconnect)
    void onReconnectClick() {
        dfv_dot.startAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_water_fountain);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_device_name.setText("XX楼X层具体位置");
        iv_collect.setVisibility(View.VISIBLE);
        iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_collect.setImageResource(R.drawable.collected);
            }
        });
        if (bsv_wave != null && !bsv_wave.isRunning()) {
            bsv_wave.startAnim();
        }
    }

}
