package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/1
 */
public interface IMonthlyBillView extends IBaseView {
    void render(UserMonthlyBillRespDTO data);
}
