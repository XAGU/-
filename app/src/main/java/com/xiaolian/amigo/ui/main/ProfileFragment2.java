package com.xiaolian.amigo.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.bonus.BonusActivity;
import com.xiaolian.amigo.ui.credits.CreditsActivity;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.main.adaptor.ProfileAdaptor;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.more.MoreActivity;
import com.xiaolian.amigo.ui.notice.NoticeListActivity;
import com.xiaolian.amigo.ui.repair.RepairNavActivity;
import com.xiaolian.amigo.ui.user.EditProfileActivity;
import com.xiaolian.amigo.ui.user.UserCertificationActivity;
import com.xiaolian.amigo.ui.user.UserCertificationStatusActivity;
import com.xiaolian.amigo.ui.wallet.WalletActivity;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.Log;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import lombok.Data;

import static com.xiaolian.amigo.util.Constant.CERTIFICATION_FAILURE;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_NONE;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_PASS;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_REVIEWING;

/**
 * 个人中心
 *
 * @author zcd
 * @date 17/10/10
 */

public class ProfileFragment2 extends BaseFragment {
    private static final String CERTIFICATION_NONE_TXT = "未认证" ;
    private static final String CERTIFICATION_REVIEWING_TXT = "审核中";
    private static final String CERTIFICATION_PASS_TXT = "已认证" ;
    private static final String CERTIFICATION_FAILURE_TXT = "认证失败";

    public static final int START_EDIT_PROFILE = 01;
    private static final String TAG = ProfileFragment2.class.getSimpleName();
    ProfileAdaptor.Item wallet = new ProfileAdaptor.Item(R.drawable.profile_wallet, "我的钱包", WalletActivity.class);
    ProfileAdaptor.Item credits = new ProfileAdaptor.Item(R.drawable.profile_credits, "积分兑换", CreditsActivity.class);
    ProfileAdaptor.Item bonus = new ProfileAdaptor.Item(R.drawable.profile_luck, "我的代金券", BonusActivity.class);
    ProfileAdaptor.Item repair = new ProfileAdaptor.Item(R.drawable.profile_repair, "设备报修", RepairNavActivity.class);
    ProfileAdaptor.Item legalize = new ProfileAdaptor.Item(R.drawable.profile_certification, "学生认证", null,"");
    List<ProfileAdaptor.Item> items = new ArrayList<ProfileAdaptor.Item>() {
        {
            add(new ProfileAdaptor.Item(R.drawable.profile_edit, "编辑个人信息", EditProfileActivity.class));
            add(legalize);
            add(wallet);
//            add(new ProfileAdaptor.Item(R.drawable.profile_order, "消费记录", OrderActivity.class));
            add(bonus);
//            add(new ProfileAdaptor.Item(R.drawable.profile_favorite, "我收藏的设备", FavoriteActivity.class));
            add(repair);
            add(new ProfileAdaptor.Item(R.drawable.profile_more, "更多", MoreActivity.class));
        }
    };
    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_schoolName)
    TextView tvSchoolName;
    @BindView(R.id.tv_notice_count)
    TextView tvNoticeCount;


    public ProfileFragment2() {
    }

    @SuppressLint("ValidFragment")
    public ProfileFragment2(IMainPresenter<IMainView> presenter, boolean isServerError) {
        this.presenter = presenter;
        this.isServerError = isServerError;
    }

    IMainPresenter<IMainView> presenter;
    boolean isServerError;

    ProfileAdaptor adaptor;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    private DecimalFormat df = new DecimalFormat("###.##");

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private String avatarUrl;  //  图片url



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    public void setAvatar(String pictureUrl) {
        if (ivAvatar == null) return;
        if (!TextUtils.isEmpty(pictureUrl)) {
            avatarUrl = pictureUrl;
            Glide.with(this).load(Constant.IMAGE_PREFIX + pictureUrl)
                    .asBitmap()
                    .placeholder(R.drawable.ic_picture_error)
                    .error(R.drawable.ic_picture_error)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.ic_picture_error);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptor = new ProfileAdaptor(getActivity(), R.layout.item_profile, items);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                        EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.START_ACTIVITY,
                                items.get(position).getActivityClazz()));
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.wtf(TAG, "数组越界", e);
                } catch (Exception e) {
                    Log.wtf(TAG, e);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.notice)
    void gotoNoticeList() {
        if (presenter.isLogin()) {
            startActivity(new Intent(mActivity, NoticeListActivity.class));
        } else {
            gotoLoginView();
        }
    }

    private boolean checkLogin() {
        if (presenter.getToken() == null ||  TextUtils.isEmpty(presenter.getToken())) {
            redirectToLogin();
            return false;
        }
        return true;
    }


    /**
     * 点击昵称 跳转到编辑个人信息页面
     */
    @OnClick({R.id.ll_user_info, R.id.iv_avatar})
    void onUserInfoClick() {
        if (checkLogin()) {
            Intent editInfo = new Intent(mActivity, EditProfileActivity.class);
            startActivityForResult(editInfo, START_EDIT_PROFILE);
        }
    }

    /**
     * 点击头像 跳转到编辑个人信息页面
     */
    @OnClick({R.id.iv_avatar, R.id.ll_user_info})
    void gotoLoginView() {
        if (!presenter.isLogin()) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void noticeEvent(NoticeEvent event) {
        if (tvNoticeCount == null) return;
        if (event.num > 0) {
            tvNoticeCount.setVisibility(View.VISIBLE);
            tvNoticeCount.setText(event.num+"");
        } else {
            tvNoticeCount.setVisibility(View.GONE);
        }
    }


    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PersonalExtraInfoDTO data) {
        try {
            wallet.setBalance(df.format(data.getAllBalance()));
            bonus.setBonusAmount(data.getBonusAmount());
            if (data.getCredits() == null) {
                credits.setBonusAmount(-1);
            } else {
                credits.setBonusAmount(data.getCredits());
            }

            getCertificationStatus(data.getStudentAuth());

        } catch (Exception e) {
            MobclickAgent.reportError(getContext(), "customReport tag: " + TAG + e.getMessage());
            wallet.setBalance(String.valueOf(data.getAllBalance()));
            bonus.setBonusAmount(data.getBonusAmount());
            if (data.getCredits() == null) {
                credits.setBonusAmount(-1);
            } else {
                credits.setBonusAmount(data.getCredits());
            }
        }
        if (data.isNeedShowDot()) {
            repair.setShowDot(true);
        } else {
            repair.setShowDot(false);
        }
        if (credits.getBonusAmount() != -1
                && !items.contains(credits)) {
            items.add(3, credits);
        }
        if (credits.getBonusAmount() == -1) {
            items.remove(credits);
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adaptor);
        } else {
            adaptor.notifyDataSetChanged();
        }

    }
    int certifyStatusType = -1  ;

    private void getCertificationStatus(int statusType){
        presenter.setCertifyStatus(statusType);
        String status ;
        certifyStatusType = statusType ;
        switch (statusType){
            case CERTIFICATION_NONE:
                status = CERTIFICATION_NONE_TXT ;
                legalize.setVerifiedStatus(status);
                legalize.setActivityClazz(UserCertificationActivity.class);
                break;
            case CERTIFICATION_FAILURE:
                status = CERTIFICATION_NONE_TXT ;
                legalize.setVerifiedStatus(status);
                legalize.setActivityClazz(UserCertificationStatusActivity.class);
                break;
            case CERTIFICATION_PASS:
                status = CERTIFICATION_PASS_TXT ;
                legalize.setVerifiedStatus(status);
                legalize.setActivityClazz(UserCertificationStatusActivity.class);
                break;
            case CERTIFICATION_REVIEWING:
                status = CERTIFICATION_NONE_TXT;
                legalize.setVerifiedStatus(status);
                legalize.setActivityClazz(UserCertificationStatusActivity.class);
                break;
                default:
                    break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (presenter == null) return;
        if (presenter.isLogin()) {
            presenter.getNoticeAmount();
            User user = presenter.getUserInfo();
            setAvatar(user.getPictureUrl());
            if (tvNickName != null) {
                if (!user.getNickName().equals(tvNickName.getText().toString()))
                    tvNickName.setText(user.getNickName());
            }

            if (tvSchoolName != null)
                tvSchoolName.setVisibility(View.GONE);
        } else {
            setAvatar("");
            if (tvNickName != null)
                tvNickName.setText("登录/注册");
            if (tvSchoolName != null)
                tvSchoolName.setVisibility(View.GONE);
        }
    }


    @Data
    public static class Event {
        private EventType type;
        private Object object;

        public Event(EventType type, Object object) {
            this.type = type;
            this.object = object;
        }

        public Event(EventType type) {
            this.type = type;
        }

        public enum EventType {
            /**
             * 改变动画
             */
            CHANGE_ANIMATION(1);

            EventType(int type) {
                this.type = type;
            }

            private int type;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new MainActivity.Event(MainActivity.Event.EventType.REFRESH_NOTICE));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Data
    public static class NoticeEvent {
        /**
         * 是否显示红点
         */
        private int num;


        public NoticeEvent(int num) {
            this.num = num;
        }


    }

}
