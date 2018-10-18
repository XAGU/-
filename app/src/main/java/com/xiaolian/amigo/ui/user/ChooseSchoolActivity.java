package com.xiaolian.amigo.ui.user;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.school.IndexBar.helper.IIndexBarDataHelper;
import com.xiaolian.amigo.ui.widget.school.IndexBar.helper.IndexBarDataHelperImpl;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;
import com.xiaolian.amigo.ui.widget.school.suspension.SuspensionDecoration;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.util.SoftInputUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * @author wcm
 * @data 18/10/11
 */
public class ChooseSchoolActivity extends BaseActivity implements IChooseSchoolView {


    @Inject
    IChooseSchoolPresenter<IChooseSchoolView> presenter;
    @BindView(R.id.school_ry)
    RecyclerView schoolRy;
    @BindView(R.id.search)
    EditText search;
    //    @BindView(R.id.indexBar)
//    IndexBar indexBar;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title_right)
    RecyclerView titleRight;
    @BindView(R.id.search_cy)
    RecyclerView searchCy;
    @BindView(R.id.cancle)
    TextView cancle;
    @BindView(R.id.online_school)
    RecyclerView onlineSchool;
    @BindView(R.id.right_rl)
    RelativeLayout rightRl;
    @BindView(R.id.school)
    LinearLayout school;

    private SuspensionDecoration mDecoration;


    private LinearLayoutManager manager;


    private List<CityBean> cityBeans = new ArrayList<>();

    private CommonAdapter<CityBean> commonAdapter;

    private Unbinder unbinder;


    private UserActivityComponent mActivityComponent;

    private CommonAdapter<String> titleRightAdapter;

    private List<String> rightTitles;

    private IIndexBarDataHelper mDataHelper;

    private int lastSelectPosition = 0;

    private CommonAdapter<CityBean> searchAdapter;

    private List<CityBean> searchSchools;


    private int searchWidth;

    private int cancelWidth;


    private ValueAnimator translateAnimator;

    private LinearLayout.LayoutParams searchLayoutParams;


    private List<CityBean> onLineSchools;

    private CommonAdapter<CityBean> onLineAdapter;


    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        mActivityComponent.inject(this);
        presenter.onAttach(this);
//        SoftInputUtils.showSoftInputFromWindow(this,  search);
        search.setOnEditorActionListener((v, actionId, event) -> {
                    onSuccess(search.getText().toString());
                    return false;
                }
        );
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //Log.d("STATE", appBarLayout.getTotalScrollRange() +"//"+ verticalOffset+"//"+tv_toolbar_title.getHeight());
            if (verticalOffset < -(tvToolbarTitle.getHeight() + llHeader.getPaddingTop())) {
                setTitleVisiable(View.VISIBLE);
            } else {
                setTitleVisiable(View.GONE);
            }
        });
        mDataHelper = new IndexBarDataHelperImpl();

        search.post(() -> searchWidth = search.getWidth());

//        cancle.post(() -> cancelWidth = cancle.getWidth());
        cancelWidth = ScreenUtils.dpToPxInt(this, 50);
        search.setCursorVisible(false);
        searchLayoutParams = (LinearLayout.LayoutParams) search.getLayoutParams();
        initSchoolRy();
    }


    private void setTitleVisiable(int visiable) {
        tvTitle.setVisibility(visiable);
    }

    protected void initInject() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
    }

    private void initSchoolRy() {
        searchSchools = new ArrayList<>();
        onLineSchools = new ArrayList<>();
        manager = new LinearLayoutManager(this);
        schoolRy.addItemDecoration(mDecoration = new SuspensionDecoration(this, cityBeans));
        mDecoration.setmDatas(cityBeans);
        schoolRy.setAdapter(commonAdapter = new CommonAdapter<CityBean>(this, R.layout.item_school_name, cityBeans) {

            @Override
            protected void convert(ViewHolder holder, CityBean cityBean, int position) {
                holder.setText(R.id.school_name, cityBean.getCity());
                if (cityBean.getCity().length() == 1) {
                    holder.getView(R.id.line).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
                }
            }
        });
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                presenter.updataSchool(cityBeans.get(position).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        schoolRy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                String tag = mDataHelper.getIndexTag(cityBeans.get(firstVisibleItem));
                try {
                    int index = rightTitles.indexOf(tag);
                    if (index != -1) {
                        if (titleRightAdapter != null) {
                            lastSelectPosition = index;
                            titleRightAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    Log.wtf(TAG, e.getMessage());
                }
            }
        });
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                presenter.updataSchool(cityBeans.get(position).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        schoolRy.setLayoutManager(manager);


        searchCy.setAdapter(searchAdapter = new CommonAdapter<CityBean>(this, R.layout.item_school_name, searchSchools) {

            @Override
            protected void convert(ViewHolder holder, CityBean school, int position) {
                holder.setText(R.id.school_name, school.getCity());
                holder.getView(R.id.line).setVisibility(View.VISIBLE);
//
            }
        });
        searchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                presenter.updataSchool(searchSchools.get(position).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        searchCy.setLayoutManager(new LinearLayoutManager(this));


        onlineSchool.setAdapter(onLineAdapter = new CommonAdapter<CityBean>(this, R.layout.item_school_name, onLineSchools) {

            @Override
            protected void convert(ViewHolder holder, CityBean school, int position) {
                holder.setText(R.id.school_name, school.getCity());
                holder.getView(R.id.line).setVisibility(View.VISIBLE);
//
            }
        });
        onLineAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                presenter.updataSchool(onLineSchools.get(position).getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        onlineSchool.setLayoutManager(new LinearLayoutManager(this));

//        indexBar.setNeedRealIndex(true)//设置需要真实的索引
//                .setmLayoutManager(manager)
//                .setmSourceDatas(cityBeans);

    }

    /**
     * 将拿出的学校转成字母
     */
    private void covert() {

        if (cityBeans == null || cityBeans.isEmpty()) {
            return;
        }

        mDataHelper.convert(cityBeans);
        mDataHelper.fillInexTag(cityBeans);
        mDataHelper.getSortedIndexDatas(cityBeans, rightTitles);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        unbinder.unbind();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);
        unbinder = ButterKnife.bind(this);
        initInject();
        initView();
        initSmartRecyclerLayout();
        initTitleRightRecyclerView();
    }

    private void showSearch() {
        titleRight.setVisibility(View.GONE);
        searchCy.setVisibility(View.VISIBLE);
        schoolRy.setVisibility(View.GONE);
        translte(searchWidth, searchWidth - cancelWidth);

    }


    private void translte(int startValue, int endValue) {
        translateAnimator = ValueAnimator.ofInt(startValue, endValue);
        translateAnimator.addUpdateListener(animation -> {

            int currentValue = (int) animation.getAnimatedValue();
            Log.wtf(TAG, currentValue + "");
            searchLayoutParams.width = currentValue;
            search.setLayoutParams(searchLayoutParams);
        });
        translateAnimator.setDuration(300);
        translateAnimator.start();
    }

    @OnClick(R.id.cancle)
    public void cancelSearch() {
        isClickSearch = false;
        hideSearcy();
        SoftInputUtils.hideSoftInputFromWindow(this, search);
        search.setText("");
        search.setCursorVisible(false);
    }

    private void hideSearcy() {
        titleRight.setVisibility(View.VISIBLE);
        searchCy.setVisibility(View.GONE);
        schoolRy.setVisibility(View.VISIBLE);
        translte(searchWidth - cancelWidth, searchWidth);
//        cancle.setVisibility(View.GONE);
    }

    private void initTitleRightRecyclerView() {
        rightTitles = new ArrayList<>();
        titleRight.setLayoutManager(new LinearLayoutManager(this));
        titleRight.setAdapter(titleRightAdapter = new CommonAdapter<String>(this, R.layout.item_school_title, rightTitles) {

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                TextView tv = holder.getView(R.id.school_title);
                tv.setText(rightTitles.get(position));
                if (position == lastSelectPosition) {
                    tv.setTextColor(ChooseSchoolActivity.this.getResources().getColor(R.color.colorDark2));
                    tv.setTextSize(14);
                } else {
                    tv.setTextColor(ChooseSchoolActivity.this.getResources().getColor(R.color.colorDarkB));
                    tv.setTextSize(10);
                }

                holder.setText(R.id.school_title, rightTitles.get(position));
                holder.setOnClickListener(R.id.school_title, v -> {
                    titleRightAdapter.notifyDataSetChanged();
                    lastSelectPosition = position;
                    if (manager != null) {
                        int selectPosition = getPosByTag(rightTitles.get(position));
                        if (selectPosition != -1) {
                            manager.scrollToPositionWithOffset(selectPosition, 0);
                        }
                    }
                });
            }
        });
        titleRight.setAnimation(null);
        ((SimpleItemAnimator) titleRight.getItemAnimator()).setSupportsChangeAnimations(false);
//        titleRightAdapter.setHasStableIds(true);
    }


    @OnTextChanged(R.id.search)
    public void searchSchool() {
        if (searchSchools == null || searchAdapter == null) return;
//        showSearch();
        String text = search.getText().toString().trim();
        searchSchools.clear();
        if ("".equals(text)) {

        } else {
            for (CityBean cityBean : cityBeans) {
                if (cityBean.getCity().indexOf(text) != -1) {
                    searchSchools.add(cityBean);
                }
            }
        }
        searchAdapter.notifyDataSetChanged();
    }

    boolean isClickSearch = false;

    @OnClick(R.id.search)
    public void search() {
        if (!isClickSearch)
            showSearch();
        isClickSearch = true;

        search.setCursorVisible(true);
    }


    private void initSmartRecyclerLayout() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                ChooseSchoolActivity.this.onLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ChooseSchoolActivity.this.onRefresh();
            }
        });
        refreshLayout.setRefreshHeader(new RefreshLayoutHeader(this));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setReboundDuration(200);
        refreshLayout.autoRefresh(200);
    }


    public void onLoadMore() {

    }

    public void onRefresh() {
        presenter.getSchoolNameList();
    }


    @Override
    public void referSchoolList(List<SchoolNameListRespDTO.SchoolNameListBean> schoolNameList) {
//        if (indexBar == null || commonAdapter == null) return;
        if (schoolNameList == null || schoolNameList.size() == 0 || cityBeans == null) return;

        if (cityBeans.size() > 0) cityBeans.clear();
        for (SchoolNameListRespDTO.SchoolNameListBean schoolNameListBean : schoolNameList) {
            List<SchoolNameListRespDTO.SchoolNameListBean.SchoolListBean> schoolListBeans = schoolNameListBean.getSchoolList();
            for (SchoolNameListRespDTO.SchoolNameListBean.SchoolListBean schoolListBean : schoolListBeans) {

                cityBeans.add(new CityBean(schoolListBean.getSchoolName(), schoolListBean.getId()));
            }
        }
//        indexBar.setmSourceDatas(cityBeans)
//                .invalidate();
        commonAdapter.notifyDataSetChanged();

        covert();

        titleRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void setreferComplete() {
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
    }

    @Override
    public void backToProfile() {
        this.finish();
    }

    @Override
    public void showOnLineSchool(List<School> schools) {
        if (onLineSchools == null || onLineAdapter == null) return;
        refreshLayout.setVisibility(View.VISIBLE);
        school.setVisibility(View.GONE);
        titleRight.setVisibility(View.GONE);
        rightRl.setVisibility(View.GONE);
        onlineSchool.setVisibility(View.VISIBLE);
        onLineSchools.clear();
        for (School school : schools) {
            onLineSchools.add(new CityBean(school.getSchoolName(), school.getId()));
        }

        onLineAdapter.notifyDataSetChanged();

    }


    /**
     * 根据传入的pos返回tag
     *
     * @param tag
     * @return
     */
    private int getPosByTag(String tag) {
        if (null == cityBeans || cityBeans.isEmpty()) {
            return -1;
        }
        if (TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < cityBeans.size(); i++) {
            if (tag.equals(cityBeans.get(i).getBaseIndexTag())) {
                return i;
            }
        }
        return -1;
    }


    private int mTouchRepeat = 0;
    private boolean mPoint2Down = false;
    private boolean mThreePointDown = false;
    long[] mHits = new long[4];
    private boolean online = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPoint2Down = false;
                mThreePointDown = false;
                mTouchRepeat = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchRepeat++;
                break;
            case MotionEvent.ACTION_POINTER_2_DOWN:
                mPoint2Down = true;
                break;
            case MotionEvent.ACTION_POINTER_3_DOWN:
                mThreePointDown = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (mPoint2Down && mTouchRepeat < 10 && !mThreePointDown) {
                    System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                    mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                    if (mHits[0] >= (SystemClock.uptimeMillis() - 3000)) {
                        online = !online;
                        Arrays.fill(mHits, 0);
                        presenter.getSchoolList(null, null, online);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
