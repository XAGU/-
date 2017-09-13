package com.xiaolian.amigo.activity.device.geyser;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;
import com.xiaolian.amigo.component.dialog.ActionSheetDialog;
import com.xiaolian.amigo.component.dialog.IOSAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热水器设备页
 * @author zcd
 */

public class GeyserActivity extends BaseActivity {


    private static final int CHOOSE_BONUS_CODE = 0x0010;
    /**
     * 确认支付
     */
    @BindView(R.id.bt_pay)
    Button bt_pay;

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
    @BindView(R.id.ll_header)
    LinearLayout ll_header;

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

    @OnClick(R.id.rl_pay_way)
    void onPayWayClick() {
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
        tv_water_right.setText("预付10元／1顿水");
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
        tv_water_left.setText(getString(R.string.excepted_water));
        tv_water_right.setText("2个可用");
    }

    void clearPayTabStatus() {
        tv_money_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_bonus_pay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_money_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_bonus_pay.setTextColor(ContextCompat.getColor(this, R.color.colorTextGray));
        tv_money_pay.setTypeface(null, Typeface.NORMAL);
        tv_bonus_pay.setTypeface(null, Typeface.NORMAL);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_geyser);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < 19) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            ll_header.setLayoutParams(layoutParams);
            LinearLayout contentView = (LinearLayout) findViewById(R.id.ll_content);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams1);
        }
        initView();
    }


    private void initView() {
        tv_device_name.setText("3栋－5楼－510");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_BONUS_CODE) {

            }
        }
    }
}
