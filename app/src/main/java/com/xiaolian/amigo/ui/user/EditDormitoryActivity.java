package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.SpaceItemDecoration;
import com.xiaolian.amigo.tmp.common.util.ScreenUtils;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public class EditDormitoryActivity extends UserBaseListActivity implements IEditDormitoryView{

    @Inject
    IEditDormitoryPresenter<IEditDormitoryView> presenter;

    List<EditDormitoryAdaptor.UserResidenceWrapper> items = new ArrayList<>();

    EditDormitoryAdaptor adaptor;

    @BindView(R.id.tv_add_dormitory)
    TextView tv_add_dormitory;

    @OnClick(R.id.tv_add_dormitory)
    void onAddDormitoryClick() {
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                ListChooseActivity.ACTION_LIST_BUILDING);
        startActivity(intent);

    }

//    @Override
//    protected int getLayout() {
//        return R.layout.activity_edit_dormitory;
//    }

    @Override
    public void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> userResidenceWrappers) {
        items.addAll(userResidenceWrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void notifyAdaptor() {
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        items.clear();
        presenter.queryDormitoryList(page, Constant.PAGE_SIZE);
    }

    @Override
    public void onLoadMore() {
        presenter.queryDormitoryList(page, Constant.PAGE_SIZE);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new EditDormitoryAdaptor(this, R.layout.item_dormitory, items, presenter);
        adaptor.setOnItemClickListener((userResidenceWrapper, position) -> {
            presenter.updateResidenceId(userResidenceWrapper.getResidenceId());
        });
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
    }

    @Override
    protected int setTitle() {
        return R.string.edit_dormitory;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditDormitoryActivity.this);
    }
}
