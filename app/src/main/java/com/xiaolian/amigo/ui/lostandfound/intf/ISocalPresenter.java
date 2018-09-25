package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface ISocalPresenter <V extends ISocalView> extends IBasePresenter<V> {


    void getTopicList();

    void getLostList(String hotPosIds , int page , String selectKey , int topicId);

    void unLikeComment(int position, long id) ;

    void likeComment(int position, long id);

    void unLikeComment(int position, long id, boolean isDialog) ;

    void likeComment(int position, long id , boolean isDialog);

    /**
     * 是否开启评论
     */
    boolean isCommentEnable();

    int getNoticeCount();

    void fetchNoticeCount();


}
