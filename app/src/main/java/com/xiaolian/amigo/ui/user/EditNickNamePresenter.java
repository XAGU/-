package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PersonalUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditNickNamePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditNickNameView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 编辑昵称Presenter
 * @author zcd
 */
public class EditNickNamePresenter<V extends IEditNickNameView> extends BasePresenter<V>
        implements IEditNickNamePresenter<V> {

    private IEditNickNameView mView;

    private IUserDataManager mUserDataManager;

    @Inject
    public EditNickNamePresenter(IUserDataManager manager) {
        super();
        mUserDataManager = manager;
    }

    @Override
    public void updateNickName(String nickName) {
        getMvpView().showLoading();
        PersonalUpdateReqDTO dto = new PersonalUpdateReqDTO();
        dto.setNickName(nickName);
        mUserDataManager.updateUserInfo(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CallbackWrapper<ApiResult<EntireUserDTO>>(mView) {
//                    @Override
//                    protected void onSuccess(ApiResult<EntireUserDTO> entireUserDTOApiResult) {
//
//                    }
//                });
//                .subscribe(new Consumer<ApiResult<EntireUserDTO>>() {
//                    @Override
//                    public void accept(@NonNull ApiResult<EntireUserDTO> entireUserDTOApiResult) throws Exception {
//
//                    }
//                });
                .subscribe(new DisposableSubscriber<ApiResult<EntireUserDTO>>() {
                    @Override
                    public void onNext(ApiResult<EntireUserDTO> entireUserDTOApiResult) {
                        getMvpView().showMessage(entireUserDTOApiResult.getError().getDebugMessage());
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onError(Throwable t) {
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().hideLoading();
                    }
                });

    }

//    @Override
//    public void onAttach(V mvpView) {
//        super.onAttach(mvpView);
//        mView = mvpView;
//    }
}
