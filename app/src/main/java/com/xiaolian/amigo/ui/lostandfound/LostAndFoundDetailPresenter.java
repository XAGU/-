package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostFoundDetailReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundDetailView;

import javax.inject.Inject;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public class LostAndFoundDetailPresenter<V extends ILostAndFoundDetailView> extends BasePresenter<V>
        implements ILostAndFoundDetailPresenter<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;

    @Inject
    LostAndFoundDetailPresenter(ILostAndFoundDataManager lostAndFoundDataManager) {
        super();
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void getLostAndFoundDetail(Long id) {
        QueryLostFoundDetailReqDTO dto = new QueryLostFoundDetailReqDTO();
        dto.setId(id);
        addObserver(lostAndFoundDataManager.getLostAndFound(dto),
                new NetworkObserver<ApiResult<LostAndFoundDTO>>(false, true) {

            @Override
            public void onReady(ApiResult<LostAndFoundDTO> result) {
                if (null == result.getError()) {
                    getMvpView().render(result.getData().transform());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
