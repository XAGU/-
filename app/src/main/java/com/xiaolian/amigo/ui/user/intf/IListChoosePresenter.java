package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 列表选择页
 *
 * @author zcd
 * @date 17/9/19
 */

public interface IListChoosePresenter<V extends IListChooseView> extends IBasePresenter<V> {

    /**
     * 获取学校列表
     *
     * @param page 页数
     * @param size 每页个数
     */
    void getSchoolList(Integer page, Integer size);

    /**
     * 获取建筑物列表
     *
     * @param page       页数
     * @param size       每页个数
     * @param deviceType 设备类型
     */
    void getBuildList(Integer page, Integer size, Integer deviceType);

    /**
     * 更新学校
     *
     * @param schoolId 学校id
     */
    void updateSchool(Long schoolId);

    /**
     * 更新性别
     *
     * @param sex 性别
     */
    void updateSex(int sex);

    /**
     * 获取楼层列表
     *
     * @param page       页数
     * @param size       每页个数
     * @param deviceType 设备类型
     * @param parentId   父id
     */
    void getFloorList(Integer page, Integer size, Integer deviceType, Long parentId);

    /**
     * 获取宿舍列表
     *
     * @param page        页数
     * @param size        每页个数
     * @param deviceType  设备类型
     * @param parentId    父id
     * @param existDevice 是否存在设备
     */
    void getDormitoryList(Integer page, Integer size, Integer deviceType, Long parentId, boolean existDevice);

    /**
     * 绑定宿舍
     *
     * @param id          id
     * @param residenceId 位置id
     * @param isEdit      是否编辑
     */
    void bindDormitory(Long id, Long residenceId, boolean isEdit);

    /**
     * 绑定宿舍
     *
     * @param id          id
     * @param residenceId 位置id
     * @param isEdit      是否编辑
     * @param activitySrc 来源activity
     */
    void bindDormitory(Long id, Long residenceId, boolean isEdit, String activitySrc);
}
