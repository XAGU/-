package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * @author zcd
 * @date 18/6/13
 */
public interface ILostAndFoundNoticeView extends IBaseView {
    void setLoadMoreComplete();

    void setRefreshComplete();

    void showEmptyView();

    void hideEmptyView();

    void showErrorView();

    void hideErrorView();
}
