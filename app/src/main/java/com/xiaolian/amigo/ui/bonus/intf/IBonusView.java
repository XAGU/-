package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;

import java.util.List;

/**
 * 红包View接口
 * @author zcd
 */
public interface IBonusView extends IBaseListView {

    /**
     * 加载红包数据
     *
     * @param bonuses 等待添加的红包数据
     */
    void addMore(List<BonusAdaptor.BonusWrapper> bonuses);
}
