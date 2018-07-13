package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.BathroomDataManager;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.di.BathroomActivityContext;
import com.xiaolian.amigo.ui.device.bathroom.BathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.BathroomScanPresenter;
import com.xiaolian.amigo.ui.device.bathroom.BookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.BookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.BuyCodePresenter;
import com.xiaolian.amigo.ui.device.bathroom.BuyRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.PayUsePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomScanPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomScanView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBathroomView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingRecordView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBookingView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyCodeView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IBuyRecordView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUsePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IPayUseView;

import dagger.Module;
import dagger.Provides;

/**
 * @author zcd
 * @date 18/6/29
 */
@Module
public class BathroomActivityModule {

    private AppCompatActivity mActivity;

    public BathroomActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    Context provideContext() {
        return mActivity;
    }

    @Provides
    @BathroomActivityContext
    IBathroomPresenter<IBathroomView> provideBathroomPresenter(
            BathroomPresenter<IBathroomView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IBookingPresenter<IBookingView> provideBookingPresenter(
            BookingPresenter<IBookingView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IBookingRecordPresenter<IBookingRecordView> provideBookingRecordPresenter(
            BookingRecordPresenter<IBookingRecordView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IBuyCodePresenter<IBuyCodeView> provideBuyCodePresenter(
            BuyCodePresenter<IBuyCodeView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IBuyRecordPresenter<IBuyRecordView> provideBuyRecordPresenter(
            BuyRecordPresenter<IBuyRecordView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IPayUsePresenter<IPayUseView> providePayUsePresenter(
            PayUsePresenter<IPayUseView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IChooseBathroomPresenter<IChooseBathroomView> provideChooseBathroomPresenter(
            ChooseBathroomPresenter<IChooseBathroomView> presenter) {
        return presenter;
    }

    @Provides
    @BathroomActivityContext
    IBathroomScanPresenter<IBathroomScanView> provideBathroomScanPresenter(
            BathroomScanPresenter<IBathroomScanView> presenter) {
        return presenter;
    }

    @Provides
    IBathroomDataManager provideBathroomDataManager(BathroomDataManager manager) {
        return manager;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }
}
