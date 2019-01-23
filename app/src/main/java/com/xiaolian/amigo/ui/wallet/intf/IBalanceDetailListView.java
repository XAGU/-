package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.userbill.UserMonthlyBillRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

public interface IBalanceDetailListView extends IBaseView {
    void render(UserMonthlyBillRespDTO data);
}
