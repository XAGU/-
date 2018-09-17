package com.xiaolian.amigo.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.device.washer.ScanActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.ui.main.adaptor.HomeBannerDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeNormalDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeSmallDelegate;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.widget.RecyclerItemClickListener;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lombok.Data;

import static com.xiaolian.amigo.ui.device.washer.ScanActivity.IS_SACN;
import static com.xiaolian.amigo.ui.device.washer.ScanActivity.SCAN_TYPE;

/**
 * 主页
 *
 * @author yik
 * @date 17/9/5
 */

public class HomeFragment2 extends BaseFragment {
    private IMainPresenter<IMainView> presenter ;
    private boolean isServerError ;

    @SuppressLint("ValidFragment")
    public HomeFragment2(IMainPresenter<IMainView> presenter , boolean isServerError){
        this.presenter = presenter ;
        this.isServerError = isServerError ;
    }

    public HomeFragment2(){}

    private static final int SMALL_LIST_FORMAT_MIN_SIZE = 3;

    private static final String TAG = HomeFragment2.class.getSimpleName();
    HomeAdaptor.ItemWrapper shower = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "热水澡", "TAKE A SHOWER",
            R.drawable.shower, R.drawable.small_shower);
    HomeAdaptor.ItemWrapper dryer = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "吹风机", "HAIR DRIER",
            R.drawable.dryer, R.drawable.small_dryer);
    HomeAdaptor.ItemWrapper washer = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "洗衣机", "WASH CLOTHES",
            R.drawable.washer, R.drawable.small_washer);
    HomeAdaptor.ItemWrapper water = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "饮水机", "DRINK A WATER",
            R.drawable.water, R.drawable.small_water);
    HomeAdaptor.ItemWrapper gate = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "门禁卡", "ACCESS CONTROL",
            R.drawable.gate, R.drawable.small_gate);
    HomeAdaptor.ItemWrapper dryer1 = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE ,
            null ,"烘干机","DRYER" ,
            R.drawable.hongganji_long , R.drawable.hongganji_short);
//    HomeAdaptor.ItemWrapper lost = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
//            null, "失物招领", "LOST AND FOUND",
//            R.drawable.lost, R.drawable.small_lost);

    List<HomeAdaptor.ItemWrapper> items = new ArrayList<HomeAdaptor.ItemWrapper>() {
        {
//            add(shower);
//            add(water);
//            add(lost);
        }
    };

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeAdaptor adaptor;
    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.scan)
    ImageView scan;
    /**
     * 未找零账单个数
     */
    private int prepayOrderSize = 0;
    /**
     * 学校业务个数
     */
    private int businessSize = 0;
    /**
     * 正在使用的设备个数
     */
    private int usingAmount = 0;
    private View disabledView;
    private HomeAdaptor.ItemWrapper banner;
    private GridLayoutManager gridLayoutManager;

    private Unbinder unbinder ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, homeView);
        initView();
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        adaptor = new HomeAdaptor(getActivity(), items, gridLayoutManager);
        adaptor.addItemViewDelegate(new HomeNormalDelegate(getActivity()));
        adaptor.addItemViewDelegate(new HomeSmallDelegate(getActivity()));
        adaptor.addItemViewDelegate(new HomeBannerDelegate(getActivity()));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, MotionEvent e) {
                        try {
                            Log.d(TAG, "recycler view onItemClick " + position);
                            if (items.get(position).getType() != HomeAdaptor.NORMAL_TYPE
                                    && items.get(position).getType() != HomeAdaptor.SMALL_TYPE) {
                                view.dispatchTouchEvent(e);
                                return;
                            }
                            if (items.get(position).getType() == HomeAdaptor.NORMAL_TYPE
                                    || items.get(position).getType() == HomeAdaptor.SMALL_TYPE) {
                                disabledView = view;
                                view.setEnabled(false);
                                if (items.get(position).getRes() == R.drawable.shower) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_HEATER));
                                } else if (items.get(position).getRes() == R.drawable.water) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_DISPENSER));
                                } else if (items.get(position).getRes() == R.drawable.lost) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_LOST_AND_FOUND));
                                } else if (items.get(position).getRes() == R.drawable.dryer) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_DRYER));
                                } else if (items.get(position).getRes() == R.drawable.washer) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_WASHER));
                                } else if (items.get(position).getRes() == R.drawable.gate) {
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_GATE));
                                }else if (items.get(position).getRes() == R.drawable.hongganji_long){
                                    EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_DRAYER2));
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            Log.wtf(TAG, "数组越界", ex);
                        } catch (Exception ex) {
                            Log.wtf(TAG, ex);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d(TAG, "recycler view onLongItemClick");
                    }
                }));
    }



    /**
     * 请求网络数据
     */
    public void requestData(){
        if (presenter == null )  return ;
        if (!isNetworkAvailable()) {
//            onError(R.string.network_available_error_tip);
            if (presenter.isLogin()) {
                presenter.getSchoolBusiness();
                // 设置学校
                if (schoolName != null) {
                    if (!presenter.getUserInfo().getSchoolName().equals(schoolName.getText().toString()))
                        schoolName.setText(presenter.getUserInfo().getSchoolName());
                }
                // 设置学校业务
                return;
            }
            initSchoolBiz();
            return;
        }
        if (!presenter.isLogin()) {
            initSchoolBiz();
            Log.d(TAG, "onResume: not login");
        } else {
            if (isServerError) {
                initSchoolBiz();
            }
            // 请求通知
            presenter.getSchoolBusiness();
            presenter.getNoticeAmount();
            if (schoolName != null) {
                if (!presenter.getUserInfo().getSchoolName().equals(schoolName.getText().toString()))
                    schoolName.setText(presenter.getUserInfo().getSchoolName());
            }
            // 设置昵称
        }
    }


    private void initSchoolBiz(){

        onEvent(new HomeFragment2.Event(HomeFragment2.Event.EventType.INIT_BIZ,
                null));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onBannerEvent(List<BannerDTO> banners) {
        if (items.size() == 0) return ;
        if (items.get(items.size() - 1).getType() == HomeAdaptor.NORMAL_TYPE ||
                items.get(items.size() - 1).getType() == HomeAdaptor.SMALL_TYPE) {
            banner = new HomeAdaptor.ItemWrapper(HomeAdaptor.BANNER_TYPE, banners, null, null, 0, 0);
            items.add(banner);
            Log.d(TAG, "onBannerEvent notify");
            adaptor.notifyItemInserted(items.size() - 1);
        } else {
            if (!isBannersEqual(items.get(items.size() - 1).getBanners(), banners)) {
                items.get(items.size() - 1).setBanners(banners);
                Log.d(TAG, "onBannerEvent notify");
                notifyAdaptor();
            }
        }
    }


    private void notifyAdaptor() {
        if (recyclerView == null) return ;
        if (recyclerView.getAdapter() == null) {
            resetItem();
            recyclerView.setAdapter(adaptor);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            resetItem();
            adaptor.notifyDataSetChanged();
        }
    }

    private void resetItem() {
        items.clear();
        if (dryer.isActive()) {
            items.add(dryer);
        }
        if (shower.isActive()) {
            items.add(shower);
        }
        if (washer.isActive()) {
            items.add(washer);
        }
        if (water.isActive()) {
            items.add(water);
        }
        if (gate.isActive()) {
            items.add(gate);
        }
        if (dryer1.isActive()){
            items.add(dryer1);
        }
//        if (lost.isActive()) {
//            items.add(lost);
//        }
//        items.add(lost);
        if (banner != null) {
            items.add(banner);
        }

    }


    public void onSchoolBizEvent(List<BriefSchoolBusiness> businesses) {
        int currentPrepayOrderSize = 0;
        int currentBusinessSize = 0;
        int currentUsingAmount = 0;
        boolean needNotify = false;
        shower.setActive(false);
        dryer.setActive(false);
        water.setActive(false);
        washer.setActive(false);
        gate.setActive(false);
        dryer1.setActive(false);
//        lost.setActive(true);
        /// business为空则不显示shower和water
        if (businesses == null || businesses.isEmpty()) {
            notifyAdaptor();
            return;
        }
        for (BriefSchoolBusiness business : businesses) {
            if (business.getUsing()) {
                currentUsingAmount += 1;
            }
            if (business.getBusinessId() == 2) {
                water.setActive(true);
                shower.setExistOrder(false);
                water.setPrepaySize(business.getPrepayOrder());
                water.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            } else if (business.getBusinessId() == 1) {
                shower.setActive(true);
                shower.setExistOrder(false);
                shower.setPrepaySize(business.getPrepayOrder());
                shower.setUsing(business.getUsing());
                shower.setStatus(0);
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
                if (shower.isExistOrder()){
                    needNotify = true ;
                    shower.setExistOrder(false);
                }
            } else if (business.getBusinessId() == 3) {
                dryer.setActive(true);
                shower.setExistOrder(false);
                dryer.setPrepaySize(business.getPrepayOrder());
                dryer.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            } else if (business.getBusinessId() == 4) {
                washer.setActive(true);
                shower.setExistOrder(false);
                washer.setPrepaySize(business.getPrepayOrder());
                washer.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            }else if (business.getBusinessId() == 6){
                dryer1.setActive(true);
                shower.setExistOrder(false);
                dryer1.setPrepaySize(business.getPrepayOrder());
                dryer1.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            } else if (business.getBusinessId() == 101) {
                gate.setActive(true);
//                gate.setPrepaySize(business.getPrepayOrder());
//                gate.setUsing(business.getUsing());
//                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            }
        }
        if (currentBusinessSize != businessSize) {
            businessSize = currentBusinessSize;
            if (businessSize >= SMALL_LIST_FORMAT_MIN_SIZE) {
                switchSmallLayout();
            } else {
                switchLargeLayout();
            }
            needNotify = true;
        }
        if (currentPrepayOrderSize != prepayOrderSize) {
            prepayOrderSize = currentPrepayOrderSize;
            needNotify = true;
        }
        if (currentUsingAmount != usingAmount) {
            usingAmount = currentUsingAmount;
            needNotify = true;
        }

        if (needNotify) {
            Log.d(TAG, "onSchoolBizEvent notify");
            notifyAdaptor();
        }
    }

    private void switchLargeLayout() {
        gridLayoutManager.setSpanCount(1);
        shower.setType(HomeAdaptor.NORMAL_TYPE);
        dryer.setType(HomeAdaptor.NORMAL_TYPE);
        water.setType(HomeAdaptor.NORMAL_TYPE);
        washer.setType(HomeAdaptor.NORMAL_TYPE);
        gate.setType(HomeAdaptor.NORMAL_TYPE);
        dryer1.setType(HomeAdaptor.NORMAL_TYPE);
//        lost.setType(HomeAdaptor.NORMAL_TYPE);
    }

    private void switchSmallLayout() {
        gridLayoutManager.setSpanCount(2);
        shower.setType(HomeAdaptor.SMALL_TYPE);
        dryer.setType(HomeAdaptor.SMALL_TYPE);
        water.setType(HomeAdaptor.SMALL_TYPE);
        washer.setType(HomeAdaptor.SMALL_TYPE);
        gate.setType(HomeAdaptor.SMALL_TYPE);
        dryer1.setType(HomeAdaptor.SMALL_TYPE);
//        lost.setType(HomeAdaptor.SMALL_TYPE);
    }



    private void onPrepayOrderEvent(HomeAdaptor.ItemWrapper itemWrapper) {
        boolean needNotify = false;
        for (HomeAdaptor.ItemWrapper item : items) {
            if (ObjectsCompat.equals(itemWrapper.getRes(), item.getRes())) {
                if (!ObjectsCompat.equals(item.getPrepaySize(), itemWrapper.getPrepaySize())) {
                    needNotify = true;
                    item.setPrepaySize(itemWrapper.getPrepaySize());
                }
            }
        }
        if (needNotify) {
            Log.d(TAG, "onPrepayOrderEvent notify");
            notifyAdaptor();
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getType()) {
            case BANNER:
                onBannerEvent((List<BannerDTO>) event.getObject());
                break;
            case SCHOOL_BIZ:
                onSchoolBizEvent((List<BriefSchoolBusiness>) event.getObject());
                break;
            case ENABLE_VIEW:
                Log.d(TAG, "enable View");
                if (!disabledView.isEnabled()) {
                    disabledView.setEnabled(true);
                }
                break;
            case CHANGE_ANIMATION:
                int animationRes = (int) event.getObject();
                changeAnimation(animationRes);
                break;
            case INIT_BIZ:
                notifyAdaptor();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OrderEvent(CurrentBathOrderRespDTO event) {
        checkTitleTip(event);
    }

    /**
     * 显示主页面是否有上一订单
     *
     * @param event
     */
    private void checkTitleTip(CurrentBathOrderRespDTO event) {
        shower.setExistOrder(true);
        shower.setStatus(event.getStatus());
        if (shower.getStatus() == 1) {
            shower.setUsing(false);
        } else {
            shower.setUsing(true);
        }
        Log.e(TAG, "checkTitleTip预约订单" + event.getStatus());
        notifyAdaptor();
    }


    private void changeAnimation(int animationRes) {
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getContext(), animationRes);
        recyclerView.setLayoutAnimation(animation);
    }


    private boolean isBannersEqual(List<BannerDTO> banners1, List<BannerDTO> banners2) {
        if (banners1 == null || banners2 == null) {
            return false;
        }
        if (banners1.size() != banners2.size()) {
            return false;
        }
        for (int i = 0; i < banners1.size(); i++) {
            if (!TextUtils.equals(banners1.get(i).getImage(), banners2.get(i).getImage())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    protected void initData() {

        }

    @Override
    protected void initView() {
        requestData();
    }

    @Data
    public static class Event {
        private EventType type;
        private Object object;

        public Event(EventType type, Object object) {
            this.type = type;
            this.object = object;
        }

        public Event(EventType type) {
            this.type = type;
        }

        public enum EventType {
            /**
             * banner
             */
            BANNER(1),
            /**
             * 学校业务
             */
            SCHOOL_BIZ(2),
            /**
             * enable view
             */
            ENABLE_VIEW(3),
            /**
             * 改变动画
             */
            CHANGE_ANIMATION(4),
            /**
             * 初始化学校业务
             */
            INIT_BIZ(5);

            EventType(int type) {
                this.type = type;
            }

            private int type;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 扫一扫
     */
    @OnClick(R.id.scan)
    public void scan() {
        if (presenter.isLogin()) {
            scanQRCode();
        } else {
            startActivity(new Intent(mActivity, LoginActivity.class));
        }
    }

    private void scanQRCode() {
//        startActivity(new Intent(this, CaptureActivity.class));
        IntentIntegrator integrator = new IntentIntegrator(mActivity);
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
        integrator.addExtra(SCAN_TYPE, 2);
        integrator.addExtra(IS_SACN, true);
        integrator.initiateScan();
    }
}
