package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.adapter.DeviceInfoAdapter;
import com.xiaolian.amigo.ui.widget.BathroomOperationStatusView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 使用公共澡堂基础类
 *
 * @author zcd
 * @date 18/7/2
 */
public abstract class UseWayActivity extends BathroomBaseActivity {

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

    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_tip1)
    TextView tvTip1;
    @BindView(R.id.tv_tip2)
    TextView tvTip2;
    @BindView(R.id.tv_tip3)
    TextView tvTip3;
    @BindView(R.id.tv_tip4)
    TextView tvTip4;

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

        setTitle(tvTitle);
        setToolbarTitle(tvToolbarTitle);
        setSubTitle(tvSubTitle);
        setToolbarSubTitle(tvToolbarSubTitle);
        setTips(tvTip1, tvTip2, tvTip3, tvTip4, tvTip, rlTip);
        initRecyclerView();
    }

    protected abstract void setToolbarTitle(TextView textView);

    protected abstract void setTitle(TextView textView);

    protected abstract void setToolbarSubTitle(TextView textView);

    protected abstract void setSubTitle(TextView textView);

    protected abstract void setTips(TextView tip1,
                                    TextView tip2,
                                    TextView tip3,
                                    TextView tip4,
                                    TextView tip,
                                    RelativeLayout rlTip);

    


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
