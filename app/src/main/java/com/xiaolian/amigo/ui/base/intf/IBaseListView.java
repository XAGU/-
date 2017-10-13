package com.xiaolian.amigo.ui.base.intf;

/**
 * BaseListView接口
 * @author zcd
 */

public interface IBaseListView extends IBaseView {

    void setLoadMoreComplete();

    void setRefreshComplete();

    void addPage();

    void showEmptyView();

    void showEmptyView(int colorRes);

    void hideEmptyView();

    void showErrorView();

    void showErrorView(int colorRes);

    void hideErrorView();

}
