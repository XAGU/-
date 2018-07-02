package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 预约记录
 *
 * @author zcd
 * @date 18/6/29
 */
public class BookingRecordActivity extends BathroomBaseActivity implements IBookingRecordView {

    @Inject
    IBookingRecordPresenter<IBookingRecordView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bathroom_shower);
        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }
}
