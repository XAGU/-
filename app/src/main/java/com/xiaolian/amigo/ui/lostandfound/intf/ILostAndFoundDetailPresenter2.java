package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.vo.LostAndFound;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * @author zcd
 * @date 18/5/14
 */
public interface ILostAndFoundDetailPresenter2<V extends ILostAndFoundDetailView2>
        extends IBasePresenter<V> {
    void setType(int type);

    void getDetail(long id);

    void refreshDetail();

    LostAndFound getLostAndFound();

    void getComments();

    void resetPage();

    void publishComment(String comment);

    void publishReply(Long replyToId, Long replyToUserId, String reply);

    boolean isOwner();

    void reportOrDelete();


    boolean isCommentEnable();

    void collect();

    void unCollect();

    void unLikeComment(int position, long id);

    void likeComment(int position, long id);

    void unLikeContent(int position, long id);

    void likeContent(int position, long id);
}
