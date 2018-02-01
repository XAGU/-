package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RatingRepairReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.vo.User;

import retrofit2.http.Body;
import rx.Observable;

/**
 * @author caidong
 * @date 17/9/18
 */
public interface IRepairDataManager {
    // 查询个人报修列表
    Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO);

    // 查询报修单详情
    Observable<ApiResult<RepairDetailRespDTO>> queryRepairDetail(@Body RepairDetailReqDTO reqDTO);

    // 申请报修
    Observable<ApiResult<RepairApplyRespDTO>> applyRepair(@Body RepairApplyReqDTO reqDTO);

    // 获取报修问题列表
    Observable<ApiResult<RepairProblemRespDTO>> queryRepairProblems(@Body RepairProblemReqDTO reqDTO);

    // 维修评价
    Observable<ApiResult<BooleanRespDTO>> rateRapair(@Body RatingRepairReqDTO reqDTO);

    // 取消报修
    Observable<ApiResult<BooleanRespDTO>> cancelRepair(@Body SimpleReqDTO reqDTO);

    // 提醒客服
    Observable<ApiResult<BooleanRespDTO>> remind(@Body RemindReqDTO reqDTO);

    // oss
    Observable<ApiResult<OssModel>> getOssModel();

    User getUser();

    void setLastRepairTime(long l);
}
