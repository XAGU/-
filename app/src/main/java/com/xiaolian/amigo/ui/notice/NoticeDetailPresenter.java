package com.xiaolian.amigo.ui.notice;

import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.notify.NotifyDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailPresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailView;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/7/20
 */
public class NoticeDetailPresenter<V extends INoticeDetailView> extends BasePresenter<V>
        implements INoticeDetailPresenter<V> {
    private INoticeDataManager noticeDataManager;

    @Inject
    public NoticeDetailPresenter(INoticeDataManager noticeDataManager) {
        this.noticeDataManager = noticeDataManager;
    }

    @Override
    public void getNoticeDetail(Long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(noticeDataManager.getNotice(reqDTO),
                new NetworkObserver<ApiResult<NotifyDTO>>() {

                    @Override
                    public void onReady(ApiResult<NotifyDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().render(result.getData());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }
}
