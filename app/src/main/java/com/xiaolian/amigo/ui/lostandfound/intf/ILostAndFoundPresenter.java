package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface ILostAndFoundPresenter<V extends ILostAndFoundView> extends IBasePresenter<V> {
    void queryLostAndFoundList(Integer page, Integer size, Integer type, String selectKey);

    void queryLostList(Integer page, Integer size);

    void queryFoundList(Integer page, Integer size);

    void searchLostList(Integer page, Integer size, String selectKey);

    void searchFoundList(Integer page, Integer size, String selectKey);
}
