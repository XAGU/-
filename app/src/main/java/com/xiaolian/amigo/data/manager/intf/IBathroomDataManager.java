package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.ShowerRoomRouterRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;

import retrofit2.http.Body;
import rx.Observable;

/**
 * @author zcd
 * @date 18/6/29
 */
public interface IBathroomDataManager {

    /**
     * 获取当前楼栋的浴室房间信息
     */
    Observable<ApiResult<BathBuildingRespDTO>> list(@Body SimpleReqDTO reqDTO);

    /**
     * 根据当前登录用户所在学校配置，以及用户上次洗澡的习惯，决定路由到"宿舍热水澡模块"、还是"公共浴室模块"
     */
    Observable<ApiResult<ShowerRoomRouterRespDTO>> route();
}
