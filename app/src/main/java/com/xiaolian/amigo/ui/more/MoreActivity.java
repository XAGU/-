package com.xiaolian.amigo.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tencent.android.tpush.XGPushManager;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.more.adapter.MoreAdapter;
import com.xiaolian.amigo.ui.more.intf.IMorePresenter;
import com.xiaolian.amigo.ui.more.intf.IMoreView;
import com.xiaolian.amigo.ui.widget.RecycleViewDivider;
import com.xiaolian.amigo.ui.widget.dialog.ShareAlertDialog;
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
public class MoreActivity extends MoreBaseActivity implements IMoreView ,IUiListener , ShareAlertDialog.ShareQQListener {
    private static final String TAG = MoreActivity.class.getSimpleName();

    List<MoreAdapter.MoreModel> items = new ArrayList<MoreAdapter.MoreModel>() {
        {
            add(new MoreAdapter.MoreModel("帮助中心", WebActivity.class, Constant.H5_HELP));
            add(new MoreAdapter.MoreModel("意见反馈", WebActivity.class, Constant.H5_FEEDBACK));
            add(new MoreAdapter.MoreModel("用户协议", WebActivity.class, Constant.H5_AGREEMENT));
            add(new MoreAdapter.MoreModel("关于我们", AboutUsActivity.class));
            add(new MoreAdapter.MoreModel("推荐好友" ,null , null));
        }
    };

    @Inject
    IMorePresenter<IMoreView> presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    MoreAdapter adapter;

    private ShareAlertDialog shareAlertDialog ;

    public static Tencent mTencent;

    private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
    private int mExtarFlag = 0x00;

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
                                    + "?accessToken=" + presenter.getAccessToken() + "&refreshToken=" + presenter.getRefreshToken()));
                }else{
                    share();
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

    private void share(){
        if (shareAlertDialog == null){
            shareAlertDialog = new ShareAlertDialog(this);
            shareAlertDialog.Builder();
            shareAlertDialog.setShareQQListener(this);
        }
        shareAlertDialog.show();
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
    protected void onDestroy() {
        presenter.onDetach();
        if (shareAlertDialog != null) shareAlertDialog.finish();
        super.onDestroy();
    }


    @Override
    public void shareQQ() {
        if (mTencent == null){
            mTencent = Tencent.createInstance(Constant.QQ_APP_ID, MoreActivity.this);
        }
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constant.SHARE_URL);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "笑联-以科技和创新改善校园生活");

        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "校园高品质生活服务专家");

        // 网络图片
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"https://s.xiaolian365.com/found/129731_1546939243367_0171.jpg");

        // 本地图片
//        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,PictureUtil.getPathFromDrawableRes(this ,R.mipmap.ic_launcher));

        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "笑联");
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        mTencent.shareToQQ(this ,bundle ,this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode,resultCode,data,this);
        }
    }

    /**
     * QQ分享回调
     * @param o
     */
    @Override
    public void onComplete(Object o) {
//        onSuccess("分享成功");
        if (shareAlertDialog != null) shareAlertDialog.dismiss();
    }

    @Override
    public void onError(UiError uiError) {
//        onError("分享失败");
        if (shareAlertDialog != null) shareAlertDialog.dismiss();
    }

    @Override
    public void onCancel() {
//        onError("取消分享");
        if (shareAlertDialog != null) shareAlertDialog.dismiss();
    }


}
