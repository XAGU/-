package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.main.MainActivity;
import com.xiaolian.amigo.ui.user.adaptor.ChooseDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseDormitoryView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 新的地址界面
 *
 * @author zcd
 * @date 17/10/11
 */

public class ChooseDormitoryActivity extends UserBaseListActivity implements IChooseDormitoryView {

    private static final int REQUEST_CODE_EDIT_DORMITORY = 0x1020;
    public static final String INTENT_KEY_LAST_DORMITORY = "intent_key_last_dormitory";
    @Inject
    IChooseDormitoryPresenter<IChooseDormitoryView> presenter;

    List<EditDormitoryAdaptor.UserResidenceWrapper> items = new ArrayList<>();

    ChooseDormitoryAdaptor adaptor;

    private Long residenceId;

    @Override
    protected void onRefresh() {
        page = Constant.PAGE_START_NUM;
        items.clear();
        presenter.queryDormitoryList(page, Constant.PAGE_SIZE);
    }

    @Override
    protected void onLoadMore() {
        presenter.queryDormitoryList(page, Constant.PAGE_SIZE);
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new ChooseDormitoryAdaptor(this, R.layout.item_choose_dormitory, items);
        adaptor.setResidenceId(residenceId);
        adaptor.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                backToDevice(items.get(position).getResidenceId(),
                        items.get(position).getMacAddress(),
                        items.get(position).getSupplierId(),
                        items.get(position).getResidenceName());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setItemPrefetchEnabled(false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dpToPxInt(this, 14)));
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected int setTitle() {
        return R.string.choose_dormitory;
    }

    @Override
    protected int setSubTitle() {
        getSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ChooseDormitoryActivity.this,
                        EditDormitoryActivity.class), REQUEST_CODE_EDIT_DORMITORY);
            }
        });
        //toolbar
        tvTitleThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ChooseDormitoryActivity.this,
                        EditDormitoryActivity.class), REQUEST_CODE_EDIT_DORMITORY);
            }
        });
        return R.string.manage_dormitory;
    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(ChooseDormitoryActivity.this);
    }

    @Override
    public void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers) {
        items.addAll(wrappers);
        adaptor.notifyDataSetChanged();
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            residenceId = getIntent().getLongExtra(INTENT_KEY_LAST_DORMITORY, -1);
        }
    }

    @Override
    public void backToDevice(Long residenceId, String macAddress, Long supplierId, String location) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.INTENT_KEY_MAC_ADDRESS, macAddress);
        intent.putExtra(MainActivity.INTENT_KEY_SUPPLIER_ID, supplierId);
        intent.putExtra(MainActivity.INTENT_KEY_LOCATION, location);
        intent.putExtra(MainActivity.INTENT_KEY_RESIDENCE_ID, residenceId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_DORMITORY) {
            onRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
