package com.xiaolian.amigo.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.ui.main.adaptor.HomeBannerDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeNormalDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeSmallDelegate;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.widget.RecyclerItemClickListener;
import com.xiaolian.amigo.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lombok.Data;

/**
 * 主页
 *
 * @author yik
 * @date 17/9/5
 */

public class HomeFragment2 extends BaseFragment {
    private IMainPresenter<IMainView> presenter ;


    @SuppressLint("ValidFragment")
    public HomeFragment2(IMainPresenter<IMainView> presenter){
        this.presenter = presenter ;
    }

    public HomeFragment2(){} ;

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
    HomeAdaptor.ItemWrapper lost = new HomeAdaptor.ItemWrapper(HomeAdaptor.SMALL_TYPE,
            null, "失物招领", "LOST AND FOUND",
            R.drawable.lost, R.drawable.small_lost);

    List<HomeAdaptor.ItemWrapper> items = new ArrayList<HomeAdaptor.ItemWrapper>() {
        {
//            add(shower);
//            add(water);
            add(lost);
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
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_home_slide_left_to_right);
        recyclerView.setLayoutAnimation(animation);
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



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG ,hidden+"");
        if (!hidden) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onBannerEvent(List<BannerDTO> banners) {
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
        if (recyclerView.getAdapter() == null) {
            resetItem();
            recyclerView.setAdapter(adaptor);
            recyclerView.setVisibility(View.VISIBLE);
            LayoutAnimationController animation = AnimationUtils
                    .loadLayoutAnimation(getContext(), R.anim.layout_animation_home_slide_left_to_right);
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500);
            recyclerView.startAnimation(animation.getAnimation());
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
//        if (lost.isActive()) {
//            items.add(lost);
//        }
        items.add(lost);
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
        lost.setActive(true);
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
        lost.setType(HomeAdaptor.NORMAL_TYPE);
    }

    private void switchSmallLayout() {
        gridLayoutManager.setSpanCount(2);
        shower.setType(HomeAdaptor.SMALL_TYPE);
        dryer.setType(HomeAdaptor.SMALL_TYPE);
        water.setType(HomeAdaptor.SMALL_TYPE);
        washer.setType(HomeAdaptor.SMALL_TYPE);
        gate.setType(HomeAdaptor.SMALL_TYPE);
        lost.setType(HomeAdaptor.SMALL_TYPE);
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
}
