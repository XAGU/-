package com.xiaolian.amigo.ui.lostandfound;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.BbsTopicListTradeRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.intf.OnItemClickListener;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailContentDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalContentAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalTagsAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.ISocalView;
import com.xiaolian.amigo.ui.widget.SearchDialog2;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.dialog.SearchDialog;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
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

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_ID;

/**
 * @author wcm
 * 2018/09/04
 */
public class SocalFragment extends BaseFragment implements View.OnClickListener, ISocalView {
    private static final String TAG = SocalFragment.class.getSimpleName();
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_content)
    LinearLayout rlContent;
    @BindView(R.id.iv_remaind)
    ImageView ivRemaind;
    @BindView(R.id.new_blog)
    TextView newBlog;

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
    @BindView(R.id.search_txt)
    EditText searchTxt;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.social_tags)
    RecyclerView socialTags;


    Unbinder unbinder;
    @BindView(R.id.title_border)
    View titleBorder;
    @BindView(R.id.title_fl)
    FrameLayout titleFl;
    @BindView(R.id.social_recy)
    RecyclerView socialRecy;
    @BindView(R.id.tv_empty_tip)
    TextView tvEmptyTip;
    @BindView(R.id.tv_error_tip)
    TextView tvErrorTip;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.social_new)
    RecyclerView socialNew;

    private RelativeLayout rlNotice;
    private TextView circle, collection, release;

    private List<BbsTopicListTradeRespDTO.TopicListBean> mSocialTagDatas = new ArrayList<>();

    private PopupWindow mPopupWindow;

    int screenWidth;

    float screentHeight;

    View rootView;
    View popView;

    // RecyclerVew
    private SocalContentAdapter socalContentAdapter;
    private List<LostAndFoundDTO> mDatas;
    private List<LostAndFoundDTO> mNewContents;
    private SocalContentAdapter socalNewContentAdapter;
    private boolean autoRefresh;

    @Inject
    ISocalPresenter<ISocalView> presenter;

    private SearchDialog2 searchDialog;

    private int page = 1;
    private int size = 10;

    private int topicId;

    private String slectkey;

    private String hotPosIds;

    private RecyclerView searchRecyclerView ;

    private SocalContentAdapter searchAdaptor ;

    private  List<LostAndFoundDTO> searchData ;

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
        initRecycler();
        initView();
        return rootView;
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        presenter.getTopicList();
        presenter.getLostList(hotPosIds, page, slectkey, topicId);
        presenter.fetchNoticeCount();
    }


    @OnClick(R.id.cancel_search)
    public void showNormalRl() {
        socialNormalRl.setVisibility(View.VISIBLE);
        searchRl.setVisibility(View.GONE);
        SoftInputUtils.hideSoftInputFromWindow(mActivity, searchTxt);
    }

    @OnClick(R.id.search)
    public void showSearchRl() {
//        socialNormalRl.setVisibility(View.GONE);
//        searchRl.setVisibility(View.VISIBLE);
//        showSoftInputFromWindow(mActivity, searchTxt);
        search();
    }


//    /**
//     * EditText获取焦点并显示软键盘
//     */
//    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//        InputMethodManager inputManager =
//                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(editText, 0);
//    }

//    @OnEditorAction(R.id.search_txt)
//    boolean search(EditText v, int actionId, KeyEvent event) {
//        // 判断如果用户输入的是搜索键
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////            this.dismiss();
//            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            presenter.getLostList("", 1, v.getText().toString(), 0);
//            return true;
//        }
//        return false;
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CODE_PUBLISH) {
////                refreshLostAndFound();
//                onRefresh();
//            } else if (requestCode == REQUEST_CODE_DETAIL) {
//                if (data != null && currentChoosePosition != -1) {
//                    int viewCount = data.getIntExtra(KEY_VIEW_COUNT, 0);
//                    int commentCount = data.getIntExtra(KEY_COMMENT_COUNT, 0);
//                    lostAndFounds.get(currentChoosePosition).setViewCount(viewCount);
//                    lostAndFounds.get(currentChoosePosition).setCommentCount(commentCount);
//                    currentChoosePosition = -1;
//                    adaptor.notifyDataSetChanged();
//                }
//            }
//        } else {
//            adaptor.notifyDataSetChanged();
//        }
    }

    SocalTagsAdapter adapter;

    /**
     * 初始化横向滚动的tag
     */
    private void initRecycler() {
        initScreen();
        mSocialTagDatas.add(new BbsTopicListTradeRespDTO.TopicListBean());
        adapter = new SocalTagsAdapter(mActivity, mSocialTagDatas, socialTags, new OnItemClickListener() {
            @Override
            public void click(int poisition) {
                if (poisition == 0) {
                    refreshLayout.autoRefresh();
                    presenter.getLostList("", 1, "", 0);
                } else {
                    topicId = mSocialTagDatas.get(poisition).getTopicId();
                    refreshLayout.autoRefresh();
                    presenter.getLostList("", 1, "", topicId);
                }
            }
        });
        socialTags.setAdapter(adapter);

        initContentRecycler();
    }

    private void initScreen() {

        screentHeight = ScreenUtils.getScreenHeight(mActivity);
        screenWidth = ScreenUtils.getScreenWidth(mActivity);
    }

    void search() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog2(mActivity);
            searchDialog.setSearchListener(searchStr -> {
                presenter.getLostList("", 1, searchStr, 0);
            });
            searchDialog.setCanceledOnTouchOutside(true);
            searchDialog.setCancelable(true);
            searchDialog.setOnDismissListener(dialog -> {
                searchData.clear();
//                ablActionbar.setExpanded(true);
            });
        }
//        ablActionbar.setExpanded(false);
        searchDialog.show();
    }

    private void initContentRecycler() {
        setAutoRefresh(true);
        initSocialContentAdapter();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                onLoadMoreContent();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onRefreshContent();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(mActivity));
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(mActivity));
        refreshLayout.setReboundDuration(200);
        if (autoRefresh) {
            refreshLayout.autoRefresh(20);
        }
    }

    protected void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    private void onLoadMoreContent() {
        page++;
        presenter.getLostList("", page, "", topicId);
    }


    private void onRefreshContent() {
        presenter.getLostList("", 1, "", topicId);
    }


    private void initSocialContentAdapter() {

        mDatas = new ArrayList<>();
        socalContentAdapter = new SocalContentAdapter(mActivity, R.layout.item_socal, mDatas, new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                intent.putExtra(KEY_ID, mDatas.get(position).getId());
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
                    presenter.unLikeComment(position, id);
                } else {
                    presenter.likeComment(position, id);
                }
            }
        });
        socialRecy.setLayoutManager(new LinearLayoutManager(mActivity));
        socialRecy.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
        socialRecy.setAdapter(socalContentAdapter);

        mNewContents = new ArrayList<>();
        socalNewContentAdapter = new SocalContentAdapter(mActivity, R.layout.item_socal, mNewContents, new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                intent.putExtra(KEY_ID, mNewContents.get(position).getId());
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
                    presenter.unLikeComment(position, id);
                } else {
                    presenter.likeComment(position, id);
                }
            }
        });
        socialNew.setLayoutManager(new LinearLayoutManager(mActivity));
        socialNew.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
        socialNew.setAdapter(socalNewContentAdapter);
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


    @OnClick(R.id.more)
    public void showPop() {
        if (mPopupWindow == null) return;
        if (popView == null) return;
        // 相对于 + 号正下面，同时可以设置偏移量
        showNoticeNumRemind(presenter.getNoticeCount());
        mPopupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, (screenWidth - ScreenUtils.dpToPxInt(mActivity, 117)), ScreenUtils.dpToPxInt(mActivity, 75));


        // 设置pop关闭监听，用于改变背景透明度
    }

    public void hideNoticeNumRemind() {
        if (circle == null) return;
        circle.setVisibility(View.GONE);
    }

    public void showNoticeNumRemind(int num) {
        if (circle == null) return;
        if (num > 0) {
            circle.setText(num+"");
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
    public void showSearchResult(List<LostAndFoundDTO>  wappers) {
        if (searchData == null) searchData = new ArrayList<>();
        if (searchRecyclerView == null) {
            searchRecyclerView = new RecyclerView(mActivity);
            searchAdaptor = new SocalContentAdapter(mActivity, R.layout.item_socal, searchData, new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                    intent.putExtra(KEY_ID, mDatas.get(position).getId());
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
                        presenter.unLikeComment(position, id);
                    } else {
                        presenter.likeComment(position, id);
                    }
                }
            });
            socialRecy.setLayoutManager(new LinearLayoutManager(mActivity));
            socialRecy.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
            socialRecy.setAdapter(socalContentAdapter);
        }else {

            searchData.clear();
            searchData.addAll(wappers);
            searchAdaptor.notifyDataSetChanged();
            searchDialog.showResult(searchRecyclerView);
        }
    }

    @Override
    public void showNoSearchResult(String selectKey) {
        searchDialog.showNoResult(selectKey);
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
        if (adapter == null) return;
        if (data == null || data.getTopicList() == null || data.getTopicList().size() == 0) return;
        if (this.mSocialTagDatas != null && this.mSocialTagDatas.size() > 0) {
            this.mSocialTagDatas.clear();
        }
        if (mSocialTagDatas.size() == 0) {
            this.mSocialTagDatas.add(new BbsTopicListTradeRespDTO.TopicListBean());
        }
        this.mSocialTagDatas.addAll(data.getTopicList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setReferComplete() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void referTopicList(QueryLostAndFoundListRespDTO data) {
        if (data.getHotPosts() != null && socalContentAdapter != null && data.getHotPosts().size() > 0) {
            if (mDatas != null && mDatas.size() > 0) {
                this.mDatas.clear();
            }
            this.mDatas.addAll(data.getHotPosts());
            socalContentAdapter.notifyDataSetChanged();
        }

        if (data.getPosts() != null && socalNewContentAdapter != null && data.getPosts().size() > 0) {
            if (mNewContents != null && mNewContents.size() > 0) {
                this.mNewContents.clear();
            }
            this.mNewContents.addAll(data.getPosts());
            socalNewContentAdapter.notifyDataSetChanged();
            if (rlContent != null && rlEmpty != null && rlEmpty != null) {
                rlContent.setVisibility(View.VISIBLE);
                rlError.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.GONE);
            }
            newBlog.setVisibility(View.VISIBLE);
        } else {
            newBlog.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadMore(QueryLostAndFoundListRespDTO data) {
        if (data.getPosts() != null && socalNewContentAdapter != null && data.getPosts().size() > 0) {
            this.mNewContents.addAll(data.getPosts());
            socalNewContentAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    public void reducePage() {
        if (page > 1) {
            page--;
        }
    }


    @Override
    public void onErrorView() {
        rlError.setVisibility(View.VISIBLE);
        rlEmpty.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);

    }

    @Override
    public void onEmpty() {
        rlEmpty.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);
    }

    @Override
    public void notifyAdapter(int position, boolean b) {
        if (socalNewContentAdapter != null) {
            socalNewContentAdapter.notifyItemChanged(position);
        }

        if (socalContentAdapter != null && position < 3) {
            socalContentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void showNoticeRemind(int num) {
        ivRemaind.setVisibility(View.VISIBLE);
        if (circle != null) {
            if (num > 0) {
                circle.setVisibility(View.VISIBLE);
                circle.setText(num + "");

            }
        }
    }

    @Override
    public void hideNoticeRemind() {
        ivRemaind.setVisibility(View.GONE);
        if (circle != null) {
            circle.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideCommentView() {
        if (rlNotice != null) {
            rlNotice.setVisibility(View.GONE);
        }

        if ( release!= null) {
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


}
