package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundPresenter2;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundView2;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/5/12
 */
public class LostAndFoundPresenter2<V extends ILostAndFoundView2> extends BasePresenter<V>
    implements ILostAndFoundPresenter2<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;

    @Inject
    public LostAndFoundPresenter2(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }
}
