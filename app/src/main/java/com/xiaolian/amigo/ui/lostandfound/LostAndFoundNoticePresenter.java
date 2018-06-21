package com.xiaolian.amigo.ui.lostandfound;

import com.xiaolian.amigo.data.manager.intf.ILostAndFoundDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundNoticeAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticePresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ILostAndFoundNoticeView;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/6/13
 */
public class LostAndFoundNoticePresenter<V extends ILostAndFoundNoticeView> extends BasePresenter<V>
        implements ILostAndFoundNoticePresenter<V> {
    private ILostAndFoundDataManager lostAndFoundDataManager;
    private LostAndFoundNoticeAdapter.ItemType currentItemType =
            LostAndFoundNoticeAdapter.ItemType.REPLY;
    private int page = Constant.PAGE_START_NUM;
    private static final int size = Constant.PAGE_SIZE;

    @Inject
    public LostAndFoundNoticePresenter(ILostAndFoundDataManager lostAndFoundDataManager) {
        this.lostAndFoundDataManager = lostAndFoundDataManager;
    }

    @Override
    public void changeItemTo(LostAndFoundNoticeAdapter.ItemType itemType) {
        this.currentItemType = itemType;
    }

    @Override
    public void getList() {
        if (currentItemType == LostAndFoundNoticeAdapter.ItemType.REPLY) {
            getReplyList();
        } else {
            getLikeList();
        }
    }

    private void getReplyList() {
        getMvpView().setLoadMoreComplete();
        getMvpView().setRefreshComplete();
    }

    private void getLikeList() {
        getMvpView().setLoadMoreComplete();
        getMvpView().setRefreshComplete();
    }

    @Override
    public void resetPage() {
        page = Constant.PAGE_START_NUM;
    }
}
