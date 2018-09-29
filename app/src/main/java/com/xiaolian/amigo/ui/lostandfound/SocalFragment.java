package com.xiaolian.amigo.ui.lostandfound;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.base.RxBus;
import com.xiaolian.amigo.ui.lostandfound.adapter.BlogAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailContentDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalContentAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalTagsAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocialImgAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalView;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.ui.widget.SearchDialog2;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.GildeUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.SoftInputUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import butterknife.internal.ListenerClass;

import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static android.view.View.OVER_SCROLL_NEVER;
import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_ID;

/**
 * @author wcm
 * 2018/09/04
 */
public class SocalFragment extends BaseFragment implements View.OnClickListener, ISocalView, SocialImgAdapter.PhotoClickListener, BlogFragment.ScrollListener {

    public static final int REQUEST_CODE_DETAIL = 0x02;
    public static final int REQUEST_CODE_PHOTO = 0x03;

    private static final String TAG = SocalFragment.class.getSimpleName();
    @BindView(R.id.iv_remaind)
    ImageView ivRemaind;
    //    @BindView(R.id.title_border)
    View titleBorder;
    @BindView(R.id.tag_rl)
    RelativeLayout tagRl;
    @BindView(R.id.vp_blog_content)
    ViewPager vpBlogContent;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.fl_result_contain)
    FrameLayout flResultContain;
    @BindView(R.id.rl_result)
    RelativeLayout rlResult;

    private LostAndFoundActivityComponent mActivityComponent;

    private static final int REQUEST_CODE_PUBLISH = 0x0101;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.social_normal_rl)
    RelativeLayout socialNormalRl;
//    @BindView(R.id.social_tags)
//    RecyclerView socialTags;


    @BindView(R.id.social_tags)
    HorizontalScrollView socialTags;
    Unbinder unbinder;

    private RelativeLayout rlNotice;
    private TextView circle, collection, release;
    private View line1, line2;

    private List<BbsTopicListTradeRespDTO.TopicListBean> mSocialTagDatas = new ArrayList<>();

    private PopupWindow mPopupWindow;


    View rootView;
    View popView;

    @Inject
    ISocalPresenter<ISocalView> presenter;

//    private SearchDialog2 searchDialog;


    private String slectkey;


    private RecyclerView searchRecyclerView;

    private SocalContentAdapter searchAdaptor;

    private List<LostAndFoundDTO> searchData;

    private IMainPresenter<IMainView> mainPresenter;

    int scrollHeight = 0;  // 滚动的距离

    int tagRlHeight;

    int[] tagLocations = new int[2];

    boolean isCanMove = true;

    List<BaseFragment> blogFragments;
    BlogAdapter blogAdapter;
    FragmentManager fm;

    private int socialTagHeight;

    @SuppressLint("ValidFragment")
    public SocalFragment(IMainPresenter<IMainView> mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    public SocalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socal, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mActivityComponent = DaggerLostAndFoundActivityComponent.builder()
                .lostAndFoundActivityModule(new LostAndFoundActivityModule(mActivity))
                .applicationComponent(((MvpApp) mActivity.getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        initPop();
        getInitSocialTagHeight();
        initRecycler();
        initViewPager();
        getSocialTagHeight();

        return rootView;
    }

    /**
     * 获取socialTags的高度
     *
     * @return
     */
    public void getSocialTagHeight() {
        socialTagHeight = ScreenUtils.dpToPxInt(mActivity, 54) + ScreenUtils.dpToPxInt(mActivity, 10);
    }

    int screenWidth;

    private void initViewPager() {
        if (fm == null) fm = getChildFragmentManager();
        blogFragments = new ArrayList<>();
        blogAdapter = new BlogAdapter(fm, blogFragments);
        vpBlogContent.setAdapter(blogAdapter);
        screenWidth = ScreenUtils.getScreenWidth(mActivity);
        vpBlogContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean scrollRight;
            int lastPosition;
            int[] location = new int[2];

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                moveCursor(position);
                showTags();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == SCROLL_STATE_SETTLING) {
                    scrollRight = lastPosition < vpBlogContent.getCurrentItem();

                    lastPosition = vpBlogContent.getCurrentItem();
                    if (lastPosition + 1 < tags.size() && lastPosition - 1 >= 0) {
                        tags.get(scrollRight ? lastPosition + 1 : lastPosition - 1).getLocationOnScreen(location);
                        if (location[0] > screenWidth) {
                            socialTags.smoothScrollBy(screenWidth / 2, 0);
                        } else if (location[0] < 0) {
                            socialTags.smoothScrollBy(-screenWidth / 2, 0);
                        }
                    }

                }
            }
        });
    }

    /**   search  ***/
    /**
     * s
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @OnEditorAction(R.id.et_search_content)
    boolean search(TextView v, int actionId, KeyEvent event) {
        // 判断如果用户输入的是搜索键
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            this.dismiss();
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            presenter.getLostList("", 1, etSearchContent.getText().toString().trim(), 0);
            return true;
        }
        return false;
    }


    @OnClick(R.id.tv_cancel)
    void cancelSearch() {
        etSearchContent.setText("");
        searchRl.setVisibility(View.GONE);
        rlResult.setVisibility(View.GONE);
        socialNormalRl.setVisibility(View.VISIBLE);
        flResultContain.setVisibility(View.GONE);
        vpBlogContent.setVisibility(View.VISIBLE);
        SoftInputUtils.hideSoftInputFromWindow( mActivity, etSearchContent);
    }

    public void showSearch() {
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.dialog_search);
        searchRl.startAnimation(animation);
    }

    public void showNoResult(String selectKey) {
        flResultContain.setVisibility(View.GONE);
        rlResult.setVisibility(View.VISIBLE);
    }

    public void showResult(View view) {
        rlResult.setVisibility(View.GONE);
        if (flResultContain.getChildCount() > 0) {
            flResultContain.removeAllViews();
        }
        flResultContain.setVisibility(View.VISIBLE);
        flResultContain.addView(view);
    }


    /**
     * search
     ***/
    private void showTags() {

        ValueAnimator animator = ValueAnimator.ofInt(moveDistance, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) socialTags.getLayoutParams();
                layoutParams.setMargins(0, -currentValue, 0, 0);
                socialTags.setLayoutParams(layoutParams);
                moveDistance = currentValue;
            }
        });
        animator.setDuration(100);
        animator.start();
    }


    private void getInitSocialTagHeight() {
        tagRl.post(new Runnable() {
            @Override
            public void run() {
                tagRlHeight = tagRl.getHeight();
                tagRl.getLocationInWindow(tagLocations);
            }
        });
    }


    public void setReferTop(boolean referTop) {
        if (blogFragments == null || blogFragments.size() <= 0 || blogFragments.get(0) == null)
            return;
        moveCursor(0);
        vpBlogContent.setCurrentItem(0);
        ((BlogFragment) blogFragments.get(0)).setReferTop(true);
    }

    @Override
    protected void initData() {

    }

    /**
     * 滑动监听
     */
    private AnimatorSet.AnimatorListener moveListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isCanMove = true;
        }
    };

    @Override
    protected void initView() {
        if (mSocialTagDatas == null || mSocialTagDatas.size() == 0) presenter.getTopicList();
        mainPresenter.getNoticeAmount();
        presenter.getLostList("", 1, "", 0);

    }


    private List<ImageView> tags = new ArrayList<>();

    public void moveCursor(int position) {
        isCanMove = false;
        try {
            if (tags == null || tags.size() == 0 || tags.get(position) == null) return;
            ImageView imageView = tags.get(position);
            int middle = (imageView.getLeft() + imageView.getRight()) / 2;
            animWidthMove(middle);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * tag 移动动画
     * <p>
     * 第一步，将游标从A 点 宽度增加到B 点
     * 第二步  将游标从A 点 缩放到B 点，宽度减小，直至正常大小，为8dp
     *
     * @param moveLeft 是最终位置的中间位置
     */
    private void animWidthMove(int moveLeft) {

        moveLeft = moveLeft - ScreenUtils.dpToPxInt(mActivity, 4);
        int oldLeft = titleBorder.getLeft();
        int maxWidth;
        int oldWidth = ScreenUtils.dpToPxInt(mActivity, 8);
        boolean backMove = false;
        if (oldLeft < moveLeft) {
            backMove = true;
            maxWidth = moveLeft - oldLeft + oldWidth;
        } else {
            backMove = false;
            maxWidth = oldLeft - moveLeft + oldWidth;
        }
        if (maxWidth < oldWidth) {
            isCanMove = true;
            return;
        }
        if (backMove) {
            ValueAnimator widthAnim = ValueAnimator.ofInt(oldWidth, maxWidth);
            widthAnim.setDuration(100);
            widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleBorder.getLayoutParams();
                    layoutParams.width = currentValue;
                    layoutParams.height = titleBorder.getHeight();
                    titleBorder.setLayoutParams(layoutParams);
                }
            });

            widthAnim.start();
            ValueAnimator widthAnim2 = ValueAnimator.ofInt(maxWidth, oldWidth);
            widthAnim2.setDuration(100);
            widthAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                int initMarginStart = ((LinearLayout.LayoutParams) titleBorder.getLayoutParams()).getMarginStart();

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleBorder.getLayoutParams();
                    layoutParams.setMarginStart(initMarginStart + (maxWidth - currentValue));
                    layoutParams.width = currentValue;
                    layoutParams.height = titleBorder.getHeight();
                    titleBorder.setLayoutParams(layoutParams);
                }
            });

            widthAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    widthAnim2.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            ValueAnimator widthAnim = ValueAnimator.ofInt(oldWidth, maxWidth);
            widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                int initMarginStart = ((LinearLayout.LayoutParams) titleBorder.getLayoutParams()).getMarginStart();

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleBorder.getLayoutParams();
                    layoutParams.setMarginStart(initMarginStart - currentValue + oldWidth);
                    layoutParams.width = currentValue;
                    layoutParams.height = titleBorder.getHeight();
                    titleBorder.setLayoutParams(layoutParams);
                }
            });
            ValueAnimator widthAnim2 = ValueAnimator.ofInt(maxWidth, oldWidth);
            widthAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleBorder.getLayoutParams();
                    layoutParams.width = currentValue;
                    layoutParams.height = titleBorder.getHeight();
                    titleBorder.setLayoutParams(layoutParams);
                }
            });
            animatorSet.addListener(moveListener);
            animatorSet.setDuration(200);
            animatorSet.playSequentially(widthAnim, widthAnim2);
            animatorSet.start();
        }
    }


    @OnClick(R.id.search)
    public void showSearchRl() {
        search();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETAIL) {
            if (data != null) {
                RxBus.getDefault().post(data);
            }
        }
    }

    SocalTagsAdapter adapter;


    /**
     * 初始化横向滚动的tag
     */
    private void initRecycler() {

        titleBorder = new View(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = ScreenUtils.dpToPxInt(mActivity, 8);
        layoutParams.height = ScreenUtils.dpToPxInt(mActivity, 2);
        layoutParams.setMargins(ScreenUtils.dpToPxInt(mActivity, 45), ScreenUtils.dpToPxInt(mActivity, 6)
                , 0, 0);
        titleBorder.setLayoutParams(layoutParams);
        titleBorder.setBackgroundResource(R.drawable.red_cursor);
    }

    void search() {
        searchRl.setVisibility(View.VISIBLE);
        showSearch();
        socialNormalRl.setVisibility(View.GONE);
        SoftInputUtils.showSoftInputFromWindow(getActivity() ,etSearchContent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null) {

            if (mPopupWindow.isShowing()) mPopupWindow.dismiss();

            mPopupWindow = null;
        }

        presenter.onDetach();
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
//        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        rlNotice = popView.findViewById(R.id.rl_notice);
        circle = popView.findViewById(R.id.circle);
        release = popView.findViewById(R.id.release);
        line1 = popView.findViewById(R.id.v_line1);
        line2 = popView.findViewById(R.id.v_line2);
        collection = popView.findViewById(R.id.collection);
        rlNotice.setOnClickListener(this);
        collection.setOnClickListener(this);
        release.setOnClickListener(this);


    }


    @OnClick(R.id.more)
    public void showPop() {
        if (mPopupWindow == null) return;
        if (popView == null) return;
        // 相对于 + 号正下面，同时可以设置偏移量
        showNoticeNumRemind(presenter.getNoticeCount());
        showOrHideCommentView();
        mPopupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, (ScreenUtils.getScreenWidth(mActivity) - ScreenUtils.dpToPxInt(mActivity, 117)), ScreenUtils.dpToPxInt(mActivity, 75));
        // 设置pop关闭监听，用于改变背景透明度
    }

    public void hideNoticeNumRemind() {
        if (circle == null) return;
        circle.setVisibility(View.GONE);
    }

    public void showNoticeNumRemind(int num) {
        if (circle == null) return;
        if (num > 0) {
            circle.setText(num + "");
            circle.setVisibility(View.VISIBLE);
        } else {
            circle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showSearchResult(List<LostAndFoundDTO> wappers) {
        flResultContain.setVisibility(View.VISIBLE);
        vpBlogContent.setVisibility(View.GONE);
        if (searchData == null) searchData = new ArrayList<>();
        if (searchRecyclerView == null) {
            searchRecyclerView = new RecyclerView(mActivity);

            searchRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
            searchAdaptor = new SocalContentAdapter(mActivity, R.layout.item_socal, searchData, new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                    intent.putExtra(KEY_ID, searchData.get(position).getId());
                    mActivity.startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            }, new LostAndFoundDetailContentDelegate.OnLikeClickListener() {
                @Override
                public void onLikeClick(int position, long id, boolean like) {
                    if (like) {
                        presenter.unLikeComment(position, id, true);
                    } else {
                        presenter.likeComment(position, id, true);
                    }
                }
            });
            searchAdaptor.setPhotoClickListener(this);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            searchRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
            searchRecyclerView.setAdapter(searchAdaptor);
        }
        searchData.clear();
        for (LostAndFoundDTO wapper : wappers) {
            wapper.setCommentEnable(false);
        }
        searchData.addAll(wappers);
        searchAdaptor.notifyDataSetChanged();
        showResult(searchRecyclerView);
    }

    @Override
    public void showNoSearchResult(String selectKey) {
        showNoResult(selectKey);
    }

    @Override
    public void notifyAdapter(int position, boolean b, boolean b1) {
        if (searchAdaptor != null) {
            searchAdaptor.notifyItemChanged(position, "aaa");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notice:
                hideNoticeRemind();
                hideNoticeNumRemind();
                mPopupWindow.dismiss();
                startActivity(new Intent(mActivity, LostAndFoundNoticeActivity.class));
                break;
            case R.id.collection:
                mPopupWindow.dismiss();
                startActivity(new Intent(mActivity, MyCollectActivity.class));
                break;
            case R.id.release:
                mPopupWindow.dismiss();
                startActivityForResult(
                        new Intent(mActivity, MyPublishActivity2.class), REQUEST_CODE_PUBLISH);
                break;
        }
    }

    @Override
    public void referTopic(BbsTopicListTradeRespDTO data) {
//        if (adapter == null) return;
        if (data == null || data.getTopicList() == null || data.getTopicList().size() == 0) return;
        if (this.mSocialTagDatas != null && this.mSocialTagDatas.size() > 0) {
            this.mSocialTagDatas.clear();
        }
        if (mSocialTagDatas.size() == 0) {
            this.mSocialTagDatas.add(new BbsTopicListTradeRespDTO.TopicListBean(0));
        }
        if (mSocialTagDatas.size() == 1) {
            this.mSocialTagDatas.addAll(data.getTopicList());
        }
        initTAG(mSocialTagDatas);
        referFragment(mSocialTagDatas);
    }

    LinearLayout linearLayout;

    LinearLayout linearLayout1;

    LinearLayout.LayoutParams layoutParams;

    LinearLayout.LayoutParams layoutParams1;

    private void initTAG(List<BbsTopicListTradeRespDTO.TopicListBean> mSocialTagDatas) {
        if (socialTags == null) return;
        if (socialTags.getChildCount() > 0) socialTags.removeAllViews();

        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (layoutParams1 == null) {
            layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtils.dpToPxInt(mActivity, 50));
        }

        if (linearLayout == null) {
            linearLayout = new LinearLayout(mActivity);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundResource(R.color.white);
            linearLayout.setLayoutParams(layoutParams);
        }
        if (linearLayout1 == null) {
            linearLayout1 = new LinearLayout(mActivity);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            linearLayout1.setLayoutParams(layoutParams);
        }

        if (linearLayout.getChildCount() > 0)
            linearLayout.removeAllViews();
        for (int i = 0; i < mSocialTagDatas.size(); i++) {
            BbsTopicListTradeRespDTO.TopicListBean topicListBean = mSocialTagDatas.get(i);
            ImageView imageView = new ImageView(mActivity);
            if (TextUtils.isEmpty(topicListBean.getIcon())) {
                imageView.setBackgroundResource(R.drawable.shishi);
                layoutParams.setMargins(ScreenUtils.dpToPxInt(mActivity, 5), 0, ScreenUtils.dpToPxInt(mActivity, 5), 0);
                imageView.setLayoutParams(layoutParams);
            } else {
                if (i == mSocialTagDatas.size() - 1) {
                    GildeUtils.setNoErrorImage(mActivity, imageView, topicListBean.getIcon(), ScreenUtils.dpToPx(mActivity, 50), true);
                } else {
                    GildeUtils.setNoErrorImage(mActivity, imageView, topicListBean.getIcon(), ScreenUtils.dpToPx(mActivity, 50), false);
                }
            }
            imageView.setTag(topicListBean.getTopicId());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCursor(topicListBean.getTopicId());
                    vpBlogContent.setCurrentItem(topicListBean.getTopicId());
                }
            });
            linearLayout.addView(imageView);
            tags.add(imageView);
        }
        if (linearLayout1.getChildCount() > 0)
            linearLayout1.removeAllViews();

        linearLayout1.addView(linearLayout);
        linearLayout1.addView(titleBorder);

        socialTags.addView(linearLayout1);
    }

    /**
     * 根据标签添加Fragment
     */
    public void referFragment(List<BbsTopicListTradeRespDTO.TopicListBean> data) {
        if (blogFragments == null && blogAdapter == null) return;
        if (blogFragments.size() > 0) blogFragments.clear();

        for (BbsTopicListTradeRespDTO.TopicListBean topicListBean : data) {
            blogFragments.add(new BlogFragment(topicListBean.getTopicId(), this));
        }
        blogAdapter.notifyDataSetChanged();

    }


    @Override
    public void showNoticeRemind(int num) {
        if (presenter.isCommentEnable()) {
            ivRemaind.setVisibility(View.VISIBLE);
            if (circle != null) {
                if (num > 0) {
                    circle.setVisibility(View.VISIBLE);
                    circle.setText(num + "");

                }
            }
        } else {
            ivRemaind.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideNoticeRemind() {
        ivRemaind.setVisibility(View.GONE);
        if (circle != null) {
            circle.setVisibility(View.GONE);
        }
    }

    /**
     * 显示或者隐藏commentView
     */
    public void showOrHideCommentView() {
        if (presenter.isCommentEnable()) {
            if (release != null) {
                release.setVisibility(View.VISIBLE);
            }

            if (collection != null) {
                collection.setVisibility(View.VISIBLE);
            }

            if (rlNotice != null) {
                rlNotice.setVisibility(View.VISIBLE);

            }
        } else {
            if (release != null) {
                release.setVisibility(View.VISIBLE);
            }

            if (collection != null) {
                collection.setVisibility(View.GONE);
            }
            if (rlNotice != null) {
                rlNotice.setVisibility(View.GONE);
            }

            line2.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);

        }
    }

    @Override
    public void hideCommentView() {
        if (rlNotice != null) {
            rlNotice.setVisibility(View.GONE);
        }

        if (release != null) {
            release.setVisibility(View.GONE);
        }

        if (collection != null) {
            collection.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCommentView() {
        if (rlNotice != null) {
            rlNotice.setVisibility(View.VISIBLE);
        }

        if (release != null) {
            release.setVisibility(View.VISIBLE);
        }

        if (collection != null) {
            collection.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void photoClick(int position, ArrayList<String> datas) {
        Intent intent = new Intent(mActivity, AlbumItemActivity.class);
        intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, position);
        intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, (ArrayList<String>) datas);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }


    /**
     * 滚动参数
     */
    int moveDistance = 0;

    /**
     * 滚动参数
     **/

    @Override
    public void onUpMove(int height) {
        if (socialTags == null || height < 3) return;

        if (moveDistance < socialTagHeight) {
            moveDistance += height;

            if (moveDistance < socialTagHeight) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) socialTags.getLayoutParams();
                layoutParams.setMargins(0, -moveDistance, 0, 0);
                socialTags.setLayoutParams(layoutParams);
//              moveVP(moveDistance);
            } else {
                moveDistance = socialTagHeight;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) socialTags.getLayoutParams();
                layoutParams.setMargins(0, -moveDistance, 0, 0);
                socialTags.setLayoutParams(layoutParams);
//                moveVP(moveDistance);
            }
        }
    }

    @Override
    public void onDownMove(int height) {

        if (socialTags == null || Math.abs(height) < 3) return;

        if (moveDistance > 0) {
            moveDistance += height;

            if (moveDistance > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) socialTags.getLayoutParams();
                layoutParams.setMargins(0, -moveDistance, 0, 0);
                socialTags.setLayoutParams(layoutParams);
            } else {
                moveDistance = 0;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) socialTags.getLayoutParams();
                layoutParams.setMargins(0, -moveDistance, 0, 0);
                socialTags.setLayoutParams(layoutParams);
            }

        }

    }
}
