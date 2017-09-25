package com.xiaolian.amigo.ui.base.intf;

/**
 * BaseListView接口
 * @author zcd
 */

public interface IBaseListView extends IBaseView {

    void showLoadMoreView();

    void hideLoadMoreView();

    void showNoMoreDataView();

    void setLoadMoreComplete();

    void setLoadAll(boolean hasLoadAll);

    void addPage();

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

}
