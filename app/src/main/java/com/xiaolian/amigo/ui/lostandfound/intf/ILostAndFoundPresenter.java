package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface ILostAndFoundPresenter<V extends ILostAndFoundView> extends IBasePresenter<V> {
    void queryLostAndFoundList(int page, Long schoolId, String selectKey, int size, int type);
}
