package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/5/12
 */
public interface ILostAndFoundPresenter2<V extends ILostAndFoundView2> extends IBasePresenter<V> {
    void getList(boolean isSearch, String searchStr);

    void resetPage();

    void getMyList();

    /**
     * 是否开启评论
     */
    boolean isCommentEnable();

    int getNoticeCount();

    void fetchNoticeCount();

    void unLikeComment(int position, long id) ;

    void likeComment(int position, long id);
}
