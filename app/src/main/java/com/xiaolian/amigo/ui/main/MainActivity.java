package com.xiaolian.amigo.ui.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.data.enumeration.Orientation;
import com.xiaolian.amigo.data.network.model.dto.response.BannerDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.main.update.IVersionModel;
import com.xiaolian.amigo.ui.main.update.IntentKey;
import com.xiaolian.amigo.ui.main.update.UpdateActivity;
import com.xiaolian.amigo.ui.notice.NoticeListActivity;
import com.xiaolian.amigo.ui.repair.RepairActivity;
import com.xiaolian.amigo.ui.user.EditDormitoryActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.wallet.PrepayActivity;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.ui.widget.dialog.GuideDialog;
import com.xiaolian.amigo.ui.widget.dialog.NoticeAlertDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Data;

import static com.xiaolian.amigo.data.enumeration.Device.DISPENSER;
import static com.xiaolian.amigo.data.enumeration.Device.HEATER;
import static com.xiaolian.amigo.util.Log.getContext;

/**
 * 首页
 * Created by yik on 2017/9/5.
 */

public class MainActivity extends MainBaseActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String INTENT_KEY_MAC_ADDRESS = "intent_key_mac_address";
    public static final String INTENT_KEY_LOCATION = "intent_key_location";
    public static final String INTENT_KEY_DEVICE_TYPE = "intent_key_device_type";
    public static final String INTENT_KEY_RESIDENCE_ID = "intent_key_residence_id";
    public static final String INTENT_KEY_RECOVERY = "intent_key_recovery";
    public static final String INTENT_KEY_SWITCH_TO_HOME = "intent_key_switch_to_home";
    public static final String INTENT_KEY_SERVER_ERROR = "intent_key_server_error";
    public static final String INTENT_KEY_BANNERS = "intent_key_banners";
    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_PROFILE = "profile";

    @Inject
    IMainPresenter<IMainView> presenter;

    @BindView(R.id.bt_switch)
    ImageView btSwitch;

    @BindView(R.id.tv_nickName)
    TextView tv_nickName;

    @BindView(R.id.tv_schoolName)
    TextView tv_schoolName;

    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    /**
     * 通知数量上标
     */
    @BindView(R.id.tv_notice_count)
    TextView tv_notice_count;

    /**
     * MainActivity根View
     */
    @BindView(R.id.rl_main)
    RelativeLayout rl_main;

    /**
     * 通知
     */
    @BindView(R.id.rl_notice)
    RelativeLayout rl_notice;

    @BindView(R.id.sv_main)
    ScrollView sl_main;

    private GestureDetector mGestureDetector;

    private DecimalFormat df = new DecimalFormat("###.##");

    HomeFragment2 homeFragment;
    ProfileFragment2 profileFragment;

    int current = 0;
    private boolean hasBanners;
    List<BriefSchoolBusiness> businesses;
    private AvailabilityDialog availabilityDialog;
    private AvailabilityDialog openLocationDialog;
    private int heaterOrderSize;
    private int dispenserOrderSize;
    private PrepayDialog prepayDialog;
    private Long lastRepairTime;
    private Boolean isServerError;
    private OrderPreInfoDTO orderPreInfo;
    private ArrayList<BannerDTO> defaultBanners;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(this);
        // 友盟日志加密
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setCatchUncaughtExceptions(true);

        btSwitch.setBackgroundResource(R.drawable.profile);

        presenter.checkUpdate(AppUtils.getAppVersionCode(this),
                AppUtils.getVersionName(this));

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment2();
            getSupportFragmentManager().beginTransaction().add(R.id.fm_container, homeFragment, FRAGMENT_TAG_HOME).commit();
        } else {
            homeFragment = (HomeFragment2) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_HOME);
            profileFragment = (ProfileFragment2) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_PROFILE);
            if (homeFragment != null && profileFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .show(homeFragment)
                        .hide(profileFragment)
                        .commitAllowingStateLoss();
            } else {
                if (homeFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .show(homeFragment).commitAllowingStateLoss();
                }
                if (profileFragment != null && homeFragment == null) {
                    getSupportFragmentManager().beginTransaction()
                            .show(profileFragment).commitAllowingStateLoss();
                }
            }
        }

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (hasBanners && current == 0) {
                    Banner banner = (Banner) sl_main.getRootView().findViewById(R.id.banner);
                    if (banner != null) {
                        RectF rectF = CommonUtil.calcViewScreenLocation(banner);
                        if (rectF.contains(e1.getRawX(), e1.getRawY())) {
                            return false;
                        }
                    }
                }
                if ((e1.getRawX() - e2.getRawX()) > 200) {
                    onSwitch(Orientation.RIGHT_TO_LEFT);
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX()) > 200) {
                    onSwitch(Orientation.LEFT_TO_RIGHT);
                    return true;//消费掉当前事件  不让当前事件继续向下传递
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isSwitchToHome = intent.getBooleanExtra(INTENT_KEY_SWITCH_TO_HOME, false);
            if (isSwitchToHome) {
                onSwitch(0);
            }
            int actionType = intent.getIntExtra(Constant.INTENT_ACTION, 0);
            if (IntentAction.getAction(actionType) == IntentAction.ACTION_GOTO_HEATER) {
                if (btSwitch != null) {
                    btSwitch.postDelayed(this::gotoHeater, 200);
                }
            }
        }
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isServerError = getIntent().getBooleanExtra(INTENT_KEY_SERVER_ERROR, false);
            defaultBanners = getIntent().getParcelableArrayListExtra(INTENT_KEY_BANNERS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d(TAG, "dispatchTouchEvent");
        if (mGestureDetector.onTouchEvent(ev)) {
            Log.d(TAG, "mGestureDetector onTouchEvent");
            return true;
        }
        if (hasBanners && current == 0) {
            Banner banner = (Banner) sl_main.getRootView().findViewById(R.id.banner);
            if (banner != null) {
                RectF rectF = CommonUtil.calcViewScreenLocation(banner);
                if (rectF.contains(ev.getRawX(), ev.getRawY())) {
                    if (!presenter.isLogin()) {
                        redirectToLogin();
                        return true;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            // 检测MainActivity的RootView是否为空，为空表示出现白屏的情况，要重新把rl_main添加到RootView里
//            FrameLayout contentView = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
//            if (rl_main.getParent() == null) {
//                Log.wtf(TAG, "rl_main的parent为null");
//                contentView.removeAllViews();
//                contentView.addView(rl_main);
//            } else if (rl_main.getParent() != contentView) {
//                Log.wtf(TAG, "rl_main的parent和RootView不一致");
//                contentView.removeAllViews();
//                ((FrameLayout) rl_main.getParent()).removeView(rl_main);
//                contentView.addView(rl_main);
//            }
//        } catch (Exception e) {
//            Log.wtf(TAG, e);
//        }
        Log.d(TAG, "onResume");
        showBanners(null);
        if (!isNetworkAvailable()) {
            showNoticeAmount(0);
            initSchoolBiz();
            onError(R.string.network_available_error_tip);
            return;
        }
        if (!presenter.isLogin()) {
            showNoticeAmount(0);
            initSchoolBiz();
            Log.d(TAG, "onResume: not login");
            tv_nickName.setText("登录／注册");
            tv_schoolName.setText("登录以后才能使用哦");
            iv_avatar.setImageResource(R.drawable.ic_picture_error);
        } else {
            if (isServerError) {
                showNoticeAmount(0);
                initSchoolBiz();
            }
            uploadDeviceInfo();
            // 请求通知
            presenter.getNoticeAmount();
            presenter.getSchoolBusiness();
            Log.d(TAG, "onResume: login");
            // 设置昵称
            tv_nickName.setText(presenter.getUserInfo().getNickName());
            // 设置学校
            tv_schoolName.setText(presenter.getUserInfo().getSchoolName());
            // 设置头像
            if (presenter.getUserInfo().getPictureUrl() != null) {
                Glide.with(this).load(Constant.IMAGE_PREFIX + presenter.getUserInfo().getPictureUrl())
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(iv_avatar);
            } else {
                iv_avatar.setImageResource(R.drawable.ic_picture_error);
            }
        }
    }

    private void uploadDeviceInfo() {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String model = Build.MODEL;
        String brand = Build.BRAND;
        int systemVersion = Build.VERSION.SDK_INT;
        String appVersion = AppUtils.getVersionName(this);
        presenter.uploadDeviceInfo(appVersion, brand, model,
                systemVersion, androidId);
    }

    // orientation 1 表示左->右 右->左
    void onSwitch(Orientation orientation) {
        // 未登录跳转到登录页
        if (!presenter.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        switch (orientation) {
            case LEFT_TO_RIGHT:
                // 当前在home
                if (current == 0) {
                    EventBus.getDefault().post(new ProfileFragment2.Event(
                            ProfileFragment2.Event.EventType.CHANGE_ANIMATION,
                            R.anim.layout_animation_profile_slide_left_to_right));
                    imageViewAnimatedChange(this, btSwitch, R.drawable.home, orientation);
                } else {
                    EventBus.getDefault().post(
                            new HomeFragment2.Event(
                                    HomeFragment2.Event.EventType.CHANGE_ANIMATION,
                                    R.anim.layout_animation_home_slide_left_to_right));
                    imageViewAnimatedChange(this, btSwitch, R.drawable.profile, orientation);
                }
                break;
            case RIGHT_TO_LEFT:
                if (current == 0) {
                    EventBus.getDefault().post(new ProfileFragment2.Event(
                            ProfileFragment2.Event.EventType.CHANGE_ANIMATION,
                            R.anim.layout_animation_profile_slide_right_to_left));
                    imageViewAnimatedChange(this, btSwitch, R.drawable.home, orientation);
                } else {
                    EventBus.getDefault().post(
                            new HomeFragment2.Event(
                                    HomeFragment2.Event.EventType.CHANGE_ANIMATION,
                                    R.anim.layout_animation_home_slide_right_to_left));
                    imageViewAnimatedChange(this, btSwitch, R.drawable.profile, orientation);
                }
                break;
        }
        onSwitch();
    }

    @OnClick(R.id.bt_switch)
    void onSwitchClick() {
        // 未登录跳转到登录页
        if (!presenter.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (current == 0) {
            EventBus.getDefault().post(new ProfileFragment2.Event(
                    ProfileFragment2.Event.EventType.CHANGE_ANIMATION,
                    R.anim.layout_animation_profile_slide_left_to_right));
            imageViewAnimatedChange(this, btSwitch, R.drawable.home, Orientation.LEFT_TO_RIGHT);
        } else {
            EventBus.getDefault().post(
                    new HomeFragment2.Event(
                            HomeFragment2.Event.EventType.CHANGE_ANIMATION,
                            R.anim.layout_animation_home_slide_right_to_left));
            imageViewAnimatedChange(this, btSwitch, R.drawable.profile, Orientation.RIGHT_TO_LEFT);
        }
        onSwitch();
    }

    void onSwitch() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (current == 0) {
            // 未登录跳转到登录页
            if (!presenter.isLogin()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            if (!presenter.isMainGuideDone()) {
                // 显示引导页
                GuideDialog guideDialog = new GuideDialog(this, GuideDialog.TYPE_MAIN);
                guideDialog.show();
            }
            if (profileFragment == null) {
                profileFragment = new ProfileFragment2();
            }
            if (homeFragment == null) {
                homeFragment = new HomeFragment2();
            }
            if (homeFragment.isAdded() && homeFragment.isVisible()) {
                transaction = transaction.hide(homeFragment);
            }
            if (!profileFragment.isAdded()) {
                transaction.add(R.id.fm_container, profileFragment, FRAGMENT_TAG_PROFILE).commitAllowingStateLoss();
            } else {
                transaction.show(profileFragment).commitAllowingStateLoss();
            }
            current = 1;
        } else {
            if (profileFragment == null) {
                profileFragment = new ProfileFragment2();
            }
            if (homeFragment == null) {
                homeFragment = new HomeFragment2();
            }
            if (profileFragment.isAdded() && homeFragment.isVisible()) {
                transaction = transaction.hide(profileFragment);
            }
            if (!homeFragment.isAdded()) {
                transaction.add(R.id.fm_container, homeFragment, FRAGMENT_TAG_HOME).commitAllowingStateLoss();
            } else {
                transaction.show(homeFragment).commitAllowingStateLoss();
            }
            current = 0;
        }
    }

    void onSwitch(int current) {
        if (this.current != current) {
            if (current == 0) {
                btSwitch.setBackgroundResource(R.drawable.home);
            } else {
                btSwitch.setBackgroundResource(R.drawable.profile);
            }
            onSwitch();
        }
    }

    public void imageViewAnimatedChange(Context context, ImageView imageView, int res, Orientation orientation) {
        final Animation anim_out_right = AnimationUtils.loadAnimation(context, R.anim.item_slide_out_right);
        final Animation anim_out_left = AnimationUtils.loadAnimation(context, R.anim.item_slide_out_left);
        final Animation anim_in_left = AnimationUtils.loadAnimation(context, R.anim.item_slide_in_left);
        final Animation anim_in_right = AnimationUtils.loadAnimation(context, R.anim.item_slide_in_right);
        switch (orientation) {
            case LEFT_TO_RIGHT:
                anim_out_right.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setBackgroundResource(res);
                        anim_in_right.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }
                        });
                        imageView.startAnimation(anim_in_right);
                    }
                });
                imageView.startAnimation(anim_out_right);
                break;
            case RIGHT_TO_LEFT:
                anim_out_left.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setBackgroundResource(res);
                        anim_in_right.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }
                        });
                        imageView.startAnimation(anim_in_left);
                    }
                });
                imageView.startAnimation(anim_out_left);
                break;
        }
    }


    @OnClick({R.id.iv_avatar, R.id.ll_user_info})
    void gotoLoginView() {
        if (!presenter.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 通知
     */
    @OnClick(R.id.iv_notice)
    void gotoNoticeList() {
        if (presenter.isLogin()) {
            startActivity(new Intent(this, NoticeListActivity.class));
        } else {
            gotoLoginView();
        }
    }

    /**
     * 点击头像 跳转到编辑个人信息页面
     */
    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
        if (checkLogin()) {
            startActivity(this, EditProfileActivity.class);
        }
    }

    /**
     * 点击昵称 跳转到编辑个人信息页面
     */
    @OnClick(R.id.ll_user_info)
    void onUserInfoClick() {
        if (checkLogin()) {
            startActivity(this, EditProfileActivity.class);
        }
    }


    /**
     * 显示通知个数
     *
     * @param amount 通知个数
     */
    @Override
    public void showNoticeAmount(Integer amount) {
        Log.d(TAG, "showNoticeAmount: " + amount);
        if (amount != null && amount != 0) {
            tv_notice_count.setVisibility(View.VISIBLE);
            tv_notice_count.setText(String.valueOf(amount));
        } else {
            tv_notice_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data) {
        Log.d(TAG, "showTimeValidDialog: " + data.getTitle() + "->" + data.getRemark() + "->" + deviceType);
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
        availabilityDialog.setOkText(getString(R.string.keep_use));
        availabilityDialog.setTip(data.getTitle());
        availabilityDialog.setSubTip(data.getRemark());
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            if (deviceType == Device.HEATER.getType()) {
                presenter.gotoHeaterDevice(data.getDefaultMacAddress(), data.getLocation(),
                        data.getResidenceId());
            } else if (deviceType == Device.DISPENSER.getType()){
                gotoChooseDispenser();
            }
        });
        availabilityDialog.show();
    }

    private void gotoChooseDispenser() {
        Intent intent = new Intent(this, ChooseDispenserActivity.class);
        intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
        startActivity(intent);
    }

    @Override
    public void gotoDevice(Device device, String macAddress, String location, Long residenceId, boolean recovery) {
        Log.d(TAG, "gotoDevice: " + device.getDesc() + "->" + macAddress + "->" + location + "->" + residenceId);
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, device.getClz());
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, device.getType());
            intent.putExtra(INTENT_KEY_RESIDENCE_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }

    public void gotoDispenser(String macAddress, String location, Long residenceId,
                              boolean favor, int usefor,
                              boolean recovery) {
        if (TextUtils.isEmpty(macAddress)) {
            onError("设备macAddress不合法");
        } else {
            Intent intent = new Intent(this, DispenserActivity.class);
            intent.putExtra(INTENT_KEY_LOCATION, location);
            intent.putExtra(INTENT_KEY_MAC_ADDRESS, macAddress);
            intent.putExtra(INTENT_KEY_DEVICE_TYPE, Device.DISPENSER.getType());
            intent.putExtra(DispenserActivity.INTENT_KEY_ID, residenceId);
            intent.putExtra(MainActivity.INTENT_KEY_RECOVERY, recovery);
            intent.putExtra(DispenserActivity.INTENT_KEY_FAVOR, favor);
            intent.putExtra(DispenserActivity.INTENT_KEY_TEMPERATURE, String.valueOf(usefor));
            intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
            startActivity(intent);
        }
    }

    @Override
    public void showUrgentNotify(String content, Long id) {
        Log.d(TAG, "showUrgentNotify: " + content + "->" + id);
        NoticeAlertDialog dialog = new NoticeAlertDialog(this);
        dialog.setContent(content);
        dialog.setOnOkClickListener((dialog1, isNotReminder) -> {
            if (isNotReminder) {
                presenter.readUrgentNotify(id);
            }
        });
        dialog.show();
    }

    @Override
    public void refreshNoticeAmount() {
        Log.d(TAG, "refreshNoticeAmount");
        presenter.getNoticeAmount();
    }

    @Override
    public void showBanners(List<BannerDTO> banners) {
        List<BannerDTO> settingBanners = new ArrayList<>();
        if (defaultBanners != null) {
            settingBanners.addAll(defaultBanners);
        }
        if (banners != null) {
            settingBanners.addAll(banners);
        }
        if (!settingBanners.isEmpty()) {
            Log.d(TAG, "showBanners");
            hasBanners = true;
            EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.BANNER,
                    settingBanners));
        }
    }

    private void initSchoolBiz() {
        EventBus.getDefault()
                .post(new HomeFragment2.Event(HomeFragment2.Event.EventType.INIT_BIZ,
                        null));
    }

    @Override
    public void showSchoolBiz(List<BriefSchoolBusiness> businesses) {
        if (businesses == null) {
            initSchoolBiz();
            return;
        }
        Log.d(TAG, "showSchoolBiz");
        this.businesses = businesses;
        for (BriefSchoolBusiness business : businesses) {
            if (business.getBusinessId() == 1) {
                heaterOrderSize = business.getPrepayOrder();
            } else if (business.getBusinessId() == 2) {
                dispenserOrderSize = business.getPrepayOrder();
            }
        }
        EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.SCHOOL_BIZ,
                businesses));
    }

    @Override
    public void showDeviceUsageDialog(int type, DeviceCheckRespDTO data) {
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
        // 2小时内存在未找零订单
        if (data.getExistsUnsettledOrder() != null && data.getExistsUnsettledOrder()) {
            // 1 表示热水澡 2 表示饮水机
            if (type == Device.HEATER.getType()) {
                // 直接前往热水澡处理找零
                gotoDevice(HEATER, data.getUnsettledMacAddress(), data.getLocation(),
                        data.getResidenceId(), true);
            } else if (type == Device.DISPENSER.getType()){
                gotoDispenser(data.getUnsettledMacAddress(), data.getLocation(), data.getResidenceId(),
                        data.getFavor(), data.getUsefor(),true);
            }
        } else {
            if (type == Device.HEATER.getType() && heaterOrderSize > 0) {
                showPrepayDialog(type, heaterOrderSize, data);
            } else if (type == Device.DISPENSER.getType() && dispenserOrderSize > 0) {
                showPrepayDialog(type, dispenserOrderSize, data);
            } else {
                // 如果热水澡 检查默认宿舍
                if (type == Device.HEATER.getType() && !presenter.checkDefaultDormitoryExist()) {
                    showBindDormitoryDialog();
                    return;
                }
                if (!data.getTimeValid()) {
                    showTimeValidDialog(type, data);
                } else {
                    if (type == Device.HEATER.getType()) {
                        // 前往默认宿舍的热水澡
                        presenter.gotoHeaterDevice(data.getDefaultMacAddress(), data.getLocation(),
                                data.getResidenceId());
                    } else if (type == Device.DISPENSER.getType()){
                        // 进入饮水机选择页面
                        gotoChooseDispenser();
                    }
                }
            }
        }
    }

    @Override
    public void showBindDormitoryDialog() {
        Log.d(TAG, "showBindDormitoryDialog");
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()
                && availabilityDialog.getType() == AvailabilityDialog.Type.BIND_DORMITORY) {
            return;
        }
        availabilityDialog.setType(AvailabilityDialog.Type.BIND_DORMITORY);
        availabilityDialog.setOkText("前往绑定");
        availabilityDialog.setTip("需要先绑定宿舍信息");
        availabilityDialog.setSubTipVisible(false);
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            Intent intent;
            intent = new Intent(this, ListChooseActivity.class);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_BUILDING);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.MAIN_ACTIVITY_SRC);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
            startActivity(intent);
        });
        availabilityDialog.show();
    }

    @Override
    public void showOpenLocationDialog() {
        Log.d(TAG, "showOpenLocationDialog");
        if (null == openLocationDialog) {
            openLocationDialog = new AvailabilityDialog(this);
        }
        if (openLocationDialog.isShowing()
                && openLocationDialog.getType() == AvailabilityDialog.Type.OPEN_LOCAION_SERVICE) {
            return;
        }
        openLocationDialog.setType(AvailabilityDialog.Type.OPEN_LOCAION_SERVICE);
        openLocationDialog.setOkText("前往设置");
        openLocationDialog.setTip("设备用水需要开启位置服务");
        openLocationDialog.setSubTipVisible(false);
        openLocationDialog.setOnOkClickListener(dialog1 -> {
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.startActivityForResult(locationIntent, REQUEST_LOCATION);
        });
        openLocationDialog.show();
    }

    @Override
    public void showNoDeviceDialog() {
        Log.d(TAG, "showNoDeviceDialog");
        if (null == availabilityDialog) {
            availabilityDialog = new AvailabilityDialog(this);
        }
        if (availabilityDialog.isShowing()) {
            if (availabilityDialog.getType() == AvailabilityDialog.Type.NO_DEVICE) {
                return;
            } else {
                availabilityDialog.cancel();
            }
        }
        availabilityDialog.setType(AvailabilityDialog.Type.NO_DEVICE);
        availabilityDialog.setOkText("前往设置");
        availabilityDialog.setTip("你的默认宿舍无设备");
        availabilityDialog.setSubTipVisible(false);
        availabilityDialog.setOnOkClickListener(dialog1 ->
                startActivity(new Intent(MainActivity.this, EditDormitoryActivity.class)));
        availabilityDialog.show();
    }

    @Override
    public void refreshProfile(PersonalExtraInfoDTO data) {
        lastRepairTime = data.getLastRepairTime();
        presenter.setBalance(df.format(data.getBalance()));
        presenter.setBonusAmount(data.getBonusAmount());
        EventBus.getDefault().post(data);
    }

    @Override
    public void showUpdateDialog(IVersionModel model) {
        Log.i(TAG, "showUpdateDialog");
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(this, UpdateActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKey.MODEL, model);
                        intent.putExtra(IntentKey.NOTIFICATION_ICON, R.mipmap.ic_launcher);
                        startActivity(intent);
                    } else {
                        showMessage("没有SD卡权限");
                    }
                });
    }

    public void refreshProfile() {
        PersonalExtraInfoDTO data = new PersonalExtraInfoDTO();
        data.setBalance(Double.valueOf(presenter.getBalance()));
        data.setBonusAmount(presenter.getBonusAmount());
        EventBus.getDefault().post(data);
    }

    public void showPrepayDialog(int type, int prepaySize, DeviceCheckRespDTO data) {
        Log.d(TAG, "showPrepayDialog: " + type + "->" + prepaySize);
        if (prepayDialog == null) {
            prepayDialog = new PrepayDialog(this);
        }
        if (prepayDialog.isShowing()) {
            return;
        }
        prepayDialog.setDeviceTypeAndPrepaySize(type, prepaySize);
        prepayDialog.setOnOkClickListener(dialog -> startActivity(new Intent(MainActivity.this, PrepayActivity.class)));
        prepayDialog.setOnCancelClickListener(dialog -> {
            if (data.getTimeValid()) {
                // 1 表示热水澡 2 表示饮水机
                if (type == Device.HEATER.getType()) {
                    presenter.gotoHeaterDevice(data.getDefaultMacAddress(), data.getLocation(),
                            data.getResidenceId());
                } else if (type == Device.DISPENSER.getType()){
                    gotoChooseDispenser();
                }
            } else {
                showTimeValidDialog(type, data);
            }
        });
        prepayDialog.show();
    }

    @Override
    public void startActivity(AppCompatActivity activity, Class<?> clazz) {
        Map<String, Long> extraMap = new HashMap<String, Long>() {
            {
                put(RepairActivity.INTENT_KEY_LAST_REPAIR_TIME, lastRepairTime);
            }
        };
        super.startActivity(activity, clazz, extraMap);
    }

    // 设备用水检查
    public void checkDeviceUsage(Device device) {
        Log.d(TAG, "checkDeviceUsage");
        presenter.checkDeviceUsage(device.getType());
    }

    private boolean checkLogin() {
        if (TextUtils.isEmpty(presenter.getToken())) {
            redirectToLogin();
            return false;
        }
        return true;
    }

    /**
     * 点击进入热水澡界面
     */
    public void gotoHeater() {
        Log.d(TAG, "gotoHeater");
        setBleCallback(() -> checkDeviceUsage(HEATER));
        getBlePermission();
    }

    /**
     * 点击进入饮水机页面
     */
    public void gotoDispenser() {
        Log.d(TAG, "gotoDispenser");
        setBleCallback(() -> checkDeviceUsage(DISPENSER));
        getBlePermission();
    }

    /**
     * 点击进入失物招领
     */
    public void gotoLostAndFound() {
        Log.d(TAG, "gotoLostAndFound");
        startActivity(this, LostAndFoundActivity.class);
    }

    public void logout() {
        Log.d(TAG, "logout");
        presenter.logout();
    }

    @Override
    public void onBackPressed() {
        //返回桌面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        if (prepayDialog != null) {
            prepayDialog.dismiss();
        }
        if (availabilityDialog != null) {
            availabilityDialog.dismiss();
        }
        if (openLocationDialog != null) {
            openLocationDialog.dismiss();
        }
        presenter.onDetach();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        Log.d(TAG, "onEvent: " + event.getType().type);
        switch (event.getType()) {
            case GOTO_HEATER:
                if (checkLogin()) {
                    gotoHeater();
                } else {
                    EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                }
                break;
            case GOTO_DISPENSER:
                if (checkLogin()) {
                    gotoDispenser();
                }
                break;
            case GOTO_LOST_AND_FOUND:
                if (checkLogin()) {
                    gotoLostAndFound();
                }
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                break;
            case START_ACTIVITY:
                if (checkLogin()) {
                    startActivity(this, (Class) event.getObject());
                }
                break;
            case REFRESH_NOTICE:
                refreshProfile();
                break;
        }
    }

    @Data
    public static class Event {
        EventType type;
        Object object;

        public Event(EventType type, Object object) {
            this.type = type;
            this.object = object;
        }

        public Event(EventType type) {
            this.type = type;
        }

        public enum EventType {
            GOTO_HEATER(1),
            GOTO_DISPENSER(2),
            GOTO_LOST_AND_FOUND(3),
            START_ACTIVITY(4),
            REFRESH_NOTICE(5),
            LOGOUT(6);
            private int type;

            EventType(int type) {
                this.type = type;
            }
        }
    }

}
