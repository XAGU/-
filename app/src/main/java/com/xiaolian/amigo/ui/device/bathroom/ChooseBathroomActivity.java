package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.ui.device.washer.ScanActivity;
import com.xiaolian.amigo.ui.widget.GridSpacesItemDecoration;
import com.xiaolian.amigo.ui.widget.MetaBallView;
import com.xiaolian.amigo.ui.widget.ZoomRecyclerView;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BALANCE;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_AMOUNT;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_DESC;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_BONUS_ID;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_EXPIRED_TIME;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_LOCATION;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MAX_MISSABLE_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MIN_PREPAY;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_MISSED_TIMES;
import static com.xiaolian.amigo.ui.device.bathroom.BathroomConstant.KEY_PREPAY;

/**
 * 选择浴室
 *
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomActivity extends BathroomBaseActivity implements IChooseBathroomView {
    @Inject
    IChooseBathroomPresenter<IChooseBathroomView> presenter;

    private List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroups = new ArrayList<>();

    private ZoomRecyclerView recyclerView;
    private ChooseBathroomOuterAdapter outerAdapter;
    private ImageView ivHelp;
    private MetaBallView metaBall;
    private int lastSelectedGroupPosition = -1;
    private int lastSelectedRoomPosition = -1;
    /**
     * 当前是否处于选中状态 选中状态显示预约使用 非选中状态显示购买编码
     */
    private boolean isSelected = false;
    /**
     * 是否显示付费使用
     */
    private boolean isShowBuyUse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bathroom);
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerView);
        ivHelp = findViewById(R.id.iv_help);
        ivHelp.setOnClickListener(v -> {
            isSelected = !isSelected;
//            changeToBookingUse();
//            metaBall.translation();
            gotoShowerAddress();

        });
        metaBall = findViewById(R.id.metaBall);
        metaBall.setOnButtonClickListener(left -> {
            if (left) {
                onLeftClick();
            } else {
                onRightClick();
            }
        });
    }

    private void gotoShowerAddress() {
        startActivity(new Intent(this, ShowerAddressActivity.class));
    }

    private void initRecyclerView() {
        setMockData(bathGroups);
        outerAdapter = new ChooseBathroomOuterAdapter(this, R.layout.item_choose_bathroom_outer, bathGroups,
                (groupPosition, bathroomPosition) -> {
                    onSuccess(groupPosition + " " + bathroomPosition);
                    if (bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).getStatus()
                            != ChooseBathroomAdapter.BathroomStatus.AVAILABLE) {
                        if (lastSelectedGroupPosition != -1 && lastSelectedRoomPosition != -1) {
                            bathGroups.get(lastSelectedGroupPosition).getBathGroups().get(lastSelectedRoomPosition).setSelected(false);
                        }
                        lastSelectedGroupPosition = -1;
                        lastSelectedRoomPosition = -1;
                        isSelected = false;
                    } else {
                        if (lastSelectedGroupPosition != -1 && lastSelectedRoomPosition != -1) {
                            bathGroups.get(lastSelectedGroupPosition).getBathGroups().get(lastSelectedRoomPosition).setSelected(false);
                        }
                        bathGroups.get(groupPosition).getBathGroups().get(bathroomPosition).setSelected(true);
                        lastSelectedGroupPosition = groupPosition;
                        lastSelectedRoomPosition = bathroomPosition;
                        isSelected = true;
                    }
                    outerAdapter.notifyDataSetChanged();
                    changeBottomUseWay();
                });
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        recyclerView.setLayoutParams(lp);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(outerAdapter);
        recyclerView.setOnGestureListener(new ZoomRecyclerView.OnGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector, float scaleFactor) {
                if (scaleFactor > 2
                        && !outerAdapter.isScaled()) {
                    outerAdapter.setScaled(true);
                } else if (scaleFactor < 2
                        && outerAdapter.isScaled()) {
                    outerAdapter.setScaled(false);
                }
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }
        });
    }

    private void changeBottomUseWay() {
        if (!isSelected) {
            metaBall.changeToBuyCodeWay();
        } else {
            metaBall.changeToBookingWay();
        }
    }

//    private void changeToBookingUse() {
//        if (!isSelected) {
//            llRight.setVisibility(View.VISIBLE);
//            tvMissedBookingTime.setVisibility(View.INVISIBLE);
//            tvLeft.setText("购买编码");
//            ivLeft.setImageResource(R.drawable.ic_bathroom_buy_code);
//            tvRight.setText("扫一扫");
//            ivRight.setImageResource(R.drawable.ic_bathroom_scan);
//        } else {
//            tvMissedBookingTime.setVisibility(View.VISIBLE);
//            tvLeft.setText("预约使用");
//            ivLeft.setImageResource(R.drawable.ic_bathroom_booking);
//            if (isShowBuyUse) {
//                llRight.setVisibility(View.VISIBLE);
//                tvRight.setText("付费使用");
//                ivRight.setImageResource(R.drawable.ic_bathroom_pay_use);
//            } else {
//                llRight.setVisibility(View.GONE);
//            }
//        }
//    }

    private void onLeftClick() {
        if (isSelected) {
            presenter.preBooking();
        } else {
            startActivity(new Intent(this, BuyCodeActivity.class));
        }
    }

    private void onRightClick() {
        if (isSelected) {
            startActivity(new Intent(this, PayUseActivity.class));
        } else {
//            startActivity(new Intent(this, BathroomScanActivity.class));
            startActivity(new Intent(this, ScanUseActivity.class));
        }
    }

    private void setMockData(List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroups) {
        List<ChooseBathroomAdapter.BathroomWrapper> inner1 = new ArrayList<>();
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("101", ChooseBathroomAdapter.BathroomStatus.NONE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("102", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("103", ChooseBathroomAdapter.BathroomStatus.NONE));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("104", ChooseBathroomAdapter.BathroomStatus.USING));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner1.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.ERROR));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner1, "一层A"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner2 = new ArrayList<>();
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("101", ChooseBathroomAdapter.BathroomStatus.NONE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("102", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("103", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("104", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner2.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner2, "一层B"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner3 = new ArrayList<>();
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("101", ChooseBathroomAdapter.BathroomStatus.NONE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("102", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("103", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("104", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner3.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner3, "一层A"));

        List<ChooseBathroomAdapter.BathroomWrapper> inner4 = new ArrayList<>();
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("101", ChooseBathroomAdapter.BathroomStatus.NONE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("102", ChooseBathroomAdapter.BathroomStatus.ERROR));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("103", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("104", ChooseBathroomAdapter.BathroomStatus.AVAILABLE));
        inner4.add(new ChooseBathroomAdapter.BathroomWrapper("105", ChooseBathroomAdapter.BathroomStatus.USING));
        bathGroups.add(new ChooseBathroomOuterAdapter.BathGroupWrapper(inner4, "一层A"));
    }

    @Override
    public void refreshBathroom(List<ChooseBathroomOuterAdapter.BathGroupWrapper> wrappers,
                                List<Integer> methods, Integer missTimes) {
        bathGroups.clear();
        bathGroups.addAll(wrappers);
        outerAdapter.notifyDataSetChanged();
    }

    @Override
    public void gotoBookingView(Double balance,
                                Long bonusId, String bonusDesc, Double bonusAmount,
                                Long expiredTime, String location, Integer maxMissAbleTimes,
                                Double minPrepay, Integer missedTimes, Double prepay) {
        startActivity(new Intent(this, BookingActivity.class)
                .putExtra(KEY_BALANCE, balance)
                .putExtra(KEY_BONUS_ID, bonusId)
                .putExtra(KEY_BONUS_DESC, bonusDesc)
                .putExtra(KEY_BONUS_AMOUNT, bonusAmount)
                .putExtra(KEY_EXPIRED_TIME, expiredTime)
                .putExtra(KEY_LOCATION, location)
                .putExtra(KEY_MAX_MISSABLE_TIMES, maxMissAbleTimes)
                .putExtra(KEY_MIN_PREPAY, minPrepay)
                .putExtra(KEY_MISSED_TIMES, missedTimes)
                .putExtra(KEY_PREPAY, prepay)
        );
    }
}
