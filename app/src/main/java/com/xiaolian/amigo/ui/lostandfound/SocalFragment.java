package com.xiaolian.amigo.ui.lostandfound;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.widget.RecyclerItemClickListener;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author wcm
 * 2018/09/04
 */
public class SocalFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SocalFragment.class.getSimpleName();

    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.social_normal_rl)
    RelativeLayout socialNormalRl;
    @BindView(R.id.search_txt)
    TextView searchTxt;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.social_tags)
    RecyclerView socialTags;


    Unbinder unbinder;
    @BindView(R.id.title_border)
    View titleBorder;
    @BindView(R.id.title_fl)
    FrameLayout titleFl;
    @BindView(R.id.circle_doit)
    LinearLayout circleDoit;

    private RelativeLayout rlNotice;
    private TextView circle, collection, release;

    private CommonAdapter<String> socialTagAdatper;
    private List<String> mSocialTagDatas = new ArrayList<>();

    private PopupWindow mPopupWindow;

    int screenWidth;

    float screentHeight;

    View rootView;
    View popView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socal, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initRecycler();
        return rootView;
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化横向滚动的tag
     */
    private void initRecycler() {
        initScreen();
        for (int i = 0; i < 10; i++) {
            mSocialTagDatas.add("");
        }

        socialTagAdatper = new CommonAdapter<String>(mActivity, R.layout.item_social_tag, mSocialTagDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        socialTags.setLayoutManager(linearLayoutManager);
        socialTags.setAdapter(socialTagAdatper);
        socialTags.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, socialTags, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, MotionEvent e) {
                onSuccess(position + "");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        addDiot();
        initPop();
    }

    private void initScreen() {
        WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screentHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    public void initPop() {
        // 设置布局文件
        mPopupWindow = new PopupWindow(mActivity);
        popView = LayoutInflater.from(mActivity).inflate(R.layout.pop_social_more, null);
        mPopupWindow.setContentView(popView);
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        rlNotice = popView.findViewById(R.id.rl_notice);
        circle = popView.findViewById(R.id.circle);
        release = popView.findViewById(R.id.release);
        collection = popView.findViewById(R.id.collection);
        rlNotice.setOnClickListener(this);
        collection.setOnClickListener(this);
        release.setOnClickListener(this);
    }

    private void addDiot() {
        socialTags.post(new Runnable() {
            @Override
            public void run() {
                int width = socialTags.getWidth();
                int diotNum = (width / screenWidth) + 1;
                for (int i = 0; i < diotNum; i++) {
                    View view = new View(mActivity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.width = (int) ScreenUtils.dpToPx(mActivity , 5);
                    layoutParams.height = (int) ScreenUtils.dpToPx(mActivity , 5);
                    layoutParams.rightMargin = (int) ScreenUtils.dpToPx(mActivity , 10);
                    view.setBackgroundResource(R.drawable.dot_circle_red);
                    circleDoit.addView(view);
                }
            }
        });
    }


    @OnClick(R.id.more)
    public void showPop() {
        if (mPopupWindow == null) return;
        if (popView == null) return;
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, (screenWidth - ScreenUtils.dpToPxInt(mActivity, 117)), ScreenUtils.dpToPxInt(mActivity, 75));
        // 设置pop关闭监听，用于改变背景透明度
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notice:
                break;
            case R.id.collection:
                break;
            case R.id.release:
                break;
        }
    }
}
