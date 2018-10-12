package com.xiaolian.amigo.ui.wallet;

import com.xiaolian.amigo.data.manager.intf.IWalletDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.wallet.intf.IWithDrawView;

import javax.inject.Inject;

public class WithDrawPresenter<v extends IWithDrawView> extends BasePresenter<v>
    implements  IWithDrawPresenter<v>{


    private IWalletDataManager walletDataManager ;

    @Inject
    public WithDrawPresenter(IWalletDataManager walletDataManager){
        this.walletDataManager = walletDataManager ;
    }



}
