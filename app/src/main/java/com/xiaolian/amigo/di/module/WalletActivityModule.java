/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.xiaolian.amigo.data.manager.OrderDataManager;
import com.xiaolian.amigo.data.manager.WalletDataManager;
import com.xiaolian.amigo.data.manager.intf.IOrderDataManager;
import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.di.WalletActivityContext;
import com.xiaolian.amigo.ui.wallet.AddAccountPresenter;
import com.xiaolian.amigo.ui.wallet.ChooseWithdrawPresenter;
import com.xiaolian.amigo.ui.wallet.PrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.PrepayPresenter;
import com.xiaolian.amigo.ui.wallet.RechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.RechargePresenter;
import com.xiaolian.amigo.ui.wallet.WalletPresenter;
import com.xiaolian.amigo.ui.wallet.WithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.WithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.WithdrawalRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IAddAccountView;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IChooseWithdrawView;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayOrderView;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IPrepayView;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeDetailView;
import com.xiaolian.amigo.ui.wallet.intf.IRechargePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IRechargeView;
import com.xiaolian.amigo.ui.wallet.intf.IWalletPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWalletView;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawRecordPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalDetailView;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalPresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalRecordView;
import com.xiaolian.amigo.ui.wallet.intf.IWithdrawalView;

import dagger.Module;
import dagger.Provides;

@Module
public class WalletActivityModule {

    private AppCompatActivity mActivity;

    public WalletActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @WalletActivityContext
    IWalletPresenter<IWalletView> provideWalletPresenter(
            WalletPresenter<IWalletView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IRechargePresenter<IRechargeView> provideRechargePresenter(
            RechargePresenter<IRechargeView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IPrepayPresenter<IPrepayView> providePrepayPresenter(
            PrepayPresenter<IPrepayView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IPrepayOrderPresenter<IPrepayOrderView> providePrepayOrderPresenter(
            PrepayOrderPresenter<IPrepayOrderView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IWithdrawalPresenter<IWithdrawalView> provideWithdrawPresenter(
            WithdrawalPresenter<IWithdrawalView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IWithdrawRecordPresenter<IWithdrawalRecordView> provideWithdrawRecordPresenter(
            WithdrawalRecordPresenter<IWithdrawalRecordView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IChooseWithdrawPresenter<IChooseWithdrawView> provideChooseWithdrawPresenter(
            ChooseWithdrawPresenter<IChooseWithdrawView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IAddAccountPresenter<IAddAccountView> provideAddAccountPresenter(
            AddAccountPresenter<IAddAccountView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IRechargeDetailPresenter<IRechargeDetailView> provideRechargeDetailPresenter(
            RechargeDetailPresenter<IRechargeDetailView> presenter) {
        return presenter;
    }

    @Provides
    @WalletActivityContext
    IWithdrawalDetailPresenter<IWithdrawalDetailView> provideWithdrawaleDetailPresenter(
            WithdrawalDetailPresenter<IWithdrawalDetailView> presenter) {
        return presenter;
    }

    @Provides
    IOrderDataManager provideOrderDataManager(OrderDataManager manager) {
        return manager;
    }

    @Provides
    IWalletDataManager provideWalletDataManager(WalletDataManager manager) {
        return manager;
    }
}
