package com.xiaolian.amigo.ui.user;

import android.util.Log;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.ChangeSchoolResDTO;
import com.xiaolian.amigo.data.network.model.common.CheckSchoolRespDTO;
import com.xiaolian.amigo.data.network.model.common.CommitSchoolReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeSchoolView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class ChangeSchoolPresenter<v extends IChangeSchoolView> extends BasePresenter<v>
        implements IChangeSchoolPresenter<v> {

    private IUserDataManager userDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    ChangeSchoolPresenter(IUserDataManager userDataManager, ISharedPreferencesHelp sharedPreferencesHelp) {
        super();
        this.userDataManager = userDataManager;
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void cancelApplySchool(long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(userDataManager.cancelApplyChangeSchool(reqDTO), new NetworkObserver<ApiResult<CheckSchoolRespDTO>>() {
            @Override
            public void onReady(ApiResult<CheckSchoolRespDTO> result) {
                if (result.getError() == null) {
                    //取消申请
                    if (result.getData().isResult()) {
                        getMvpView().gotoEditProfile();
                    } else {
                        getMvpView().onError(result.getData().getFailReason());
                    }

                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }
        });

    }

    @Override
    public void realChangeSchool(long id, String reason) {
        CommitSchoolReqDTO reqDTO = new CommitSchoolReqDTO();
        reqDTO.setTargetSchoolId(id);
        reqDTO.setReason(reason);
        addObserver(userDataManager.applyChangeSchool(reqDTO), new NetworkObserver<ApiResult<ChangeSchoolResDTO>>() {
            @Override
            public void onReady(ApiResult<ChangeSchoolResDTO> result) {
                if (result.getError() == null) {
                    //提交审核成功
                    long id = result.getData().getId();
                        //提交审核成功，请求数据并且刷新页面
                    EventBus.getDefault().post(new ChangeSchoolActivity.ChangeSchoolMsg(1,id));
                    getMvpView().showAppliedStatus();

                } else {
                    Log.e(TAG, "onReady: 提交失败：" +result.getError().getDisplayMessage());
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG, "onError: 提交申请： "+ e.getMessage() );
            }
        });

    }

    @Override
    public void preChangeSchool() {
        addObserver(userDataManager.preApplySchoolCheck(), new NetworkObserver<ApiResult<CheckSchoolRespDTO>>() {
            @Override
            public void onReady(ApiResult<CheckSchoolRespDTO> result) {
                if (result.getError() == null) {
                    //预处理成功
                    if (result.getData().isResult()) {
                        getMvpView().hideSuccessDialog();
                        getMvpView().showCommitDialog();
                    } else {
                        getMvpView().hideFailureDialgo();
                        getMvpView().onError(result.getData().getFailReason());
                    }

                } else {
                    getMvpView().hideFailureDialgo();
                    Log.e(TAG, "onReady: 更换学校预处理网络错误" );
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().hideFailureDialgo();
                getMvpView().onError("网络错误");
            }
        });
    }
     @Override
    public void requestForRelogin(){
        addObserver(userDataManager.changeSchoolCheck(), new NetworkObserver<ApiResult<CheckSchoolRespDTO>>() {
            @Override
            public void onReady(ApiResult<CheckSchoolRespDTO> result) {

            }
        });
    }
}
