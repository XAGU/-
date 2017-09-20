package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;
import com.xiaolian.amigo.ui.user.adaptor.ListChooseAdaptor;
import com.xiaolian.amigo.ui.user.intf.IListChoosePresenter;
import com.xiaolian.amigo.ui.user.intf.IListChooseView;
import com.xiaolian.amigo.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 列表选择页面
 * @author zcd
 */

public class ListChooseActivity extends UserBaseActivity implements IListChooseView {
    public static final String INTENT_KEY_LIST_CHOOSE_CONTENT = "intent_key_list_choose_content";
    public static final String INTENT_KEY_LIST_CHOOSE_ITEMS = "intent_key_list_choose_items";
    public static final String INTENT_KEY_LIST_CHOOSE_ACTION = "intent_key_list_choose_action";
    public static final String INTENT_KEY_LIST_CHOOSE_PARENT_ID = "intent_key_list_choose_parent_id";
    public static final String INTENT_KEY_LIST_CHOOSE_IS_EDIT = "intent_key_list_choose_is_edit";
    public static final String INTENT_KEY_LIST_BUILDING_TYPE = "intent_key_list_building_type";
    public static final String INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID = "intent_key_list_choose_residence_bind_id";
    public static final String INTENT_KEY_LIST_CHOOSE_ITEM_RESULT = "intent_key_list_choose_item_result";
    public static final int ACTION_LIST_SCHOOL = 1;
    public static final int ACTION_LIST_DORMITOR = 2;
    public static final int ACTION_LIST_FLOOR = 3;
    public static final int ACTION_LIST_BUILDING = 4;
    public static final int ACTION_LIST_SEX = 5;
    public static final int ACTION_LIST_SCHOOL_RESULT = 6;

    private List<ListChooseAdaptor.Item> items = new ArrayList<>();

    // 宿舍是否编辑
    private boolean isEditDormitory;

    // 宿舍编辑时需要的ID
    private int residenceBindId = -1;

    @Inject
    IListChoosePresenter<IListChooseView> presenter;

    ListChooseAdaptor adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_choose);

        setUnBinder(ButterKnife.bind(this));

        getActivityComponent().inject(this);

        presenter.onAttach(ListChooseActivity.this);

        adapter = new ListChooseAdaptor(items);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (getIntent() != null) {
            switch (getIntent().getExtras().getInt(INTENT_KEY_LIST_CHOOSE_ACTION)) {
                case ACTION_LIST_SCHOOL:
                    tv_title.setText("选择学校");
                    presenter.getSchoolList(1, Constant.PAGE_SIZE);
                    adapter.setOnItemClickListener((view, position) -> {
                        presenter.updateSchool(items.get(position).getId());
                    });
                    break;
                case ACTION_LIST_SEX:
                    tv_title.setText("选择性别");
                    adapter.setOnItemClickListener((view, position) -> {
                        presenter.updateSex(
                                TextUtils.equals(items.get(position).getContent(), "男") ? 1 : 2);
                    });
                    this.items.add(new ListChooseAdaptor.Item("男", false));
                    this.items.add(new ListChooseAdaptor.Item("女", false));
                    adapter.notifyDataSetChanged();
                    break;
                case ACTION_LIST_BUILDING:
                    tv_title.setText("选择楼栋");
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                    }
                    presenter.getBuildList(1, Constant.PAGE_SIZE);
                    adapter.setOnItemClickListener((view, position) -> {
                        Intent intent = new Intent(getApplicationContext(), ListChooseActivity.class);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_FLOOR);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                        startActivity(intent);
                    });
                    break;
                case ACTION_LIST_FLOOR:
                    tv_title.setText("选择楼层");
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                        int parentId = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, -1);
                        if (parentId != -1) {
                            presenter.getFloorList(1, Constant.PAGE_SIZE, parentId);
                        }
                    }
                    adapter.setOnItemClickListener((view, position) -> {
                        Intent intent = new Intent(getApplicationContext(), ListChooseActivity.class);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_ACTION, ACTION_LIST_DORMITOR);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, items.get(position).getId());
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, isEditDormitory);
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, residenceBindId);
                        startActivity(intent);
                    });
                    break;
                case ACTION_LIST_DORMITOR:
                    tv_title.setText("选择宿舍");
                    if (getIntent() != null) {
                        isEditDormitory = getIntent().getBooleanExtra(INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
                        residenceBindId = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, -1);
                        int parentId = getIntent().getIntExtra(INTENT_KEY_LIST_CHOOSE_PARENT_ID, -1);
                        if (parentId != -1) {
                            presenter.getDormitoryList(1, Constant.PAGE_SIZE, parentId);
                        }
                    }
                    adapter.setOnItemClickListener((view, position) -> {
                        presenter.bindDormitory(residenceBindId, items.get(position).getId(), isEditDormitory);
                    });
                    break;
                case ACTION_LIST_SCHOOL_RESULT:
                    tv_title.setText("选择学校");
                    presenter.getSchoolList(1, Constant.PAGE_SIZE);
                    adapter.setOnItemClickListener((view, position) -> {
                        Intent intent = new Intent();
                        intent.putExtra(INTENT_KEY_LIST_CHOOSE_ITEM_RESULT, items.get(position));
                        setResult(RESULT_OK, intent);
                        finish();
                    });
                    break;
            }
        }
    }

    @Override
    protected void setUp() {

    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void backToDormitory() {
        Intent intent = new Intent(this, EditDormitoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void addMore(List<ListChooseAdaptor.Item> item) {
        this.items.addAll(item);
        adapter.notifyDataSetChanged();
    }
}
