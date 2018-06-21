package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailAdapter;

import java.util.List;

/**
 * 失物招领详情
 *
 * @author zcd
 * @date 17/9/21
 */

public interface ILostAndFoundDetailView2 extends IBaseView {
    /**
     * 显示失物找零
     *
     * @param lostAndFound 失物招领
     */
    void render(LostAndFound lostAndFound);

    void setRefreshComplete();

    void setLoadMoreComplete();

    void onRefresh();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showEmptyView();

    void addMore(List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> wrappers,
                 List<LostAndFoundDetailAdapter.LostAndFoundDetailWrapper> hots);

    void closePublishDialog();

    void finishView();

    void showFootView();

    void hideFootView();
}
