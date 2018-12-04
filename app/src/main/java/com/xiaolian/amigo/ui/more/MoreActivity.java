package com.xiaolian.amigo.ui.more;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tencent.android.tpush.XGPushManager;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.more.adapter.MoreAdapter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Constant;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多页面
 *
 * @author zcd
 * @date 17/10/13
 */
public class MoreActivity extends MoreBaseActivity implements IMoreView {
    private static final String TAG = MoreActivity.class.getSimpleName();

    List<MoreAdapter.MoreModel> items = new ArrayList<MoreAdapter.MoreModel>() {
        {
            add(new MoreAdapter.MoreModel("帮助中心", WebActivity.class, Constant.H5_HELP));
            add(new MoreAdapter.MoreModel("意见反馈", WebActivity.class, Constant.H5_FEEDBACK));
            add(new MoreAdapter.MoreModel("错误上报", null, ""));
            add(new MoreAdapter.MoreModel("用户协议", WebActivity.class, Constant.H5_AGREEMENT));
            add(new MoreAdapter.MoreModel("关于我们", AboutUsActivity.class));
        }
    };

    @Inject
    IMorePresenter<IMoreView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    MoreAdapter adapter;

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(MoreActivity.this);

        adapter = new MoreAdapter(this, R.layout.item_more, items);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Class clz = items.get(position).getClz();
                if (clz != null) {
                    startActivity(new Intent(MoreActivity.this, clz)
                            .putExtra(WebActivity.INTENT_KEY_URL, items.get(position).getExtra()
                                    + "?accessToken=" + presenter.getAccessToken() +"&refreshToken=" + presenter.getRefreshToken()));
                }else{
                    if (presenter != null){
                        presenter.uploadErrorLog();
                    }

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @OnClick(R.id.bt_logout)
    public void logout() {
        String pushTag = presenter.getPushTag();
        if (!TextUtils.isEmpty(pushTag)) {
            Log.d(TAG, "删除tag" + pushTag);
            XGPushManager.deleteTag(getApplicationContext(), pushTag);
            presenter.setPushTag("");
        }
        XGPushManager.delAccount(getApplicationContext(),
                presenter.getUserId() + "_jtL2T8nYY5D0klEm");
        presenter.deletePushToken();
        presenter.logout();
    }

    @Override
    protected int setTitle() {
        return R.string.more;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_more;
    }

    @Override
    public void backToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showTransfer() {
        items.add(new MoreAdapter.MoreModel("\"校OK\"账户迁移", WebActivity.class, Constant.H5_MIGRATE));
    }

    @Override
    public String getVersionName() {
        return AppUtils.getVersionName(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
