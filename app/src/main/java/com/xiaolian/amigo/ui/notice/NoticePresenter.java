package com.xiaolian.amigo.ui.notice;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.notify.NotifyDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.notify.QueryNotifyListRespDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;
import com.xiaolian.amigo.ui.notice.intf.INoticePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public class NoticePresenter<V extends INoticeView> extends BasePresenter<V>
        implements INoticePresenter<V> {
    @SuppressWarnings("unused")
    private static final String TAG = NoticePresenter.class.getSimpleName();
    private INoticeDataManager noticeDataManager;

    @Inject
    public NoticePresenter(INoticeDataManager noticeDataManager) {
        super();
        this.noticeDataManager = noticeDataManager;
    }

    @Override
    public void requestNotices(Integer page) {
        QueryNotifyListReqDTO reqDTO = new QueryNotifyListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        addObserver(noticeDataManager.queryNotifyList(reqDTO), new NetworkObserver<ApiResult<QueryNotifyListRespDTO>>(false, true) {

            @Override
            public void onReady(ApiResult<QueryNotifyListRespDTO> result) {
                getMvpView().setLoadMoreComplete();
                getMvpView().setRefreshComplete();
                getMvpView().hideEmptyView();
                getMvpView().hideErrorView();
                if (null == result.getError()) {
                    if (result.getData().getNotices() != null && result.getData().getNotices().size() > 0) {
                        List<NoticeAdaptor.NoticeWapper> wrappers = new ArrayList<>();
                        for (NotifyDTO notify : result.getData().getNotices()) {
                            wrappers.add(new NoticeAdaptor.NoticeWapper(notify.transform()));
                        }
                        getMvpView().addMore(wrappers);
                        getMvpView().addPage();
                    } else {
                        getMvpView().addMore(new ArrayList<>());
                        getMvpView().showEmptyView(R.string.empty_tip, R.color.colorBackgroundWhite);
                    }
                } else {
                    getMvpView().showErrorView(R.color.colorBackgroundWhite);
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().setLoadMoreComplete();
                getMvpView().setRefreshComplete();
                getMvpView().showErrorView(R.color.colorBackgroundWhite);
            }
        });
    }

    @Override
    public void readUrgentNotify(Long id) {
        ReadNotifyReqDTO reqDTO = new ReadNotifyReqDTO();
        reqDTO.setId(id);
        addObserver(noticeDataManager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().readNotify(id);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
