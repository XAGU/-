package com.xiaolian.amigo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.ui.base.aspect.SingleClick;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity;
import com.xiaolian.amigo.ui.main.adaptor.HomeAdaptor;
import com.xiaolian.amigo.ui.main.adaptor.HomeBannerDelegate;
import com.xiaolian.amigo.ui.main.adaptor.HomeNormalDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

import static com.xiaolian.amigo.data.enumeration.Device.DISPENSER;
import static com.xiaolian.amigo.data.enumeration.Device.HEARTER;

/**
 * Created by yik on 2017/9/5.
 */

public class HomeFragment2 extends Fragment {

    private static final String TAG = HomeFragment2.class.getSimpleName();

    List<HomeAdaptor.ItemWrapper> items = new ArrayList<HomeAdaptor.ItemWrapper>() {
        {
            add(new HomeAdaptor.ItemWrapper(1, null, "失物招领", "LOST AND FOUND", "#b3d4ff", R.drawable.lost));
        }
    };

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    HomeAdaptor adaptor;

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
        adaptor.addItemViewDelegate(new HomeNormalDelegate());
        adaptor.addItemViewDelegate(new HomeBannerDelegate(getActivity()));
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @SingleClick
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (items.get(position).getType() == 1) {
                    if (items.get(position).getRes() == R.drawable.shower) {
                        EventBus.getDefault().post(MainActivity.Event.GOTO_HEATER);
                    } else if (items.get(position).getRes() == R.drawable.water) {
                        EventBus.getDefault().post(MainActivity.Event.GOTO_DISPENSER);
                    } else if (items.get(position).getRes() == R.drawable.lost) {
                        EventBus.getDefault().post(MainActivity.Event.GOTO_LOST_AND_FOUND);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);
        LayoutAnimationController animation = AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_slide_right);
        recyclerView.setLayoutAnimation(animation);
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
        if (businesses.isEmpty()) {
            return;
        }
        for (BriefSchoolBusiness business : businesses) {
            if (business.getBusinessId() == 2) {
                items.add(0, new HomeAdaptor.ItemWrapper(1, null, "热水器", "TAKE A SHOWER", "#ffb6c5", R.drawable.shower));
            } else if (business.getBusinessId() == 1) {
                items.add(0, new HomeAdaptor.ItemWrapper(1, null, "饮水机", "DRINK A WATER", "#aaebe4", R.drawable.water));
            }
        }
        adaptor.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getType()) {
            case BANNER:
                onBannerEvent((List<String>) event.getObject());
                break;
            case SCHOOL_BIZ:
                onSchoolBizEvent((List<BriefSchoolBusiness>) event.getObject());
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

        public enum EventType {
            BANNER(1),
            SCHOOL_BIZ(2)
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
