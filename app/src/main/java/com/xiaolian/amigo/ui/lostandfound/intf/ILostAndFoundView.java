package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;

import java.util.List;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface ILostAndFoundView extends IBaseListView {
    /**
     * 加载失物列表
     *
     * @param lost 待添加的失物列表
     */
    void addMoreLost(List<LostAndFoundAdaptor.LostAndFoundWapper> lost);

    /**
     * 加载招领列表
     *
     * @param found 待添加的招领列表
     */
    void addMoreFound(List<LostAndFoundAdaptor.LostAndFoundWapper> found);

    void addMore(List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFoundWappers);

    void showNoSearchResult(String selectKey);

    void showSearchResult(List<LostAndFoundAdaptor.LostAndFoundWapper> wappers);

    void refresh();
}
