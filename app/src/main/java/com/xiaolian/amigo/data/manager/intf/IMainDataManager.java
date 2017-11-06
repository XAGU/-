package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.IMainApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.user.User;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainDataManager extends IMainApi {

    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    void logout();

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);

    void setBonusAmount(int amount);

    int getBonusAmount();

    void setBalance(String balance);

    String getBalance();

    // 获取个人订单
    Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO);

    void setLastUpdateRemindTime();

    Long getLastUpdateRemindTime();

    boolean isMainGuideDone();

    void doneMainGuide();
}
