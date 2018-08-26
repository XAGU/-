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
import android.support.v4.util.ObjectsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserCategory;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.data.enumeration.Orientation;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.DeviceConstant;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.dryer.DryerActivity;
import com.xiaolian.amigo.ui.device.washer.ScanActivity;
import com.xiaolian.amigo.ui.device.washer.WasherActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.main.update.IVersionModel;
import com.xiaolian.amigo.ui.main.update.IntentKey;
import com.xiaolian.amigo.ui.main.update.UpdateActivity;
import com.xiaolian.amigo.ui.notice.NoticeListActivity;
import com.xiaolian.amigo.ui.repair.RepairActivity;
import com.xiaolian.amigo.ui.user.CompleteInfoActivity;
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
import com.xiaolian.amigo.util.MD5Util;
import com.xiaolian.amigo.util.ScreenUtils;
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
import static com.xiaolian.amigo.data.enumeration.Device.DRYER;
import static com.xiaolian.amigo.data.enumeration.Device.HEATER;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_BUILDING_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_TYPE;
import static com.xiaolian.amigo.ui.device.washer.ScanActivity.IS_SACN;
import static com.xiaolian.amigo.ui.device.washer.ScanActivity.SCAN_TYPE;
import static com.xiaolian.amigo.util.Log.getContext;

/**
 * 首页
 *
 * @author yik
 * @date 17/9/5
 */

public class MainActivity extends MainBaseActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String INTENT_KEY_MAC_ADDRESS = "intent_key_mac_address";
    public static final String INTENT_KEY_LOCATION = "intent_key_location";
    public static final String INTENT_KEY_SUPPLIER_ID = "intent_key_supplier_id";
    public static final String INTENT_KEY_DEVICE_TYPE = "intent_key_device_type";
    public static final String INTENT_KEY_RESIDENCE_ID = "intent_key_residence_id";
    public static final String INTENT_KEY_RECOVERY = "intent_key_recovery";
    public static final String INTENT_KEY_SWITCH_TO_HOME = "intent_key_switch_to_home";
    public static final String INTENT_KEY_SERVER_ERROR = "intent_key_server_error";
    public static final String INTENT_KEY_BANNERS = "intent_key_banners";
    public static final String INTENT_KEY_SCAN = "intent_key_scan";
    private static final String FRAGMENT_TAG_HOME = "home";
    private static final String FRAGMENT_TAG_PROFILE = "profile";
    private static final int GESTURE_DETECTOR_MIN_LENGHT = 200;

    @Inject
    IMainPresenter<IMainView> presenter;

    @BindView(R.id.bt_switch)
    ImageView btSwitch;

    @BindView(R.id.tv_nickName)
    TextView tvNickName;

    @BindView(R.id.tv_schoolName)
    TextView tvSchoolName;

    /**
     * 头像
     */
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    /**
     * 通知数量上标
     */
    @BindView(R.id.tv_notice_count)
    TextView tvNoticeCount;

    /**
     * MainActivity根View
     */
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    /**
     * 通知
     */
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;

    @BindView(R.id.sv_main)
    ScrollView slMain;

    @BindView(R.id.fm_container)
    LinearLayout llContainer;

    /**
     * 校ok迁移
     */
    @BindView(R.id.iv_xok_migrate)
    ImageView ivXokMigrate;

    private GestureDetector mGestureDetector;

    private DecimalFormat df = new DecimalFormat("###.##");

    HomeFragment2 homeFragment;
    ProfileFragment2 profileFragment;

    int current = 0;

    private boolean isNotice = false  ;
    private boolean hasBanners;
    List<BriefSchoolBusiness> businesses;
    private AvailabilityDialog availabilityDialog;
    private AvailabilityDialog openLocationDialog;
    private int heaterOrderSize;
    private int dispenserOrderSize;
    private int dryerOrderSize;
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

        if (isNotice){
            presenter.routeHeaterOrBathroom();
        }
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
                if (e1 == null || e2 == null) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
                if (hasBanners && current == 0) {
                    Banner banner = (Banner) slMain.getRootView().findViewById(R.id.banner);
                    if (banner != null) {
                        RectF rectF = CommonUtil.calcViewScreenLocation(banner);
                        if (rectF.contains(e1.getRawX(), e1.getRawY())) {
                            return false;
                        }
                    }
                }
                if ((e1.getRawX() - e2.getRawX()) > GESTURE_DETECTOR_MIN_LENGHT) {
                    onSwitch(Orientation.RIGHT_TO_LEFT);
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX()) > GESTURE_DETECTOR_MIN_LENGHT) {
                    onSwitch(Orientation.LEFT_TO_RIGHT);
                    //消费掉当前事件  不让当前事件继续向下传递
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
            isNotice = intent.getBooleanExtra(Constant.BUNDLE_ID , false);
        }


    }

    /**
     * 扫一扫
     */
    @OnClick(R.id.iv_scan)
    public void scan(){
        scanQRCode();
    }
    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isServerError = getIntent().getBooleanExtra(INTENT_KEY_SERVER_ERROR, false);
            defaultBanners = getIntent().getParcelableArrayListExtra(INTENT_KEY_BANNERS);
            isNotice = getIntent().getBooleanExtra(Constant.BUNDLE_ID ,false);
            android.util.Log.e(TAG, "setUp: "  + (defaultBanners == null));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGestureDetector.onTouchEvent(ev)) {
            Log.d(TAG, "mGestureDetector onTouchEvent");
            return true;
        }
        if (hasBanners && current == 0) {
            Banner banner = (Banner) slMain.getRootView().findViewById(R.id.banner);
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

    private void registerXGPush() {
        if (!presenter.isLogin()) {
            presenter.deletePushToken();
            return;
        }
        if (!TextUtils.isEmpty(presenter.getPushToken())) {
            return;
        }
        // 1.获取设备Token
//        Handler handler = new HandlerExtension(MainActivity.this);
//        m = handler.obtainMessage();

        /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
        String pushAccount = MD5Util.md5(presenter.getUserInfo().getId() + Constant.MD5_UID_STR);
        Log.d(TAG, "注册信鸽: " + pushAccount);
        XGPushManager.bindAccount(getApplicationContext(),
                pushAccount,
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
//                        Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                        String pushSchoolTag = MD5Util.md5(presenter.getUserInfo().getSchoolId() + Constant.MD5_SCHOOL_STR);
                        Log.d(TAG, "注册学校tag: " + pushSchoolTag);
                        XGPushManager.setTag(getApplicationContext(), pushSchoolTag);
                        if (!ObjectsCompat.equals(Constant.INVALID_ID, presenter.getUserInfo().getBuildingId())) {
                            String pushBuildingTag = MD5Util.md5(presenter.getUserInfo().getBuildingId() + Constant.MD5_BUILDING_STR);
                            Log.d(TAG, "注册楼栋tag: " + pushBuildingTag);
                            XGPushManager.setTag(getApplicationContext(), pushBuildingTag);
                        }
                        presenter.setPushToken((String) data);
//                        m.obj = "+++ register push sucess. token:" + data;
//                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
//                        Log.w(Constants.LogTag,
//                                "+++ register push fail. token:" + data
//                                        + ", errCode:" + errCode + ",msg:"
//                                        + msg);
                        presenter.deletePushToken();
//                        m.obj = "+++ register push fail. token:" + data
//                                + ", errCode:" + errCode + ",msg:" + msg;
//                        m.sendToTarget();
                    }
                });

        // 获取token
        XGPushConfig.getToken(this);
    }




    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            // 检测MainActivity的RootView是否为空，为空表示出现白屏的情况，要重新把rl_main添加到RootView里
//            FrameLayout contentView = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
//            if (rlMain.getParent() == null) {
//                Log.wtf(TAG, "rl_main的parent为null");
//                contentView.removeAllViews();
//                contentView.addView(rlMain);
//            } else if (rlMain.getParent() != contentView) {
//                Log.wtf(TAG, "rl_main的parent和RootView不一致");
//                contentView.removeAllViews();
//                ((FrameLayout) rlMain.getParent()).removeView(rlMain);
//                contentView.addView(rlMain);
//            }
//        } catch (Exception e) {
//            Log.wtf(TAG, e);
//        }
        Log.d(TAG, "onResume");
        if (isNotice && presenter != null) {
            presenter.routeHeaterOrBathroom();
            isNotice = false ;
        }
        showBanners(null);
        if (!isNetworkAvailable()) {
            onError(R.string.network_available_error_tip);
            showNoticeAmount(0);
            if (presenter.isLogin()) {
                // 设置昵称
                tvNickName.setText(presenter.getUserInfo().getNickName());
                // 设置学校
                tvSchoolName.setText(presenter.getUserInfo().getSchoolName());
                // 设置学校业务
                presenter.getSchoolBusiness();


                presenter.getUser();

                return;
            }
            initSchoolBiz();
            return;
        }
        if (!presenter.isLogin()) {
            showNoticeAmount(0);
            initSchoolBiz();
            Log.d(TAG, "onResume: not login");
            tvNickName.setText("登录／注册");
            tvSchoolName.setText("登录以后才能使用哦");
            ivAvatar.setImageResource(R.drawable.ic_picture_error);
        } else {
            if (isServerError) {
                showNoticeAmount(0);
                initSchoolBiz();
            }
            uploadDeviceInfo();
            // 请求通知
            presenter.getNoticeAmount();
            presenter.getSchoolBusiness();
            presenter.getUser();
            Log.d(TAG, "onResume: login");
            // 设置昵称
            tvNickName.setText(presenter.getUserInfo().getNickName());
            // 设置学校
            tvSchoolName.setText(presenter.getUserInfo().getSchoolName());
            // 设置头像
            if (presenter.getUserInfo().getPictureUrl() != null) {
                Glide.with(this).load(Constant.IMAGE_PREFIX + presenter.getUserInfo().getPictureUrl())
                        .asBitmap()
                        .placeholder(R.drawable.ic_picture_error)
                        .error(R.drawable.ic_picture_error)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.ic_picture_error);
            }
        }
        // 注册信鸽推送
        registerXGPush();
    }





    private void scanQRCode() {
//        startActivity(new Intent(this, CaptureActivity.class));
        IntentIntegrator integrator = new IntentIntegrator(this);
        //底部的提示文字，设为""可以置空
        integrator.setPrompt("");
        //前置或者后置摄像头
        integrator.setCameraId(0);
        //扫描成功的「哔哔」声，默认开启
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(ScanActivity.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.addExtra(DecodeHintType.CHARACTER_SET.name(), "utf-8");
        integrator.addExtra(DecodeHintType.TRY_HARDER.name(), Boolean.TRUE);
        integrator.addExtra(DecodeHintType.POSSIBLE_FORMATS.name(), BarcodeFormat.QR_CODE);
        integrator.addExtra(SCAN_TYPE , 2);
        integrator.addExtra(IS_SACN , true);
        integrator.initiateScan();
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

    /**
     * orientation 1 表示左->右 右->左
     * @param orientation 方向
     */
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
            default:
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
                GuideDialog guideDialog = new GuideDialog(this, GuideDialog.TYPE_WALLET_TIP);
                guideDialog.setAmount(getString(R.string.rmb_symbol) + presenter.getBalance());
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
                btSwitch.setBackgroundResource(R.drawable.profile);
            } else {
                btSwitch.setBackgroundResource(R.drawable.home);
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
            default:
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
            tvNoticeCount.setVisibility(View.VISIBLE);
            tvNoticeCount.setText(String.valueOf(amount));
        } else {
            tvNoticeCount.setVisibility(View.GONE);
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
        availabilityDialog.setOkText(getString(R.string.keep_use_cold_water));
        availabilityDialog.setTitle(data.getTitle());
        availabilityDialog.setTip(data.getRemark());
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            if (deviceType == Device.HEATER.getType()) {
                presenter.gotoHeaterDevice(data.getDefaultMacAddress(),
                        data.getDefaultSupplierId(), data.getLocation(),
                        data.getResidenceId());
            } else if (deviceType == Device.DISPENSER.getType()) {
                gotoChooseDispenser();
            } else if (deviceType == Device.DRYER.getType()) {
                gotoChooseDryer();
            }
        });
        availabilityDialog.show();
    }

    private void gotoChooseDispenser() {
        Intent intent = new Intent(this, ChooseDispenserActivity.class);
        intent.putExtra(DeviceConstant.INTENT_DEVICE_TYPE, Device.DISPENSER.getType());
        intent.putExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHOOSE_DISPENSER);
        intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
        startActivity(intent);
    }

    private void gotoChooseDryer() {
        Intent intent = new Intent(this, ChooseDispenserActivity.class);
        intent.putExtra(DeviceConstant.INTENT_DEVICE_TYPE, Device.DRYER.getType());
        intent.putExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHOOSE_DRYER);
        intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
        startActivity(intent);
    }


    @Override
    public void gotoDevice(Device device, String macAddress, Long supplierId,
                           String location, Long residenceId, boolean recovery) {
        Log.d(TAG, "gotoDevice: " + device.getDesc() + "->" + macAddress + "->" + supplierId + "->" + location + "->" + residenceId);
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
        android.util.Log.e(TAG, "showBanners: " + (defaultBanners == null) );
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
            } else if (business.getBusinessId() == 3) {
                dryerOrderSize = business.getPrepayOrder();
            }else if (business.getBusinessId() == Constant.PUB_BATH){
                presenter.currentOrder();
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
        orderPreInfo.setPrice(data.getPrice());
        // 2小时内存在未找零订单
        if (data.getExistsUnsettledOrder() != null && data.getExistsUnsettledOrder()) {
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
        } else {
            if (type == Device.HEATER.getType() && heaterOrderSize > 0) {
                showPrepayDialog(type, heaterOrderSize, data);
            } else if (type == Device.DISPENSER.getType() && dispenserOrderSize > 0) {
                showPrepayDialog(type, dispenserOrderSize, data);
            } else if (type == Device.DRYER.getType() && dryerOrderSize > 0) {
                showPrepayDialog(type, dryerOrderSize, data);
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
                        presenter.gotoHeaterDevice(data.getDefaultMacAddress(),
                                data.getDefaultSupplierId(), data.getLocation(),
                                data.getResidenceId());
                    } else if (type == Device.DISPENSER.getType()) {
                        // 进入饮水机选择页面
                        gotoChooseDispenser();
                    } else if (type == Device.DRYER.getType()) {
                        // 进入吹风机选择页面 复用饮水机选择页面
                        gotoChooseDryer();
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
        availabilityDialog.setTitle(AvailabilityDialog.Type.BIND_DORMITORY.getTitle());
        availabilityDialog.setTip("热水澡需要先绑定宿舍");
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
        availabilityDialog.setTitle(AvailabilityDialog.Type.NO_DEVICE.getTitle());
        availabilityDialog.setOkText("前往设置");
        availabilityDialog.setTip("请确认宿舍信息，或更换宿舍");
        availabilityDialog.setOnOkClickListener(dialog1 ->
                startActivity(new Intent(MainActivity.this, EditDormitoryActivity.class)));
        availabilityDialog.show();
    }

    @Override
    public void refreshProfile(PersonalExtraInfoDTO data) {
        lastRepairTime = data.getLastRepairTime();
        presenter.setBalance(df.format(data.getAllBalance()));
        presenter.setBonusAmount(data.getBonusAmount());
        presenter.setCredits(data.getCredits());
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

    @Override
    public String getAndroidId() {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;
    }

    @Override
    public String getAppVersion() {
        return AppUtils.getVersionName(this);
    }

    @Override
    public void showXOkMigrate() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llContainer.getLayoutParams();
        lp.topMargin = -ScreenUtils.dpToPxInt(this.getApplicationContext(), 11);
        llContainer.setLayoutParams(lp);
        ivXokMigrate.setVisibility(View.VISIBLE);
        ivXokMigrate.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, WebActivity.class)
                    .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_MIGRATE
                            + "?token=" + presenter.getToken()));
        });
    }

    @Override
    public void hideXOkMigrate() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llContainer.getLayoutParams();
        lp.topMargin = ScreenUtils.dpToPxInt(this.getApplicationContext(), 0);
        llContainer.setLayoutParams(lp);
        ivXokMigrate.setVisibility(View.GONE);
    }

    public void refreshProfile() {
        PersonalExtraInfoDTO data = new PersonalExtraInfoDTO();
        data.setAllBalance(Double.valueOf(presenter.getBalance()));
        data.setBonusAmount(presenter.getBonusAmount());
        data.setCredits(presenter.getCredits());
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
        if (ObjectsCompat.equals(type, Device.DRYER.getType())) {
            prepayDialog.setCancelText("继续使用");
        } else {
            prepayDialog.setCancelText("继续用水");
        }
        prepayDialog.setDeviceTypeAndPrepaySize(type, prepaySize);
        prepayDialog.setOnOkClickListener(dialog -> startActivity(new Intent(MainActivity.this, PrepayActivity.class)));
        prepayDialog.setOnCancelClickListener(dialog -> {
            if (data.getTimeValid()) {
                // 1 表示热水澡 2 表示饮水机
                if (type == Device.HEATER.getType()) {
                    presenter.gotoHeaterDevice(data.getDefaultMacAddress(),
                            data.getDefaultSupplierId(), data.getLocation(),
                            data.getResidenceId());
                } else if (type == Device.DISPENSER.getType()) {
                    gotoChooseDispenser();
                } else if (type == Device.DRYER.getType()) {
                    gotoChooseDryer();
                }
            } else {
                showTimeValidDialog(type, data);
            }
        });
        prepayDialog.show();
    }

    @Override
    public void startActivity(AppCompatActivity activity, Class<?> clazz) {
        Map<String, Long> extraMap = new HashMap<String, Long>(1) {
            {
                put(RepairActivity.INTENT_KEY_LAST_REPAIR_TIME, lastRepairTime);
            }
        };
        super.startActivity(activity, clazz, extraMap);
    }

    /**
     * 设备用水检查
     * @param device 设备类型
     */
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
        presenter.routeHeaterOrBathroom();
    }

    @Override
    public void routeToRoomShower(BathRouteRespDTO dto) {
        setBleCallback(() -> checkDeviceUsage(HEATER));
        getBlePermission();
    }




    @Override
    public void routeToBathroomShower(BathRouteRespDTO dto) {
        startActivity(new Intent(this, ChooseBathroomActivity.class)
        .putExtra(KEY_BUILDING_ID ,dto.getBuildingId())
        .putExtra(KEY_RESIDENCE_TYPE , dto.getResidenceType() )
        .putExtra(KEY_RESIDENCE_ID ,dto.getResidenceId()));
    }

    @Override
    public void choseBathroomAddress() {
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
        availabilityDialog.setTitle(AvailabilityDialog.Type.BIND_DORMITORY.getTitle());
        availabilityDialog.setTip("热水澡需要先绑定洗澡地址");
        availabilityDialog.setOnOkClickListener(dialog1 -> {
            Intent intent;
            intent = new Intent(this, ListChooseActivity.class);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_BUILDING);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.MAIN_ACTIVITY_BATHROOM_SRC);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
            startActivity(intent);
        });
        availabilityDialog.show();
//        startActivity(new Intent(this , ListChooseActivity.class));
    }

    @Override
    public void gotoCompleteInfoActivity(BathRouteRespDTO dto) {
        //跳转到配置信息页面
        startActivity(new Intent(this, CompleteInfoActivity.class)
                .putExtra(CompleteInfoActivity.KEY_BATHROUTERESPDTO ,dto));
    }

    @Override
    public void currentOrder(CurrentBathOrderRespDTO dto) {
        EventBus.getDefault().post(dto);
    }

    @Override
    public void startToBathroomShower() {
        startActivity(new Intent(this, ChooseBathroomActivity.class)
                .putExtra(KEY_BUILDING_ID ,1L)
                .putExtra(KEY_RESIDENCE_TYPE , 1L )
                .putExtra(KEY_RESIDENCE_ID ,1L));
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
     * 点击进入吹风机页面
     */
    private void gotoDryer() {
        Log.d(TAG, "gotoDryer");
        setBleCallback(() -> checkDeviceUsage(DRYER));
        getBlePermission();
    }


    /**
     * 点击进入失物招领
     */
    public void gotoLostAndFound() {
        Log.d(TAG, "gotoLostAndFound");
        startActivity(this, LostAndFoundActivity2.class);
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
        Log.d(TAG, "onEvent: " + event.getType());
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
            case GOTO_DRYER:
                if (checkLogin()) {
                    gotoDryer();
                }
                break;
            case GOTO_WASHER:
                if (checkLogin()) {
                    gotoWasher();
                }
                break;
            case GOTO_GATE:
                if (checkLogin()) {
                    gotoGate();
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
            default:
                break;
        }
    }

    private void gotoGate() {
        startActivity(new Intent(this, WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_GATE
                        + "?token=" + presenter.getToken()));
    }

    private void gotoWasher() {
        startActivity(new Intent(this, WasherActivity.class));
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
            /**
             * 跳转到热水澡
             */
            GOTO_HEATER(),
            /**
             * 跳转到吹风机
             */
            GOTO_DRYER(),
            /**
             * 跳转到饮水机
             */
            GOTO_DISPENSER(),
            /**
             * 跳转到失物招领
             */
            GOTO_LOST_AND_FOUND(),
            /**
             * 跳转到洗衣机
             */
            GOTO_WASHER(),
            /**
             * 跳转到门禁卡
             */
            GOTO_GATE(),
            /**
             * 跳转页面
             */
            START_ACTIVITY(),
            /**
             * 刷新通知
             */
            REFRESH_NOTICE(),
            /**
             * 退出登录
             */
            LOGOUT()
        }
    }

//    private static class HandlerExtension extends Handler {
//        WeakReference<MainActivity> mActivity;
//
//        HandlerExtension(MainActivity activity) {
//            mActivity = new WeakReference<MainActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            MainActivity theActivity = mActivity.get();
//            if (theActivity == null) {
//                theActivity = new MainActivity();
//            }
//            if (msg != null) {
//                Log.d("TPush", msg.obj.toString());
////                TextView textView = (TextView) theActivity
////                        .findViewById(R.id.deviceToken);
////                textView.setText(XGPushConfig.getToken(theActivity));
//            }
//            // XGPushManager.registerCustomNotification(theActivity,
//            // "BACKSTREET", "BOYS", System.currentTimeMillis() + 5000, 0);
//        }
//    }

}
