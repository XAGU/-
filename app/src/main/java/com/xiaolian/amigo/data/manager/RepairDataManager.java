package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IRepairDataManager;
import com.xiaolian.amigo.data.network.ICsApi;
import com.xiaolian.amigo.data.network.IOssApi;
import com.xiaolian.amigo.data.network.IRepairApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.repair.RatingRepairReqDTO;
import com.xiaolian.amigo.data.network.model.cs.RemindReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemReqDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairApplyRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairDetailRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairProblemRespDTO;
import com.xiaolian.amigo.data.network.model.repair.RepairRespDTO;
import com.xiaolian.amigo.data.network.model.file.OssModel;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import rx.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Body;

/**
 * 报修管理
 *
 * @author caidong
 * @date 17/9/18
 */
public class RepairDataManager implements IRepairDataManager {

    private static final String TAG = RepairDataManager.class.getSimpleName();

    private IRepairApi repairApi;
    private ICsApi csApi;
    private IOssApi ossApi;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public RepairDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        repairApi = retrofit.create(IRepairApi.class);
        csApi = retrofit.create(ICsApi.class);
        ossApi = retrofit.create(IOssApi.class);
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public Observable<ApiResult<RepairRespDTO>> queryRepairs(@Body RepairReqDTO reqDTO) {
        return repairApi.queryRepairs(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairDetailRespDTO>> queryRepairDetail(@Body RepairDetailReqDTO reqDTO) {
        return repairApi.queryRepairDetail(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairApplyRespDTO>> applyRepair(@Body RepairApplyReqDTO reqDTO) {
        return repairApi.applyRepair(reqDTO);
    }

    @Override
    public Observable<ApiResult<RepairProblemRespDTO>> queryRepairProblems(@Body RepairProblemReqDTO reqDTO) {
        return repairApi.queryRepairProblems(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> rateRapair(@Body RatingRepairReqDTO reqDTO) {
        return repairApi.rateRapair(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> remind(@Body RemindReqDTO reqDTO) {
        return csApi.remind(reqDTO);
    }

    @Override
    public Observable<ApiResult<OssModel>> getOssModel() {
        return ossApi.getOssModel();
    }

    @Override
    public User getUser() {
        return sharedPreferencesHelp.getUserInfo();
    }

    @Override
    public void setLastRepairTime(long l) {
        sharedPreferencesHelp.setLastRepairTime(l);
    }

    @Override
    public void setRepairGuide(Integer guideTime) {
        sharedPreferencesHelp.setRepairGuide(guideTime);
    }

    @Override
    public boolean isRepairGuideDone() {
        return !(sharedPreferencesHelp.getRepairGuide() != null
                && sharedPreferencesHelp.getRepairGuide() < 3);
    }

    @Override
    public void doneRepairGuide() {
        sharedPreferencesHelp.setRepairGuide(sharedPreferencesHelp.getRepairGuide() + 1);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> cancelRepair(@Body SimpleReqDTO reqDTO) {
        return repairApi.cancelRepair(reqDTO);
    }
}
