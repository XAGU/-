package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 失物招领回复详情
 * @author zcd
 * @date 18/5/14
 */
public interface ILostAndFoundReplyDetailPresenter<V extends ILostAndFoundReplyDetailView>
    extends IBasePresenter<V> {
    void setCommentId(Long commentId);

    void getReplies();

    void resetPage();

    boolean isOwner();

    boolean isPublisher();

    void setCommentAuthorId(Long commentAuthorId);

    void publishReply(Long replyToId, Long replyToUserId, String reply);

    void setLostFoundId(Long  lostFoundId);

    void setOwnerId(Long ownerId);

    Long getOwnerId();

    void reportOrDelete();

    Long getUserId();

    void deleteReply(Long id);

    void reportReply(Long id);

    void setCommentEnable(boolean commentEnable);

    boolean isCommentEnable();
}
