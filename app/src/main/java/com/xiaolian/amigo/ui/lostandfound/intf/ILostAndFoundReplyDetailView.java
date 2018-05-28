package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundReplyDetailAdapter;

import java.util.List;

/**
 * 失物招领回复详情
 * @author zcd
 * @date 18/5/14
 */
public interface ILostAndFoundReplyDetailView extends IBaseView {
    void setRefreshComplete();

    void setLoadMoreComplete();

    void onRefresh();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showEmptyView();

    void addMore(List<LostAndFoundReplyDetailAdapter.LostAndFoundReplyDetailWrapper> wrappers);

    void closePublishDialog();

    void finishView();
}
