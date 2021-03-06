package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.BathroomActivityContext;
import com.xiaolian.amigo.di.module.BathroomActivityModule;
import com.xiaolian.amigo.ui.device.bathroom.BathroomActivity;
import com.xiaolian.amigo.ui.device.bathroom.BathroomHeaterActivity;
import com.xiaolian.amigo.ui.device.bathroom.BathroomScanActivity;
import com.xiaolian.amigo.ui.device.bathroom.BookingActivity;
import com.xiaolian.amigo.ui.device.bathroom.BookingRecordActivity;
import com.xiaolian.amigo.ui.device.bathroom.BuyCodeActivity;
import com.xiaolian.amigo.ui.device.bathroom.BuyRecordActivity;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity;
import com.xiaolian.amigo.ui.device.bathroom.EditBathroomPasswordActivity;
import com.xiaolian.amigo.ui.device.bathroom.PayUseActivity;
import com.xiaolian.amigo.ui.device.bathroom.ScanUseActivity;

import dagger.Component;

/**
 * @author zcd
 * @date 18/6/29
 */
@BathroomActivityContext
@Component(dependencies = ApplicationComponent.class, modules = BathroomActivityModule.class)
public interface BathroomActivityComponent {

    void inject(BathroomActivity activity);

    void inject(BookingActivity activity);

    void inject(BookingRecordActivity activity);

    void inject(BuyCodeActivity activity);

    void inject(BuyRecordActivity activity);

    void inject(PayUseActivity activity);

    void inject(BathroomScanActivity activity);

    void inject(ScanUseActivity activity);

    void inject(EditBathroomPasswordActivity activity);

    void inject(ChooseBathroomActivity  activity);

    void inject(BathroomHeaterActivity activity);
}
