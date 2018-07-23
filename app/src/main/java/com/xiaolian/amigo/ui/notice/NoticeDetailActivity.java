package com.xiaolian.amigo.ui.notice;

import android.widget.TextView;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Notice;
import com.xiaolian.amigo.data.network.model.notify.NotifyDTO;
import com.xiaolian.amigo.ui.notice.adaptor.NoticeAdaptor;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailPresenter;
import com.xiaolian.amigo.ui.notice.intf.INoticeDetailView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知详情
 *
 * @author zcd
 * @date 17/10/12
 */

public class NoticeDetailActivity extends NoticeBaseActivity implements INoticeDetailView {
    NoticeAdaptor.NoticeWapper noticeWapper;
    @Inject
    INoticeDetailPresenter<INoticeDetailView> presenter;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.tv_time)
    TextView tvTime;
    private Long detailId = null;

    @Override
    protected void initView() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this必须为点击消息要跳转到页面的上下文。
        XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(this);
        if (clickedResult != null) {
            //获取消息附近参数
            String customContent = clickedResult.getCustomContent();
            //获取消息标题
            String set = clickedResult.getTitle();
            //获取消息内容
            String s = clickedResult.getContent();
            if (customContent != null && customContent.length() != 0) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    if (!obj.isNull("targetId")) {
                        detailId = Long.valueOf(obj.getString("targetId"));
                        presenter.getNoticeDetail(detailId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    public void render(NotifyDTO data) {
        Notice notice = Notice.getNotice(data.getType());
        setToolBarTitle(notice.getDesc());
        tvContent.setText(data.getContent());
        tvTime.setText(getString(R.string.time_format,
                TimeUtils.convertTimestampToFormat(data.getCreateTime()),
                TimeUtils.millis2String(data.getCreateTime(), TimeUtils.MY_TIME_FORMAT)));
    }
}
