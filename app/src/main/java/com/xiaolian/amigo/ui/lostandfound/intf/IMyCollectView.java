package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor2;

import java.util.List;

/**
 * @author zcd
 * @date 18/6/21
 */
public interface IMyCollectView extends IBaseView {
    void setRefreshComplete();

    void setLoadMoreComplete();

    void showErrorView();

    void hideEmptyView();

    void hideErrorView();

    void showEmptyView();

    void addMore(List<LostAndFoundDTO> wrappers);

    void notifyAdapter(int position, boolean b);

    void refer(List<LostAndFoundDTO> wrappers);
}
