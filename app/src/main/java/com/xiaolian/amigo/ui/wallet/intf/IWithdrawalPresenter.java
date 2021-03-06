package com.xiaolian.amigo.ui.wallet.intf;

import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 提现
 *
 * @author zcd
 * @date 17/10/14
 */

public interface IWithdrawalPresenter<V extends IWithdrawalView> extends IBasePresenter<V> {
    /**
     * 提现
     *
     * @param amount       金额
     * @param withdrawName 提现名称
     * @param withdrawId   提现id
     */
    void withdraw(String amount, String withdrawName, Long withdrawId);

    /**
     * 获取账户列表
     *
     * @param type 账户类型
     */
    void requestAccounts(int type);

    /**
     * 清除账户
     */
    void clearAccount();

    /**
     * 获取学校微信商户账号appId
     * @return
     */
    void getWechatAppid();

    /**
     * 微信提现
     */
    void wechatWithdraw(String amount ,String openId ,String userRealName , String nickName);

    /**
     * 获取退款类型
     */
    void withdrawType();

    /**
     * 获取userInfo
     */
    User getUserInfo();

    void getWeChatCode();

    /**
     * 通过授权码获取昵称
     * @param wechatCode
     */
    void getWxNickName(String wechatCode);
}
