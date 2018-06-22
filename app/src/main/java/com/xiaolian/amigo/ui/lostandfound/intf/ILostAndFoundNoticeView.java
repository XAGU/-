package com.xiaolian.amigo.ui.lostandfound.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeAdapter;

import java.util.List;

/**
 * @author zcd
 * @date 18/6/13
 */
public interface ILostAndFoundNoticeView extends IBaseView {
    void setLoadMoreComplete();

    void setRefreshComplete();

    void showEmptyView();

    void hideEmptyView();

    void showErrorView();

    void hideErrorView();

    void addMoreReply(List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers);

    void addMoreLike(List<LostAndFoundNoticeAdapter.NoticeWrapper> wrappers);
}
