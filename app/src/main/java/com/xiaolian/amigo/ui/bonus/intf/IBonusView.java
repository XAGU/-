package com.xiaolian.amigo.ui.bonus.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.bonus.adaptor.BonusAdaptor;

import java.util.List;

/**
 * 代金券View接口
 * @author zcd
 */
public interface IBonusView extends IBaseListView {

    /**
     * 加载代金券数据
     *
     * @param bonuses 等待添加的代金券数据
     */
    void addMore(List<BonusAdaptor.BonusWrapper> bonuses);
}
