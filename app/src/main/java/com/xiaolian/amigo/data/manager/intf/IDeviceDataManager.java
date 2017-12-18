package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.FavorDeviceReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.device.QueryFavorDeviceRespDTO;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListReqDTO;
import com.xiaolian.amigo.data.network.model.device.QueryWaterListRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.cs.CsMobileRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.funds.PersonalWalletDTO;
import com.xiaolian.amigo.data.network.model.order.OrderPreInfoDTO;
import com.xiaolian.amigo.data.network.model.order.QueryPrepayOptionReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.UnsettledOrderStatusCheckRespDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultReqDTO;
import com.xiaolian.amigo.data.network.model.trade.CmdResultRespDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandReqDTO;
import com.xiaolian.amigo.data.network.model.trade.ConnectCommandRespDTO;
import com.xiaolian.amigo.data.network.model.trade.PayReqDTO;
import com.xiaolian.amigo.data.network.model.trade.PayRespDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;

import java.util.List;

import retrofit2.http.Body;
import rx.Observable;

/**
 * 设备数据管理
 * <p>
 * Created by zcd on 9/29/17.
 */

public interface IDeviceDataManager {
    void setBonusAmount(int amount);

    int getBonusAmount();

    String getMacAddressByDeviceNo(String macAddress);

    void setDeviceNoAndMacAddress(String macAddress, String currentMacAddress);

    void setDeviceToken(String deviceNo, String deviceToken);

    // 请求连接设备指令
    Observable<ApiResult<ConnectCommandRespDTO>> getConnectCommand(@Body ConnectCommandReqDTO reqDTO);

    // 请求处理设备指令响应结果
    Observable<ApiResult<CmdResultRespDTO>> processCmdResult(@Body CmdResultReqDTO reqDTO);

    // 网络支付，创建用水订单
    Observable<ApiResult<PayRespDTO>> pay(@Body PayReqDTO reqDTO);

    // 处理扫描结果
    Observable<ApiResult<QueryDeviceListRespDTO>> handleScanDevices(QueryDeviceListReqDTO reqDTO);

    // 校验订单状态
    Observable<ApiResult<UnsettledOrderStatusCheckRespDTO>> checkOrderStatus(@Body UnsettledOrderStatusCheckReqDTO reqDTO);

    void setDeviceResult(String deviceNo, String deviceResult);

    String getDeviceResult(String deviceNo);

    void setCloseCmd(String deviceNo, String closeCmd);

    String getCloseCmd(String deviceNo);

    // 获取余额
    Observable<ApiResult<PersonalWalletDTO>> queryWallet();

    // 订单预备信息：options是预付金额选项，bonus是可用代金券数量
    Observable<ApiResult<OrderPreInfoDTO>> queryPrepayOption(@Body QueryPrepayOptionReqDTO reqDTO);

    // 获取客服人员电话
    Observable<ApiResult<CsMobileRespDTO>> queryCsInfo();

    // 设置温馨提示次数
    boolean isHeaterGuideDone();
    void doneHeaterGuide();
    void setHeaterGuide(Integer guideTime);
    boolean isDispenserGuideDone();
    void doneDispenserGuide();
    void setDispenserGuide(Integer guideTime);

    // 收藏饮水机
    Observable<ApiResult<SimpleRespDTO>> favorite(@Body FavorDeviceReqDTO reqDTO);

    // 取消收藏饮水机
    Observable<ApiResult<SimpleRespDTO>> unFavorite(@Body FavorDeviceReqDTO reqDTO);
    // 获取个人收藏的设备列表
    Observable<ApiResult<QueryFavorDeviceRespDTO>> getFavorites(@Body QueryDeviceListReqDTO reqDTO);
    List<DeviceCategory> getDeviceCategory();
}
