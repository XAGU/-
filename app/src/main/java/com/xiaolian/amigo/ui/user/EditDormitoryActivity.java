package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

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

    TextView tv_add_dormitory;

    void onAddDormitoryClick() {
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                ListChooseActivity.ACTION_LIST_BUILDING);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onRefresh();
    }

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
    public void refreshList() {
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
        adaptor = new EditDormitoryAdaptor(this, R.layout.item_dormitory, items);
        adaptor.setOnItemClickListener((userResidenceWrapper, position) -> {
            presenter.updateResidenceId(userResidenceWrapper.getResidenceId());
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected int setTitle() {
        return R.string.edit_dormitory;
    }

    @Override
    protected int setSubTitle() {
        tv_add_dormitory = getSubTitle();
        tv_add_dormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddDormitoryClick();
            }
        });
        return R.string.add_dormitory;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(EditDormitoryActivity.this);
        adaptor.setPresenter(presenter);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
