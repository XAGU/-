package com.xiaolian.amigo.ui.lostandfound.intf;

import android.content.Intent;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IBlogPresenter <V  extends IBlogView> extends IBasePresenter<V> {

    void getLostList(String hotPosIds , int page , String selectKey , int topicId);

    void unLikeComment(int position, long id) ;

    void likeComment(int position, long id);

    /**
     * 是否开启评论
     */
    boolean isCommentEnable();

    /**
     * 处理从另一个Activity返回的数据
     * @param data
     */
    void handleData(Intent data);


     void setCurrentChoosePosition(int currentChoosePosition) ;

    void setCurrentHotPosition(int currentHotPosition);



}
