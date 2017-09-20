package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.repair.adaptor.RepairAdaptor;

import java.util.List;

/**
 * Created by caidong on 2017/9/18.
 */
public interface IRepairApplyView extends IBaseView {

    // 渲染页面内容
    void render();

    // 变更提交按钮的状态
    void toggleBtnStatus();
}
