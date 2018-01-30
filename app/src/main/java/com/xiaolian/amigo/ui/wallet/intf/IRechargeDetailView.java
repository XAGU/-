package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 充值详情
 *
 * @author zcd
 * @date 17/10/23
 */

public interface IRechargeDetailView extends IBaseView {
    /**
     * 显示账单详情
     *
     * @param data 充值账单
     */
    void render(FundsDTO data);

    /**
     * 投诉
     */
    void toComplaint();
}
