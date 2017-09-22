package com.xiaolian.amigo.ui.notice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;
import com.xiaolian.amigo.ui.notice.intf.INoticePresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeView;
import com.xiaolian.amigo.util.Constant;

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

public class NoticeActivity extends NoticeBaseActivity implements INoticeView {

    @Inject
    INoticePresenter<INoticeView> presenter;

    NoticeAdaptor adaptor;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<NoticeAdaptor.NoticeWapper> notices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);

        presenter.onAttach(NoticeActivity.this);

        adaptor = new NoticeAdaptor(this, R.layout.item_notice, notices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        presenter.requestNotices(Constant.PAGE_START_NUM);
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void addMore(List<NoticeAdaptor.NoticeWapper> wapper) {
        notices.addAll(wapper);
        adaptor.notifyDataSetChanged();
    }
}
