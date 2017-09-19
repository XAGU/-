package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;

import java.util.List;

/**
 * 列表选择页
 * <p>
 * Created by zcd on 9/19/17.
 */

public interface IListChooseView extends IBaseView {
    void finishView();

    void backToDormitory();

    void addMore(List<ListChooseAdaptor.Item> item);
}
