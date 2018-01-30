package com.xiaolian.amigo.ui.notice;

import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Notice;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知详情
 *
 * @author zcd
 * @date 17/10/12
 */

public class NoticeDetailActivity extends BaseToolBarActivity {
    NoticeAdaptor.NoticeWapper noticeWapper;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    protected void initInject() {
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setMainBackground(R.color.white);
        if (noticeWapper != null) {
            Notice notice = Notice.getNotice(noticeWapper.getType());
            setToolBarTitle(notice.getDesc());
            tvContent.setText(noticeWapper.getContent());
            tvTime.setText(getString(R.string.time_format,
                    TimeUtils.convertTimestampToFormat(noticeWapper.getCreateTime()),
                    TimeUtils.millis2String(noticeWapper.getCreateTime(), TimeUtils.MY_TIME_FORMAT)));
        }
    }

    @Override
    protected int setTitle() {
        return 0;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void setUp() {
        noticeWapper = (NoticeAdaptor.NoticeWapper) getIntent().getSerializableExtra(Constant.EXTRA_KEY);
    }
}
