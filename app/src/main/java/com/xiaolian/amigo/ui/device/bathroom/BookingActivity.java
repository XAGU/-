package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.sv_main_container)
    NestedScrollView svMainContainer;

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.view_line)
    View viewLine;

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
        IOverScrollDecor iOverScrollDecor = OverScrollDecoratorHelper.setUpStaticOverScroll(svMainContainer,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        iOverScrollDecor.setOverScrollUpdateListener((decor, state, offset) -> {
            if (offset < -(tvToolbarTitle.getHeight()) + tvToolbarTitle.getPaddingTop()) {
                setTitleVisible(View.VISIBLE);
            } else {
                setTitleVisible(View.GONE);
            }
        });
    }

    private void setTitleVisible(int visible) {
        tvTitle.setVisibility(visible);
        viewLine.setVisibility(visible);
    }
}
