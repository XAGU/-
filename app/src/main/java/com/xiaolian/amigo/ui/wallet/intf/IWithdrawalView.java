package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.network.model.funds.QueryRechargeTypeListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 提现
 * @author zcd
 * @date 17/10/14
 */

public interface IWithdrawalView extends IBaseView {
    /**
     * 返回
     */
    void back();

    /**
     * 跳转到提现详情
     * @param id 提现账单id
     */
    void gotoWithdrawDetail(Long id);

    /**
     * 显示提现账号
     * @param accountName 账号名称
     * @param id 提现账单id
     */
    void showWithdrawAccount(String accountName, Long id);

    /**
     * 设置退款服务商微信号appId
     * @param appId
     */
    void setAppid(String appId);

    void showTypeList(QueryRechargeTypeListRespDTO data);
}
