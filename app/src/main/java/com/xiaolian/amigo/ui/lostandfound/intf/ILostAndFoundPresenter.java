package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领
 *
 * @author zcd
 * @date 17/9/18
 */

public interface ILostAndFoundPresenter<V extends ILostAndFoundView> extends IBasePresenter<V> {
    /**
     * 获取失物招领列表
     *
     * @param page      页数
     * @param size      每页个数
     * @param type      类型
     * @param selectKey 搜索关键词
     */
    void queryLostAndFoundList(Integer page, Integer size, Integer type, String selectKey);

    /**
     * 获取失物列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void queryLostList(Integer page, Integer size);

    /**
     * 获取招领列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void queryFoundList(Integer page, Integer size);

    /**
     * 搜索失物列表
     *
     * @param page      页数
     * @param size      每页个数
     * @param selectKey 搜索关键词
     */
    void searchLostList(Integer page, Integer size, String selectKey);

    /**
     * 搜索招领列表
     *
     * @param page      页数
     * @param size      每页个数
     * @param selectKey 搜索关键词
     */
    void searchFoundList(Integer page, Integer size, String selectKey);

    /**
     * 获取我的失物招领列表
     */
    void getMyLostAndFounds();

    /**
     * 删除我的失物招领
     *
     * @param id id
     */
    void deleteLostAndFounds(Long id);
}
