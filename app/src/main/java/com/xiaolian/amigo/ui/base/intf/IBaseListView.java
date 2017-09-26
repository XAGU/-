package com.xiaolian.amigo.ui.base.intf;

/**
 * BaseListView接口
 * @author zcd
 */

public interface IBaseListView extends IBaseView {

    void setLoadMoreComplete();

    void setRefreshComplete();

    void addPage();

}
