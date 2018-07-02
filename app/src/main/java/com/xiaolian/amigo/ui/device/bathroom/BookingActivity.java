package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.SpaceBottomItemDecoration;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 预约使用
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingActivity extends BathroomBaseActivity implements IBookingView {

    @Inject
    IBookingPresenter<IBookingView> presenter;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_sub_title)
    TextView tvToolbarSubTitle;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_sub_title)
    TextView tvSubTitle;

    @BindView(R.id.view_line)
    View viewLine;

    @BindView(R.id.statusView)
    BathroomOperationStatusView statusView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    private List<DeviceInfoAdapter.DeviceInfoWrapper> items = new ArrayList<DeviceInfoAdapter.DeviceInfoWrapper>() {
        {
            add(new DeviceInfoAdapter.DeviceInfoWrapper("浴室位置：",
                    "五楼215", R.color.colorDark2, 14, Typeface.NORMAL, false));
            add(new DeviceInfoAdapter.DeviceInfoWrapper("预留时间：",
                    "15分钟", R.color.colorDark2, 14, Typeface.NORMAL, false));
            add(new DeviceInfoAdapter.DeviceInfoWrapper("预付金额：",
                    "10元", R.color.colorDark2, 14, Typeface.NORMAL, false));
            add(new DeviceInfoAdapter.DeviceInfoWrapper("红包抵扣：",
                    "2元新人有礼", R.color.colorDark2, 14, Typeface.NORMAL, true));
        }
    };

    private DeviceInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);

        initView();
    }

    private void initView() {

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                tvTitle.setVisibility(View.VISIBLE);
                tvSubTitle.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
                tvSubTitle.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        });

        initRecyclerView();
    }

    @OnClick({R.id.tv_toolbar_sub_title, R.id.tv_sub_title})
    public void onSubtitleClick() {
        startActivity(new Intent(this, BookingRecordActivity.class));
    }

    private void initRecyclerView() {
        adapter = new DeviceInfoAdapter(this, R.layout.item_bathroom_device_info, items);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 1)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setTitleVisible(int visible) {
        tvTitle.setVisibility(visible);
        viewLine.setVisibility(visible);
    }
}
