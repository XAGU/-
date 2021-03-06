package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.residence.QueryResidenceListReqDTO;
import com.xiaolian.amigo.data.network.model.residence.ResidenceListRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 建筑相关api
 *
 * @author zcd
 * @date 17/12/14
 */

public interface IResidenceApi {
    /**
     * 获取建筑列表
     * residenceLevel 1幢 2楼层 3宿舍 具体位置 buildingType 1宿舍楼 parentId上一层事物Id
     */
    @POST("residence/list")
    Observable<ApiResult<ResidenceListRespDTO>> queryResidenceList(@Body QueryResidenceListReqDTO body);

    /**
     * 获取公共浴室列表
     * residenceLevel 1幢 2楼层 3宿舍 具体位置 buildingType 1宿舍楼 parentId上一层事物Id
     */
    @POST("residence/bath/list")
    Observable<ApiResult<ResidenceListRespDTO>> queryBathResidenceList(@Body QueryResidenceListReqDTO body);


}
