package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;

import java.util.List;

/**
 * @author zcd
 * @date 18/5/12
 */
public interface ILostAndFoundView2 extends IBaseView {
    void setRefreshComplete();

    void setLoadMoreComplete();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showEmptyView();

    void addMore(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers);

    void showNoSearchResult(String searchStr);

    void showSearchResult(List<LostAndFoundAdaptor2.LostAndFoundWrapper> wrappers);

    void showFootView();

    void hideFootView();

    void showNoticeRemind();

    void hideNoticeRemind();
}
