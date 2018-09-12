package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/6/21
 */
public interface IMyCollectPresenter<V extends IMyCollectView> extends IBasePresenter<V> {
    void getMyCollects();

    void resetPage();

    void unLikeComment(int position, long id) ;

    void likeComment(int position, long id);


}
