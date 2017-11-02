package com.xiaolian.amigo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.aspect.SingleClick;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.ui.main.adaptor.HomeBannerDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeNormalDelegate;
import com.xiaolian.amigo.ui.widget.RecyclerItemClickListener;
import com.xiaolian.amigo.util.CommonUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * Created by yik on 2017/9/5.
 */

public class HomeFragment2 extends Fragment {

    private static final String TAG = HomeFragment2.class.getSimpleName();
    HomeAdaptor.ItemWrapper shower = new HomeAdaptor.ItemWrapper(1, null, "热水澡", "TAKE A SHOWER", "#ffb6c5",
                    R.drawable.shower);
    HomeAdaptor.ItemWrapper water = new HomeAdaptor.ItemWrapper(1, null, "饮水机", "DRINK A WATER", "#aaebe4",
                    R.drawable.water);
    HomeAdaptor.ItemWrapper lost = new HomeAdaptor.ItemWrapper(1, null, "失物招领", "LOST AND FOUND", "#b3d4ff",
            R.drawable.lost);

    List<HomeAdaptor.ItemWrapper> items = new ArrayList<HomeAdaptor.ItemWrapper>() {
        {
            add(shower);
            add(water);
            add(lost);
        }
    };

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeAdaptor adaptor;
    // 未找零账单个数
    private int prepayOrderSize = 0;
    // 学校业务个数
    private int businessSize = 0;
    // 正在使用的设备个数
    private int usingAmount = 0;
    private View disabledView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, homeView);
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptor = new HomeAdaptor(getActivity(),items);
//        recyclerView.scheduleLayoutAnimation();
        adaptor.addItemViewDelegate(new HomeNormalDelegate());
        adaptor.addItemViewDelegate(new HomeBannerDelegate(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);
        ((DefaultItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_right);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "recycler view onItemClick " + position);
                        if (items.get(position).getType() == 1) {
                            disabledView = view;
                            view.setEnabled(false);
                            if (items.get(position).getRes() == R.drawable.shower) {
                                EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_HEATER));
                            } else if (items.get(position).getRes() == R.drawable.water) {
                                EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_DISPENSER));
                            } else if (items.get(position).getRes() == R.drawable.lost) {
                                EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.GOTO_LOST_AND_FOUND));
                            }
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
        if (!hidden) {
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onBannerEvent(List<String> banners) {
        if (items.get(items.size() - 1).getType() == 1) {
            items.add(new HomeAdaptor.ItemWrapper(2, banners, null, null, null, 0));
            adaptor.notifyDataSetChanged();
        } else {
            if (!isBannersEqual(items.get(items.size() - 1).getBanners(), banners)) {
                items.get(items.size() - 1).setBanners(banners);
                adaptor.notifyDataSetChanged();
            }
        }
    }

    public void onSchoolBizEvent(List<BriefSchoolBusiness> businesses) {
        int currentPrepayOrderSize = 0;
        int currentBusinessSize = 0;
        int currentUsingAmount = 0;
        boolean needNotify = false;
        shower.setActive(false);
        water.setActive(false);
        for (BriefSchoolBusiness business : businesses) {
            if (business.getUsing()) {
                currentUsingAmount += 1;
            }
            if (business.getBusinessId() == 2) {
                water.setActive(true);
                water.setPrepaySize(business.getPrepayOrder());
                water.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            } else if (business.getBusinessId() == 1) {
                shower.setActive(true);
                shower.setPrepaySize(business.getPrepayOrder());
                shower.setUsing(business.getUsing());
                currentPrepayOrderSize += business.getPrepayOrder();
                currentBusinessSize += 1;
            }
        }
        if (currentBusinessSize != businessSize) {
            businessSize = currentBusinessSize;
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
            adaptor.notifyDataSetChanged();
        }
    }

    private void onPrepayOrderEvent(HomeAdaptor.ItemWrapper itemWrapper) {
        boolean needNotify = false;
        for (HomeAdaptor.ItemWrapper item : items) {
            if (CommonUtil.equals(itemWrapper.getRes(), item.getRes())) {
                if (!CommonUtil.equals(item.getPrepaySize(), itemWrapper.getPrepaySize())) {
                    needNotify = true;
                    item.setPrepaySize(itemWrapper.getPrepaySize());
                }
            }
        }
        if (needNotify) {
            adaptor.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getType()) {
            case BANNER:
                onBannerEvent((List<String>) event.getObject());
                break;
            case SCHOOL_BIZ:
                onSchoolBizEvent((List<BriefSchoolBusiness>) event.getObject());
                break;
//            case PREPAY_ORDER:
//                onPrepayOrderEvent((HomeAdaptor.ItemWrapper) event.getObject());
//                break;
            case ENABLE_VIEW:
                Log.d(TAG, "enable View");
                if (!disabledView.isEnabled()) {
                    disabledView.setEnabled(true);
                }
                break;
        }
    }



    private boolean isBannersEqual(List<String> banners1, List<String> banners2) {
        if (banners1.size() != banners2.size()) {
            return false;
        }
        for (int i = 0; i < banners1.size(); i ++) {
            if (!TextUtils.equals(banners1.get(i), banners2.get(i))) {
                return false;
            }
        }
        return true;
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
            BANNER(1),
            SCHOOL_BIZ(2),
            ENABLE_VIEW(3)
            ;

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
