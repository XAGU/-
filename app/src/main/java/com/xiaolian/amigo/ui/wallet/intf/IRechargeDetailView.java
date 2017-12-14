package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 充值详情
 * <p>
 * Created by zcd on 10/23/17.
 */

public interface IRechargeDetailView extends IBaseView {
    void render(FundsDTO data);

    void toComplaint();
}
