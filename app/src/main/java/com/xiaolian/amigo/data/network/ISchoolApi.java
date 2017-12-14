package com.xiaolian.amigo.data.network;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.SimpleQueryReqDTO;
import com.xiaolian.amigo.data.network.model.school.QueryBriefSchoolListRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 学校相关api
 * <p>
 * Created by zcd on 17/12/14.
 */

public interface ISchoolApi {
    // 获取学校列表
    @POST("school/brief/list")
    Observable<ApiResult<QueryBriefSchoolListRespDTO>> getSchoolList(@Body SimpleQueryReqDTO body);

    // 获取学校业务列表
    @POST("school/business/list")
    Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList();
}
