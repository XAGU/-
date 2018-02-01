package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundAdaptor;

import java.util.List;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
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

    /**
     * 加载失物招领
     *
     * @param lostAndFoundWappers 失物招领
     */
    void addMore(List<LostAndFoundAdaptor.LostAndFoundWapper> lostAndFoundWappers);

    /**
     * 显示未找到搜索结果
     *
     * @param selectKey 搜索关键词
     */
    void showNoSearchResult(String selectKey);

    /**
     * 显示搜索结果
     *
     * @param wappers 搜索结果
     */
    void showSearchResult(List<LostAndFoundAdaptor.LostAndFoundWapper> wappers);

    /**
     * 刷新
     */
    void refresh();
}
