package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

public interface ISocalView extends IBaseView {
    void referTopic(BbsTopicListTradeRespDTO data);

    void setReferComplete();

    void referTopicList(QueryLostAndFoundListRespDTO data);

    void loadMore(QueryLostAndFoundListRespDTO data);

    void reducePage();

    void onErrorView();

    void onEmpty();

    void notifyAdapter(int position, boolean b);

    void showNoticeRemind(int num);

    void hideNoticeRemind();

    /**
     * 没有开放评论时，没有评论功能
     */
    void hideCommentView();

    /**
     * 显示评论功能
     */
    void showCommentView();

    void showSearchResult(List<LostAndFoundDTO> posts);

    void showNoSearchResult(String selectKey);

    void notifyAdapter(int position, boolean b, boolean b1);
}
