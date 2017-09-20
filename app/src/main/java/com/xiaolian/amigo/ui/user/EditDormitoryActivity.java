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

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public class EditDormitoryActivity extends UserBaseActivity implements IEditDormitoryView{

    @Inject
    IEditDormitoryPresenter<IEditDormitoryView> presenter;

    List<EditDormitoryAdaptor.UserResidenceWrapper> items = new ArrayList<>();

    EditDormitoryAdaptor adaptor;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dormitory);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(EditDormitoryActivity.this);

        adaptor = new EditDormitoryAdaptor(this, R.layout.item_dormitory, items, presenter);
        adaptor.setOnItemClickListener((userResidenceWrapper, position) -> {
            presenter.updateResidenceId(userResidenceWrapper.getResidenceId());
        });
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adaptor);

        presenter.queryDormitoryList(1, Constant.PAGE_SIZE);
    }

    @Override
    public void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> userResidenceWrappers) {
        items.addAll(userResidenceWrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public void notifyAdaptor() {
        adaptor.notifyDataSetChanged();
    }
}
