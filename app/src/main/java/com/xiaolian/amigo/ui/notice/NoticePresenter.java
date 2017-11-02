package com.xiaolian.amigo.ui.notice;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.INoticeDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryNotifyListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryNotifyListRespDTO;
import com.xiaolian.amigo.data.network.model.notify.Notify;
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
    private static final String TAG = NoticePresenter.class.getSimpleName();
    private INoticeDataManager manager;

    @Inject
    public NoticePresenter(INoticeDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void requestNotices(Integer page) {
        QueryNotifyListReqDTO reqDTO = new QueryNotifyListReqDTO();
        reqDTO.setPage(page);
        reqDTO.setSize(Constant.PAGE_SIZE);
        addObserver(manager.queryNotifyList(reqDTO), new NetworkObserver<ApiResult<QueryNotifyListRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<QueryNotifyListRespDTO> result) {
                getMvpView().setLoadMoreComplete();
                getMvpView().setRefreshComplete();
                if (null == result.getError()) {
                    if (result.getData().getNotices() != null && result.getData().getNotices().size() > 0) {
                        List<NoticeAdaptor.NoticeWapper> wappers = new ArrayList<>();
                        for (Notify notify : result.getData().getNotices()) {
                            wappers.add(new NoticeAdaptor.NoticeWapper(notify));
                        }
                        getMvpView().addMore(wappers);
                        getMvpView().addPage();
                    } else {
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
        addObserver(manager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

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
