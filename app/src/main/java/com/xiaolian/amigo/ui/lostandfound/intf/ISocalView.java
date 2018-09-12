package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

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
}
