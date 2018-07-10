package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
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
}
