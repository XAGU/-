package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.wallet.adaptor.RechargeAdaptor;

import java.util.List;

/**
 * 充值
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IRechargeView extends IBaseView {
    void addMore(List<RechargeAdaptor.RechargeWapper> rechargeWappers);

    void back();
}
