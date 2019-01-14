package com.xiaolian.amigo.ui.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.xiaolian.amigo.BuildConfig;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.DispenserCategory;
import com.xiaolian.amigo.data.enumeration.DispenserWater;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.notify.RollingNotifyRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.ui.base.RxBus;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.DeviceConstant;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity;
import com.xiaolian.amigo.ui.device.dispenser.ChooseDispenserActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.dryer.DryerActivity;
import com.xiaolian.amigo.ui.device.washer.WasherActivity2;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2;
import com.xiaolian.amigo.ui.lostandfound.SocalFragment;
import com.xiaolian.amigo.ui.lostandfound.WriteLZActivity;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.main.update.IVersionModel;
import com.xiaolian.amigo.ui.main.update.IntentKey;
import com.xiaolian.amigo.ui.main.update.UpdateActivity;
import com.xiaolian.amigo.ui.repair.RepairActivity;
import com.xiaolian.amigo.ui.service.BleCountService;
import com.xiaolian.amigo.ui.user.CompleteInfoActivity;
import com.xiaolian.amigo.ui.user.EditDormitoryActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.ui.wallet.PrepayActivity;
import com.xiaolian.amigo.ui.widget.dialog.AvailabilityDialog;
import com.xiaolian.amigo.ui.widget.dialog.NoticeAlertDialog;
import com.xiaolian.amigo.ui.widget.dialog.PrepayDialog;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.MD5Util;
import com.xiaolian.amigo.util.MyInterpolator;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Data;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static com.xiaolian.amigo.data.enumeration.Device.DISPENSER;
import static com.xiaolian.amigo.data.enumeration.Device.DRYER;
import static com.xiaolian.amigo.data.enumeration.Device.HEATER;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_BUILDING_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_NAME;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_TYPE;
import static com.xiaolian.amigo.util.Log.getContext;

/**
 * 首页
 *
 * @author yik
 * @date 17/9/5
 */

public class MainActivity extends MainBaseActivity implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int WRITE_BLOG = 0x11 ;
    public static final String INTENT_KEY_MAC_ADDRESS = "intent_key_mac_address";
    public static final String INTENT_KEY_LOCATION = "intent_key_location";
    public static final String INTENT_KEY_SUPPLIER_ID = "intent_key_supplier_id";
    public static final String INTENT_KEY_DEVICE_TYPE = "intent_key_device_type";
    public static final String INTENT_KEY_RESIDENCE_ID = "intent_key_residence_id";
    public static final String INTENT_KEY_RECOVERY = "intent_key_recovery";
    public static final String INTENT_KEY_SWITCH_TO_HOME = "intent_key_switch_to_home";
    public static final String INTENT_KEY_SERVER_ERROR = "intent_key_server_error";
    public static final String INTENT_KEY_BANNERS = "intent_key_banners";

    // 保存上一个点击的fragment
    private static final String KEY_LASTFRAGMENT ="KEY_LAST_FRAGMENT" ;

    @Inject
    IMainPresenter<IMainView> presenter;
    @BindView(R.id.home_image)
    ImageView homeImage;
    @BindView(R.id.home_rl)
    RelativeLayout homeRl;
    @BindView(R.id.social_image)
    ImageView socialImage;
    @BindView(R.id.social_rl)
    RelativeLayout socialRl;
    @BindView(R.id.personal_image)
    ImageView personalImage;
    @BindView(R.id.personal_rl)
    RelativeLayout personalRl;
    @BindView(R.id.rl_main)
    LinearLayout rlMain;
    @BindView(R.id.social_red)
    ImageView socialRed;
    @BindView(R.id.personal_red)
    ImageView personalRed;
    @BindView(R.id.social_sel_rl)
    RelativeLayout socialSelRl;


    private DecimalFormat df = new DecimalFormat("###.##");


    @BindView(R.id.fragment)
    FrameLayout frameLayout ;


    private boolean isNotice = false;
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

    private FragmentManager fm;

    private SocalFragment socalFragment ;
    private Fragment[] fragments ;

    private Fragment fragment ;

    private int lastFragment = -1  ;

    private int nowPosition = - 1 ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.e(TAG, "onCreate: " );
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
            if (isNotice) {
                presenter.routeHeaterOrBathroom();
            }
            // 友盟日志加密
            MobclickAgent.enableEncrypt(true);
            MobclickAgent.setCatchUncaughtExceptions(true);
            if (presenter.isLogin()) {
                android.util.Log.e(TAG, "onCreate: "  );
                presenter.checkUpdate(AppUtils.getAppVersionCode(this),
                        AppUtils.getVersionName(this));
                uploadDeviceInfo();
                presenter.rollingNotify();
            }
            if (fragments == null)
            fragments = new Fragment[3];
            initTable(savedInstanceState);

            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                    } else {
                        showMessage("没有SD卡权限");
                    }
                });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK) {
            if (requestCode == WRITE_BLOG) {
                if (socalFragment != null) {
                    socalFragment.setReferTop(true);
                }
            }
        }
    }

    /**
     * 弹性动画
     */
    private void springAnimator(View view){
        int normalHeight = ScreenUtils.dpToPxInt(this ,30);
        ValueAnimator animator2 = ValueAnimator.ofInt(ScreenUtils.dpToPxInt(this ,5),ScreenUtils.dpToPxInt(this ,30) , ScreenUtils.dpToPxInt(this ,65) , ScreenUtils.dpToPxInt(this ,80),ScreenUtils.dpToPxInt(this ,65)
                                                         ,ScreenUtils.dpToPxInt(this ,70) ,ScreenUtils.dpToPxInt(this ,65)    );
        animator2.addUpdateListener(animation -> {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int currentValue = (int) animation.getAnimatedValue();
            params.addRule(CENTER_IN_PARENT);

            if (currentValue < normalHeight){
                params.height = currentValue ;
            }else {
                params.height = (int) ScreenUtils.dpToPx(MainActivity.this, 30);
            }
            params.width = currentValue ;
            view.setLayoutParams(params);
            view.postInvalidate();
        });

        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (fm != null){
                    if (fm.findFragmentByTag(SocalFragment.class.getSimpleName()) == null){
                        setDefalutItem(1);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator2.setDuration(300);
        animator2.setInterpolator(new MyInterpolator());

        animator2.start();

    }

    /**
     * 初始化底部导航栏
     */
    private void initTable(Bundle bundle) {
        //  获取被内存杀死时的activity保存的上一个fragment
        if (fm == null) fm = getSupportFragmentManager() ;
        if (bundle != null) {
            try {
                lastFragment = bundle.getInt(KEY_LASTFRAGMENT);
                android.util.Log.e(TAG, "initTable: " + lastFragment );
            } catch (Exception e) {
                android.util.Log.e(TAG, "initTable: " + e.getMessage() + "   isLogin >>>>>" + presenter.isLogin());
            }
        }
            // 杀死界面重新进入
            if (presenter.isLogin()) {
                if (lastFragment == -1) {
                    android.util.Log.e(TAG, "initTable:>>>>>    0 "  );
                    setDefalutItem(0);
                    tableBottomImageChange(0);
                } else {
                    android.util.Log.e(TAG, "initTable: >>>>>  " +lastFragment );
                    setDefalutItem(lastFragment);
                    tableBottomImageChange(lastFragment);
                }
            } else {
                android.util.Log.e(TAG, "initTable: >>>>>>  2 "  );
                setDefalutItem(2);
                tableBottomImageChange(2);
            }
        }

    /**
     * 设置默认选择底部那个模块
     *
     * @param position
     */
    private void setDefalutItem(int position) {
        android.util.Log.e(TAG, "setDefalutItem: " + position );
        if (position == -1 || position > 2) return ;
        // 目标Fragment
        Fragment targetFragment = null ;

        // 上一个显示的Tag
        Fragment last ;
        if (fm == null) fm =getSupportFragmentManager() ;
        if (position == lastFragment){

            //  内存不足时，Fragment的Presenter 变量没有保存，所以需要我们判断给他set进去
            switch (position){
                case 0:
                    targetFragment = fm.findFragmentByTag(HomeFragment2.class.getSimpleName());
                     if (targetFragment != null){
                         if (((HomeFragment2)targetFragment).presenter == null && presenter != null){
                             ((HomeFragment2)targetFragment).setPresenter(presenter);
                         }
                     }
                     break;
                case 1:
                    targetFragment = fm.findFragmentByTag(SocalFragment.class.getSimpleName());
                    if (targetFragment != null){
                        if (((SocalFragment)targetFragment).mainPresenter == null && presenter != null){
                            ((SocalFragment)targetFragment).setPresenter(presenter);
                        }
                    }
                    break;
                case 2:
                    targetFragment = fm.findFragmentByTag(ProfileFragment2.class.getSimpleName());
                    if (targetFragment != null){
                        if (((ProfileFragment2)targetFragment).presenter == null && presenter != null){
                            ((ProfileFragment2)targetFragment).setPresenter(presenter);
                        }
                    }
                    break;
                    default:
                        break;
            }

        }else {

            //  获取上一个Fragment ； 因为Fragment添加时设置了Tag，所以用findFragmentByTag获取。

            switch (lastFragment){
                case 0:
                    last =fm.findFragmentByTag(HomeFragment2.class.getSimpleName()) ;
                    break;
                case 1:
                    last = fm.findFragmentByTag(SocalFragment.class.getSimpleName());
                    break;
                case 2:
                    last = fm.findFragmentByTag(ProfileFragment2.class.getSimpleName());
                    break;
                    default:
                        last = null;
            }
            if (position == 0) {
                if (fm.findFragmentByTag(HomeFragment2.class.getSimpleName()) == null) {
                    fragment = new HomeFragment2(presenter, isServerError);
                    switchFragment(fragment ,last ,HomeFragment2.class.getSimpleName());

                } else {
                    fragment = fm.findFragmentByTag(HomeFragment2.class.getSimpleName());
                    if (fragment instanceof HomeFragment2) {
                        if (((HomeFragment2) fragment).presenter == null) {
                            if (presenter != null)
                                ((HomeFragment2) fragment).setPresenter(presenter);
                        }
                    }
                    switchFragment(fragment ,last ,HomeFragment2.class.getSimpleName());
                }
            }
            if (position == 1) {
                if (fm.findFragmentByTag(SocalFragment.class.getSimpleName()) == null) {
                    socalFragment = new SocalFragment(presenter);
                    fragment = socalFragment ;
                    switchFragment(fragment ,last ,SocalFragment.class.getSimpleName());
                } else {
                    fragment = fm.findFragmentByTag(SocalFragment.class.getSimpleName());
                    socalFragment = (SocalFragment) fragment;
                    if (socalFragment.mainPresenter == null) {
                        if (presenter != null) socalFragment.setPresenter(presenter);
                    }
                    switchFragment(socalFragment ,last ,SocalFragment.class.getSimpleName());
                }
            }

            if (position == 2) {
                if (fm.findFragmentByTag(ProfileFragment2.class.getSimpleName()) == null) {
                    fragment = new ProfileFragment2(presenter, isServerError);
                    switchFragment(fragment , last ,ProfileFragment2.class.getSimpleName());
                } else {
                    fragment = fm.findFragmentByTag(ProfileFragment2.class.getSimpleName());
                    if (fragment instanceof ProfileFragment2) {
                        if (((ProfileFragment2) fragment).presenter == null && presenter != null)
                            ((ProfileFragment2) fragment).setPresenter(presenter);
                    }
                    switchFragment(fragment , last ,ProfileFragment2.class.getSimpleName());
                }
            }
            lastFragment = position;
        }
    }
    /**
     * 隐藏上一个界面，显示目标界面
     * @param target   目标界面
     * @param last     上一个界面
     * @param targetTag   目标tag
     */
    private void switchFragment( Fragment target,Fragment last , String targetTag){

        FragmentTransaction transaction = fm.beginTransaction() ;
        // 隐藏上一个界面
        if (last != null && last.isAdded() && !last.isHidden()){
           transaction.hide(last);
        }

        //  显示targetFragment

        if (target != null){
            if (target.isAdded()){
                transaction.show(target);
            }else{
                transaction.add(R.id.fragment ,target , targetTag);
            }
        }

        transaction.commit() ;

    }

    /**
     * 底部按钮的变化
     * @param position
     */
    private void tableBottomImageChange(int position) {
        android.util.Log.e(TAG, "tableBottomImageChange: " + position );
        if (position == -1 || position > 2) return ;
        nowPosition = position ;
        if (position == 0) {
            homeImage.setImageResource(R.drawable.tab_home_sel);
        } else {
            homeImage.setImageResource(R.drawable.tab_home_nor);
        }

        if (position == 1) {
            socialSelRl.setVisibility(View.VISIBLE);
            socialImage.setVisibility(View.GONE);
            socialRed.setVisibility(View.GONE);
            springAnimator(socialSelRl);
        } else {
            socialSelRl.setVisibility(View.GONE);
            socialImage.setVisibility(View.VISIBLE);
            if (presenter.getNoticeCount() > 0 && presenter.getCommentEnable()){
                socialRed.setVisibility(View.VISIBLE);
            }else {
                socialRed.setVisibility(View.GONE);
            }
        }

        if (position == 2) {
            personalImage.setImageResource(R.drawable.tab_personal_sel);
            personalRed.setVisibility(View.GONE);
        } else {
            personalImage.setImageResource(R.drawable.tab_personal_nor);
        }
    }


    @OnClick({R.id.home_rl, R.id.social_rl, R.id.personal_rl})
    public void onTabItemSelect(View view) {

        switch (view.getId()) {
            case R.id.home_rl:
                tableBottomImageChange(0);
                setDefalutItem(0);
                break;
            case R.id.social_rl:
                if (socialSelRl.getVisibility() == View.VISIBLE){
                    startActivityForResult(new Intent(this , WriteLZActivity.class) ,WRITE_BLOG);
                }else {
                    tableBottomImageChange(1);
                    if (fm.findFragmentByTag(SocalFragment.class.getSimpleName()) != null) {
                        setDefalutItem(1);
                    }
                }
                break;
            case R.id.personal_rl:
                tableBottomImageChange(2);
                setDefalutItem(2);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (presenter.isLogin()) {
            android.util.Log.e(TAG, "onNewIntent: " );
            presenter.checkUpdate(AppUtils.getAppVersionCode(this),
                    AppUtils.getVersionName(this));
            uploadDeviceInfo();
            if (presenter.getIsFirstAfterLogin()) {
                if (fm == null) fm = getSupportFragmentManager() ;
                presenter.setIsFirstAfterLogin(false);
                SocalFragment socalFragment = (SocalFragment) fm.findFragmentByTag(SocalFragment.class.getSimpleName());
                if (socalFragment == null ) {
                    setDefalutItem(0);
                    tableBottomImageChange(0);
                    return;
                }
                if (socalFragment.isAdded()) {
                    // commit  方法在activity 的onSaveInstanceState()之后调用会报错 。 解决方法是吧commit 换成 commitAllowingStateLoss();
                    fm.beginTransaction().remove(socalFragment).commitAllowingStateLoss();
                }
                setDefalutItem(0);
                tableBottomImageChange(0);

            }
            presenter.rollingNotify();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lastFragment != -1 ) {
            outState.putInt(KEY_LASTFRAGMENT, lastFragment);
        }
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isServerError = getIntent().getBooleanExtra(INTENT_KEY_SERVER_ERROR, false);
            defaultBanners = getIntent().getParcelableArrayListExtra(INTENT_KEY_BANNERS);
            isNotice = getIntent().getBooleanExtra(Constant.BUNDLE_ID, false);
        }
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
        XGPushManager.bindAccount(getApplicationContext(),
                pushAccount,
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
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
//       String token  =  XGPushConfig.getToken(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentInit();

    }

    /**
     * 首页界面初始化显示，因为是需要请求网络
     */
    private void FragmentInit() {
        if (!presenter.isLogin()){
            showNoticeAmount(0);
            initSchoolBiz();
            checkLogin();
        }else {
            // 注册信鸽推送
            registerXGPush();
            presenter.noticeCount();
            presenter.getSchoolBusiness();
            presenter.getNoticeAmount();
            presenter.deleteFile();
            startBleCountService();
        }

    }

    /**
     * 启动上传蓝牙统计服务
     */
    private void startBleCountService(){
        Intent intent = new Intent(this , BleCountService.class);
        startService(intent);
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
     * 显示通知个数
     *
     * @param amount 通知个数
     */
    @Override
    public void showNoticeAmount(Integer amount) {
            if (personalRed == null) return ;
            if ( (amount ==0 &&!presenter.getIsShowRepair())|| nowPosition == 2){
                personalRed.setVisibility(View.GONE);

            }else{
                personalRed.setVisibility(View.VISIBLE);
            }
        EventBus.getDefault().post(new ProfileFragment2.NoticeEvent(amount));
    }

    @Override
    public void showTimeValidDialog(int deviceType, DeviceCheckRespDTO data) {
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
        enableView();
    }

    private void gotoChooseDryer() {
        Intent intent = new Intent(this, ChooseDispenserActivity.class);
        intent.putExtra(DeviceConstant.INTENT_DEVICE_TYPE, Device.DRYER.getType());
        intent.putExtra(DeviceConstant.INTENT_KEY_ACTION, DeviceConstant.ACTION_CHOOSE_DRYER);
        intent.putExtra(WaterDeviceBaseActivity.INTENT_PREPAY_INFO, orderPreInfo);
        startActivity(intent);
        enableView();
    }


    @Override
    public void gotoDevice(Device device, String macAddress, Long supplierId,
                           String location, Long residenceId, boolean recovery) {
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
        enableView();
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

        enableView();
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
        enableView();
    }

    @Override
    public void showUrgentNotify(String content, Long id) {
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
    boolean requestBathOrder = false ;
    @Override
    public void showSchoolBiz(List<BriefSchoolBusiness> businesses) {
        if (businesses == null) {
            initSchoolBiz();
            return;
        }
        this.businesses = businesses;
        for (BriefSchoolBusiness business : businesses) {
            if (business.getBusinessId() == 1) {
                if (!business.getUsing()){
                    requestBathOrder = true ;
                    presenter.currentOrder();
                }
                heaterOrderSize = business.getPrepayOrder();
            } else if (business.getBusinessId() == 2) {
                dispenserOrderSize = business.getPrepayOrder();
            } else if (business.getBusinessId() == 3) {
                dryerOrderSize = business.getPrepayOrder();
            } else if (business.getBusinessId() == Constant.PUB_BATH) {
                if (!requestBathOrder)
                presenter.currentOrder();
            }
        }
        EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.SCHOOL_BIZ,
                businesses));

        requestBathOrder = false ;
    }

    @Override
    public void showDeviceUsageDialog(int type, DeviceCheckRespDTO data) {
        if (data == null || data.getBalance() == null
                || data.getPrepay() == null || data.getMinPrepay() == null
                || data.getTimeValid() == null) {
            onError("服务器飞走啦，努力修复中");
            enableView();
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
    public void showOpenLocationDialog() {
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
        enableView();
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
        availabilityDialog.setTip("你默认的洗澡地址无设备");
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
        postReadCount(data.getUnReadWorkOrderRemarkMessageCount());
    }

    /**
     * 服务入口未读数量
     * @param unReadCount
     */
    private void postReadCount(int unReadCount){
        EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.UNREAD_COUNT,
                unReadCount));
    }

    @Override
    public void showUpdateDialog(IVersionModel model) {
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
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llContainer.getLayoutParams();
//        lp.topMargin = -ScreenUtils.dpToPxInt(this.getApplicationContext(), 11);
//        llContainer.setLayoutParams(lp);
//        ivXokMigrate.setVisibility(View.VISIBLE);
//        ivXokMigrate.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, WebActivity.class)
//                    .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_MIGRATE
//                            + "?token=" + presenter.getToken()));
//        });
    }

    @Override
    public void hideXOkMigrate() {
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llContainer.getLayoutParams();
//        lp.topMargin = ScreenUtils.dpToPxInt(this.getApplicationContext(), 0);
//        llContainer.setLayoutParams(lp);
//        ivXokMigrate.setVisibility(View.GONE);
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
     *
     * @param device 设备类型
     */
    public void checkDeviceUsage(Device device) {
        Log.d(TAG, "checkDeviceUsage");
        presenter.checkDeviceUsage(device.getType());
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
        enableView();
        setBleCallback(() -> checkDeviceUsage(HEATER));
        getBlePermission();

    }


    @Override
    public void routeToBathroomShower(BathRouteRespDTO dto) {
        enableView();
        startActivity(new Intent(this, ChooseBathroomActivity.class)
                .putExtra(KEY_BUILDING_ID, dto.getBuildingId())
                .putExtra(KEY_RESIDENCE_TYPE, dto.getResidenceType())
                .putExtra(KEY_RESIDENCE_NAME, dto.getResidenceName())
                .putExtra(KEY_RESIDENCE_ID, dto.getResidenceId()));
    }


    @Deprecated
    @Override
    public void choseBathroomAddress() {
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
        enableView();
        startActivity(new Intent(this, CompleteInfoActivity.class)
                .putExtra(CompleteInfoActivity.KEY_BATHROUTERESPDTO, dto));
    }

    @Override
    public void currentOrder(CurrentBathOrderRespDTO dto) {
        EventBus.getDefault().post(dto);
    }

    @Override
    public void showNoticeRemind() {
        if (nowPosition  != 1  && presenter.getCommentEnable()) socialRed.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoticeRemind() {
        socialRed.setVisibility(View.GONE);
    }

    @Override
    public void setCertificationStatus(PersonalExtraInfoDTO personalExtraInfoDTO) {
        RxBus.getDefault().post(personalExtraInfoDTO);
    }

    @Override
    public void startNet() {
        RxHelper.delay(300 , TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    presenter.checkUpdate(AppUtils.getAppVersionCode(this),
                            AppUtils.getVersionName(this));
                });

    }
    
    @Override
    public void enableView() {
        EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
    }

    @Override
    public void showRollingNotify(RollingNotifyRespDTO data) {
        if (data.getRollingNotifyList() == null || data.getRollingNotifyList().size() == 0) return ;

        EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ROLLING_NOTIFY ,data.getRollingNotifyList()));
    }

    @Override
    public void showOrHideBlogFragment(boolean canShowBlogFragment) {
        if (canShowBlogFragment){
            socialRl.setVisibility(View.VISIBLE);
        }else{
            socialRl.setVisibility(View.GONE);
        }
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
        super.onStart();
        EventBus.getDefault().register(this);

    }


    private boolean checkLogin() {
        if (TextUtils.isEmpty(presenter.getAccessToken()) || TextUtils.isEmpty(presenter.getRefreshToken())) {
            redirectToLogin();
            return false;
        }
        return true;
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
                enableView();
                break;
            case GOTO_DRAYER2:

                if (checkLogin()){
                    gotoDryer2();
                }
                enableView();
                break;
            case GOTO_GATE:

                if (checkLogin()) {
                    gotoGate();
                }
                enableView();
                break;
            case GOTO_LOST_AND_FOUND:

                if (checkLogin()) {
                    gotoLostAndFound();
                }
                enableView();
                break;
            case START_ACTIVITY:
                if (checkLogin()) {
                    if (event.getObject() != null) {
                        startActivity(this, (Class) event.getObject());
                    }else{
                        //  跳服务中心h5页面

                    }
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
                        + "?accessToken=" + presenter.getAccessToken() +"&refreshToken" + presenter.getRefreshToken()));
    }

    private void gotoWasher() {
       // startActivity(new Intent(this, WasherActivity.class));
        Intent intent = new Intent(this,WebActivity.class);
        String url = BuildConfig.H5_SERVER +"/washer" + "?accessToken=" + presenter.getAccessToken()+"&refreshToken=" +presenter.getRefreshToken() + "&schoolId=" + presenter.getSchoolId();
        intent.putExtra(WebActivity.INTENT_KEY_WASHER_URL,url);
        startActivity(intent);
    }

    private void gotoDryer2(){
        startActivity(new Intent(this, WasherActivity2.class));
    }


    @Data
    public static class Event {
        EventType type;
        Object object;
        Integer certificationType ;

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
             * 跳转到烘干机
             */
            GOTO_DRAYER2(),
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

}
