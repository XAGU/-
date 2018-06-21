package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeAdapter;

/**
 * @author zcd
 * @date 18/6/13
 */
public interface ILostAndFoundNoticePresenter<V extends ILostAndFoundNoticeView>
    extends IBasePresenter<V> {
    void changeItemTo(LostAndFoundNoticeAdapter.ItemType itemType);

    void getList();

    void resetPage();
}
