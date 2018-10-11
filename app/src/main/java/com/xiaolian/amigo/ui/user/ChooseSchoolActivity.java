package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.ui.widget.indicator.RefreshLayoutHeader;
import com.xiaolian.amigo.ui.widget.school.IndexBar.widget.IndexBar;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;
import com.xiaolian.amigo.ui.widget.school.suspension.SuspensionDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.indexBar)
    IndexBar indexBar;
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

    private SuspensionDecoration mDecoration;


    private LinearLayoutManager manager;


    private List<CityBean> cityBeans = new ArrayList<>();

    private CommonAdapter<CityBean> commonAdapter;

    private Unbinder unbinder;


    private UserActivityComponent mActivityComponent;

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

        manager = new LinearLayoutManager(this);
        schoolRy.addItemDecoration(mDecoration = new SuspensionDecoration(this, cityBeans));
        schoolRy.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 29)));
        mDecoration.setmDatas(cityBeans);
        schoolRy.setAdapter(commonAdapter = new CommonAdapter<CityBean>(this, R.layout.item_school_name, cityBeans) {

            @Override
            protected void convert(ViewHolder holder, CityBean cityBean, int position) {
                holder.setText(R.id.school_name, cityBean.getCity());
            }
        });
        schoolRy.setLayoutManager(manager);
        indexBar.setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(manager)
                .setmSourceDatas(cityBeans);


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
        if (indexBar == null || commonAdapter == null) return;
        if (schoolNameList == null || schoolNameList.size() == 0 || cityBeans == null) return;

        if (cityBeans.size() > 0) cityBeans.clear();
        for (SchoolNameListRespDTO.SchoolNameListBean schoolNameListBean : schoolNameList) {
            List<SchoolNameListRespDTO.SchoolNameListBean.SchoolListBean> schoolListBeans = schoolNameListBean.getSchoolList();
            for (SchoolNameListRespDTO.SchoolNameListBean.SchoolListBean schoolListBean : schoolListBeans) {

                cityBeans.add(new CityBean(schoolListBean.getSchoolName(), schoolListBean.getId()));
            }
        }
        indexBar.setmSourceDatas(cityBeans)
                .invalidate();
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void setreferComplete() {
        if (refreshLayout != null)
        refreshLayout.finishRefresh();
    }
}
