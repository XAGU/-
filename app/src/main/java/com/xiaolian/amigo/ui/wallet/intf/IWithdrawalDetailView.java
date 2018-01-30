package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.funds.FundsDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 提现详情
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IWithdrawalDetailView extends IBaseView {
    /**
     * 显示提现详情
     */
    void render(FundsDTO data);

    /**
     * 返回
     */
    void gotoBack();

    /**
     * 投诉
     */
    void toComplaint();
}
