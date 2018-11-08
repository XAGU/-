package com.xiaolian.amigo.ui.lostandfound;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.di.componet.DaggerLostAndFoundActivityComponent;
import com.xiaolian.amigo.di.componet.LostAndFoundActivityComponent;
import com.xiaolian.amigo.di.module.LostAndFoundActivityModule;
import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.base.RxBus;
import com.xiaolian.amigo.ui.lostandfound.adapter.LostAndFoundDetailContentDelegate;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocalContentAdapter;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocialImgAdapter;
import com.xiaolian.amigo.ui.lostandfound.intf.IBlogPresenter;
import com.xiaolian.amigo.ui.lostandfound.intf.IBlogView;
import com.xiaolian.amigo.ui.widget.HideSmartLayout;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutFooter;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.photoview.AlbumItemActivity;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2.KEY_COMMENT_COUNT;
import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity2.KEY_LIKE;
import static com.xiaolian.amigo.ui.lostandfound.LostAndFoundDetailActivity2.KEY_ID;
import static com.xiaolian.amigo.ui.lostandfound.SocalFragment.REQUEST_CODE_DETAIL;
import static com.xiaolian.amigo.ui.lostandfound.SocalFragment.REQUEST_CODE_PHOTO;

/**
 * @author zcd
 * 社交联子界面
 */
public class BlogFragment extends BaseFragment implements IBlogView  , SocialImgAdapter.PhotoClickListener{


    private static final String KEY_TOPICID = "KEY_TOPICID";

    @Inject
    IBlogPresenter<IBlogView> presenter;
    @BindView(R.id.tv_empty_tip)
    TextView tvEmptyTip;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_error_tip)
    TextView tvErrorTip;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.hot_pot_title)
    TextView hotPotTitle;
    @BindView(R.id.hot_pots)
    RecyclerView hotPots;
    @BindView(R.id.pot_title)
    TextView potTitle;
    @BindView(R.id.pots)
    RecyclerView pots;
    @BindView(R.id.rl_content)
    LinearLayout rlContent;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.refreshLayout)
    HideSmartLayout refreshLayout;


    private LostAndFoundActivityComponent mActivityComponent;


    Unbinder unbinder;


    private  RefreshLayoutHeader header ;
    /**
     * 刷新数据
     */
    private int page = 1;
    private int size = 10;
    private int topicId;
    private String hotPosIds;

    //  是否滚动
    private boolean isReferTop = false;  //  ScrollView 定位到指定位置


    // 联子数据
    private List<LostAndFoundDTO> mPotsData;
    private List<LostAndFoundDTO> mHotPotsData;


    SocalContentAdapter mPotsAdapter ;
    SocalContentAdapter mHotPotsAdapter ;


    // 订阅设备返回的消息
    private Subscription busSubscriber;



    ScrollListener scrollListener ;

     public static BlogFragment newInstance(int topicId) {

        Bundle args = new Bundle();

        BlogFragment fragment = new BlogFragment();
        args.putInt(KEY_TOPICID ,topicId);
        fragment.setArguments(args);
        return fragment;
    }


    public BlogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public BlogFragment(int topicId , ScrollListener scrollListener) {

         this.topicId = topicId;
         this.scrollListener = scrollListener ;
    }


    public void setReferTop(boolean referTop) {
        this.isReferTop = referTop;
        presenter.getLostList("",1 ,"",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bolg, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivityComponent = DaggerLostAndFoundActivityComponent.builder()
                .lostAndFoundActivityModule(new LostAndFoundActivityModule(mActivity))
                .applicationComponent(((MvpApp) mActivity.getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        scrollView.setVerticalScrollBarEnabled(true);
        initRecyclerView();
        initRxbus();
        initPage();
        initScroll();
        return view;
    }


    private void initScroll(){
         scrollView.post(new Runnable() {
             @Override
             public void run() {
                 scrollView.scrollBy(0 , ScreenUtils.dpToPxInt(mActivity , 64));
             }
         });
         scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
             @Override
             public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    int onScrollDistance = scrollY - oldScrollY ;
                 Log.d(TAG ,"  " + onScrollDistance);
                        if (onScrollDistance > 0){  // 往下滑 ，tag标签往上走隐藏
                            scrollListener.onUpMove(onScrollDistance);
                            if (header != null)
                                header.getView().setVisibility(View.GONE);
                        }else{   // 往上滑， 标签往下走， 显示
                            scrollListener.onDownMove(onScrollDistance);
                        }
                    }
         });
    }



    private void initRxbus(){
        if (null == busSubscriber) {
            busSubscriber = RxBus.getDefault()
                    .toObservable(Intent.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handlePostData ,throwable -> {
                        Log.wtf(TAG, "接收从Activity返回的数据失败 thread" + Thread.currentThread().getName(), throwable);
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG ,"onActivityResult");
        if (requestCode == REQUEST_CODE_DETAIL) {
            if (data != null) {
                presenter.handleData(data);
            }
        }
    }

    /**
     * 收到从设备返回的数据，进行处理
     * @param data
     */
    private void handlePostData(Intent data){
        presenter.handleData(data);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (mPotsData== null && mHotPotsData == null || mPotsData.size() == 0 && mHotPotsData.size() ==0)
            requestNet();
    }

    private void initPage(){
        page = 1 ;
    }

    /**
     * 网络请求
     */
    protected void requestNet(){
        presenter.getLostList("", page, "", topicId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initRecyclerView() {
        header = new RefreshLayoutHeader(mActivity);
        hotPots.setNestedScrollingEnabled(false);
        pots.setNestedScrollingEnabled(false);
        ((DefaultItemAnimator) hotPots.getItemAnimator()).setSupportsChangeAnimations(false);
        ((DefaultItemAnimator) pots.getItemAnimator()).setSupportsChangeAnimations(false);
        mPotsData = new ArrayList<>();
        mHotPotsData = new ArrayList<>();
        mHotPotsAdapter = new SocalContentAdapter(mActivity, R.layout.item_socal, mHotPotsData, new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == -1 ) return ;
                presenter.setCurrentHotPosition(position);
                presenter.setCurrentChoosePosition(-1);
                Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                intent.putExtra(KEY_ID, mHotPotsData.get(position).getId());
                startActivityForResult(intent, REQUEST_CODE_DETAIL);
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
        mHotPotsAdapter.setPhotoClickListener(this);
        hotPots.setLayoutManager(new LinearLayoutManager(mActivity));
        hotPots.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
        hotPots.setAdapter(mHotPotsAdapter);

        mPotsData = new ArrayList<>();
        mPotsAdapter = new SocalContentAdapter(mActivity, R.layout.item_socal, mPotsData, new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == -1) return ;
                presenter.setCurrentHotPosition(-1);
                presenter.setCurrentChoosePosition(position);
                Intent intent = new Intent(mActivity, LostAndFoundDetailActivity2.class);
                intent.putExtra(KEY_ID, mPotsData.get(position).getId());
                startActivityForResult(intent, REQUEST_CODE_DETAIL);
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
        mPotsAdapter.setPhotoClickListener(this);
        pots.setLayoutManager(new LinearLayoutManager(mActivity));
        pots.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(mActivity, 21)));
        pots.setAdapter(mPotsAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                onLoadMoreContent();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onRefreshContent();
            }
        });
        refreshLayout.setRefreshHeader(header);
        header.getView().setVisibility(View.GONE);
        refreshLayout.setRefreshFooter(new RefreshLayoutFooter(mActivity));
        refreshLayout.setReboundDuration(200);
    }

    private void onLoadMoreContent() {
        page++;
        presenter.getLostList("", page, "", topicId);
    }

    private void onRefreshContent() {
        presenter.getLostList("", page, "", topicId);
    }

    @Override
    public void postEmpty() {
        if (mPotsData != null && mPotsAdapter != null) {
            mPotsData.clear();
            mPotsAdapter.notifyDataSetChanged();
        }
        potTitle.setVisibility(View.GONE);
        pots.setVisibility(View.GONE);
    }

    @Override
    public void hostPostsEmpty() {
        if (mHotPotsData != null && mHotPotsAdapter != null) {
            mHotPotsData.clear();
            mHotPotsAdapter.notifyDataSetChanged();
        }
        hotPotTitle.setVisibility(View.GONE);
        hotPots.setVisibility(View.GONE);
    }

    @Override
    public void referPost(List<LostAndFoundDTO> posts) {
        if (rlEmpty == null || rlContent == null || potTitle == null || pots == null) return ;
        rlEmpty.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        potTitle.setVisibility(View.VISIBLE);
        pots.setVisibility(View.VISIBLE);
        RxHelper.delay(100, TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (mPotsData != null && mPotsAdapter != null) {
                    mPotsData.clear();
                    mPotsData.addAll(posts);
                    mPotsAdapter.notifyDataSetChanged();
                }

                if (isReferTop) {
                    hotPots.post(new Runnable() {
                        @Override
                        public void run() {
                            if (scrollView == null) return;
                            int scrollHeight = hotPots.getHeight()  +
                                    hotPotTitle.getHeight() + ScreenUtils.dpToPxInt(mActivity, 43);
                            scrollView.scrollTo(0, scrollHeight);
                            pots.smoothScrollToPosition(0);
                            isReferTop = false;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void referHotPost(List<LostAndFoundDTO> hotPosts) {
        if (rlEmpty == null || rlError == null || rlContent == null  || hotPotTitle == null || hotPosts == null) return ;
        rlEmpty.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        rlContent.setVisibility(View.VISIBLE);
        hotPotTitle.setVisibility(View.VISIBLE);
        hotPots.setVisibility(View.VISIBLE);
        if (mHotPotsData != null && mHotPotsAdapter != null) {
            mHotPotsData.clear();
            mHotPotsData.addAll(hotPosts);
            mHotPotsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeHotAdapter(int currentHotPosition) {
        if (mHotPotsData== null  || mHotPotsData.size() == 0 && currentHotPosition>=mHotPotsData.size()||
                mHotPotsAdapter == null || currentHotPosition == -1) return ;
        mHotPotsData.remove(currentHotPosition);
        mHotPotsAdapter.notifyItemRemoved(currentHotPosition);
        mHotPotsAdapter.notifyItemRangeChanged(currentHotPosition, mHotPotsData.size());
    }

    @Override
    public void notifyHotAdapter(Intent data, int currentHotPosition) {
        if (mHotPotsData== null  || mHotPotsData.size() == 0 && currentHotPosition>=mHotPotsData.size()||
                mHotPotsAdapter == null || currentHotPosition == -1) return ;
        int liked = data.getIntExtra(KEY_LIKE, 0);
        int commentCount = data.getIntExtra(KEY_COMMENT_COUNT, 0);
        int oldLiked = mHotPotsData.get(currentHotPosition).getLiked();
        int likeCount;
        int oldLikeCount = mHotPotsData.get(currentHotPosition).getLikeCount();
        if (liked != oldLiked && liked != 0) {
            if (liked == 2) {
                likeCount = (oldLikeCount - 1) < 0 ? 0 :
                        mHotPotsData.get(currentHotPosition).getLikeCount() - 1;
            } else {
                likeCount = oldLikeCount + 1;
            }
            mHotPotsData.get(currentHotPosition).setLikeCount(likeCount);
            mHotPotsData.get(currentHotPosition).setLiked(liked);
        }
        mHotPotsData.get(currentHotPosition).setCommentsCount(commentCount);

        mHotPotsAdapter.notifyItemChanged(currentHotPosition , "aa");
    }

    @Override
    public void removePotItem(int currentChoosePosition) {
        if (mPotsData== null  || mPotsData.size() == 0 && currentChoosePosition>=mPotsData.size()||
                mPotsAdapter == null || currentChoosePosition == -1) return ;
        mPotsData.remove(currentChoosePosition);
        mPotsAdapter.notifyItemRemoved(currentChoosePosition);
        mPotsAdapter.notifyItemRangeChanged(currentChoosePosition, mPotsData.size());
    }

    @Override
    public void notifyPotAdapter(Intent data, int currentChoosePosition) {
        if (mPotsData== null  || mPotsData.size() == 0 && currentChoosePosition>=mPotsData.size()||
                mPotsAdapter == null || currentChoosePosition ==-1) return ;
        int liked = data.getIntExtra(KEY_LIKE, 0);
        int commentCount = data.getIntExtra(KEY_COMMENT_COUNT, 0);
        int oldLiked = mPotsData.get(currentChoosePosition).getLiked();
        int likeCount;
        int oldLikeCount = mPotsData.get(currentChoosePosition).getLikeCount();
        if (liked != oldLiked && liked != 0) {
            if (liked == 2) {
                likeCount = (oldLikeCount - 1) < 0 ? 0 :
                        mPotsData.get(currentChoosePosition).getLikeCount() - 1;
            } else {
                likeCount = oldLikeCount + 1;
            }
            mPotsData.get(currentChoosePosition).setLikeCount(likeCount);
            mPotsData.get(currentChoosePosition).setLiked(liked);
        }
        mPotsData.get(currentChoosePosition).setCommentsCount(commentCount);

        mPotsAdapter.notifyItemChanged(currentChoosePosition , "aa");
    }

    @Override
    public void loadMore(QueryLostAndFoundListRespDTO data) {
        if (data.getPosts() != null && mPotsData != null && data.getPosts().size() > 0) {
            this.mPotsData.addAll(data.getPosts());
            int positionStart = mPotsData.size() - data.getPosts().size();
            if (positionStart >= 0) {
                mPotsAdapter.notifyItemRangeInserted(positionStart, mPotsData.size());
            }
        } else {
        }
    }

    @Override
    public void onErrorView() {
        if (rlError == null || rlEmpty == null || rlContent == null) return ;
        rlError.setVisibility(View.VISIBLE);
        rlEmpty.setVisibility(View.GONE);
        rlContent.setVisibility(View.GONE);

    }

    @Override
    public void onEmpty() {
        if (rlEmpty != null)
        rlEmpty.setVisibility(View.VISIBLE);
        if (rlError != null)
        rlError.setVisibility(View.GONE);

        if (rlContent != null)
        rlContent.setVisibility(View.GONE);
    }

    @Override
    public void reducePage() {
        if (page > 1) {
            page--;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setReferComplete() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }


    @Override
    public void notifyAdapter(int position, boolean b) {
        if (mPotsAdapter != null) {
            mPotsAdapter.notifyItemChanged(position ,"bbb");
        }

        if (mHotPotsAdapter != null && position < 3) {
            mHotPotsAdapter.notifyItemChanged(position , "aaa");
        }
    }


    @Override
    public void photoClick(int position, ArrayList<String> datas) {
        Intent intent = new Intent(mActivity, AlbumItemActivity.class);
        intent.putExtra(AlbumItemActivity.EXTRA_CURRENT, position);
        intent.putStringArrayListExtra(AlbumItemActivity.EXTRA_TYPE_LIST, (ArrayList<String>) datas);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (busSubscriber != null) busSubscriber.unsubscribe();
    }


    interface ScrollListener{
        void onUpMove(int height) ;

        void onDownMove(int height);
    }
}
