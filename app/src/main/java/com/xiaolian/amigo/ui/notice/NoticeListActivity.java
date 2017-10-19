package com.xiaolian.amigo.ui.notice;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.NoticeReadStatus;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;
import com.xiaolian.amigo.ui.notice.intf.INoticePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.ui.widget.dialog.NoticeAlertDialog;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知中心
 * <p>
 * Created by zcd on 9/22/17.
 */

public class NoticeListActivity extends NoticeBaseListActivity implements INoticeView {

    @Inject
    INoticePresenter<INoticeView> presenter;

    NoticeAdaptor adaptor;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<NoticeAdaptor.NoticeWapper> notices = new ArrayList<>();

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        notices.clear();
        presenter.requestNotices(page);
    }

    @Override
    protected void onLoadMore() {
        presenter.requestNotices(page);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        setMainBackground(R.color.white);
        setHeaderBackground(R.color.white);
        adaptor = new NoticeAdaptor(this, R.layout.item_notice, notices);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                // 1 表示紧急通知
//                if (notices.get(position).getType() == 1 && notices.get(position).getReadStatus() == 1) {
//                    showUrgentNotify(notices.get(position).getContent(),
//                            notices.get(position).getId());
//                }
                notices.get(position).setReadStatus(NoticeReadStatus.READ.getType());
                presenter.readUrgentNotify(notices.get(position).getId());
                startActivity(new Intent(getApplicationContext(), NoticeDetailActivity.class)
                                .putExtra(Constant.EXTRA_KEY, notices.get(position)));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

    }

    @Override
    protected int setTitle() {
        return R.string.notice_center;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(NoticeListActivity.this);
    }

    public void showUrgentNotify(String content, Long id) {
        NoticeAlertDialog dialog = new NoticeAlertDialog(this);
        dialog.setContent(content);
        dialog.setOnOkClickListener(new NoticeAlertDialog.OnOkClickListener() {
            @Override
            public void onOkClick(Dialog dialog, boolean isNotReminder) {
                if (isNotReminder) {
                    presenter.readUrgentNotify(id);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void addMore(List<NoticeAdaptor.NoticeWapper> wapper) {
        notices.addAll(wapper);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void readNotify(Long id) {
        for (NoticeAdaptor.NoticeWapper notice : notices) {
            if (CommonUtil.equals(notice.getId(), id)) {
                // 2 表示已读
                notice.setReadStatus(NoticeReadStatus.READ.getType());
            }
        }
    }
}
