package com.xiaolian.amigo.ui.repair.intf;

import com.xiaolian.amigo.data.network.model.repair.RepairProblem;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

/**
 * 报修申请
 *
 * @author caidong
 * @date 17/9/18
 */
public interface IRepairApplyView extends IBaseView {

    /**
     * 渲染页面内容
     */
    void render();

    /**
     * 变更提交按钮的状态
     */
    void toggleBtnStatus();

    /**
     * 返回报修申请入口页面
     */
    void backToRepairNav();

    /**
     * 添加图片
     *
     * @param url      url
     * @param position 位置
     */
    void addImage(String url, int position);

    /**
     * 刷新报修问题
     *
     * @param problems 报修问题
     */
    void refreshProblems(List<RepairProblem> problems);
}
