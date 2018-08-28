package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.user.UserResidenceDTO;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.xiaolian.amigo.ui.widget.SpaceItemDecoration;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_BUILDING_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_ID;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_NAME;
import static com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomActivity.KEY_RESIDENCE_TYPE;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_LOCATION;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_MAC_ADDRESS;
import static com.xiaolian.amigo.ui.main.MainActivity.INTENT_KEY_SUPPLIER_ID;

/**
 * 洗澡地址列表
 *
 * @author zcd
 * @date 17/9/19
 */

public class EditDormitoryActivity extends UserBaseListActivity implements IEditDormitoryView {

    private static final String TAG = EditDormitoryActivity.class.getSimpleName() ;
    private static final int REQUEST_CODE_EDIT_DORMITORY = 0x1020;
    public static final String INTENT_KEY_LAST_DORMITORY = "intent_key_last_dormitory";
    @Inject
    IEditDormitoryPresenter<IEditDormitoryView> presenter;

    List<EditDormitoryAdaptor.UserResidenceWrapper> items = new ArrayList<>();

    EditDormitoryAdaptor adaptor;

    private static int lastNormalPosition = - 1 ;
    TextView tvAddDormitory;
    private boolean needRefresh;
    private volatile boolean refreshFlag = false;

    void onAddDormitoryClick() {
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, false);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY ,Constant.ADD_BATHROOM_SRC);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onRefresh();
    }

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            needRefresh = getIntent().getBooleanExtra(Constant.EXTRA_KEY, true);
        }
    }

    @Override
    public void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> userResidenceWrappers) {
        if (this.items != null) {
            this.items.clear();
            this.items.addAll(userResidenceWrappers);
            adaptor.notifyDataSetChanged();
        }else{

        }
    }

    @Override
    public void notifyAdaptor() {
        onRefresh();
    }

    @Deprecated
    @Override
    public void refreshList(Long defaultId) {
        presenter.saveDefaultResidenceId(defaultId);
        onRefresh();
    }

    @Deprecated
    @Override
    public void editDormitory(Long id, UserResidenceDTO data, int position) {
        Intent intent = new Intent(EditDormitoryActivity.this, ListChooseActivity.class);
        if (data != null) {
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_RESIDENCE_DETAIL, data);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, true);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_BUILDING);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID,
                    items.get(position).getId());
        }
        startActivity(intent);
    }


    @Override
    public void startBathroom(UserResidenceInListDTO dto) {
        startActivity(new Intent(this , ChooseBathroomActivity.class)
                .putExtra(KEY_ID , dto.getId())
                .putExtra(KEY_BUILDING_ID ,dto.getBuildingId())
                .putExtra(KEY_RESIDENCE_ID , dto.getResidenceId())
                .putExtra(KEY_RESIDENCE_TYPE , dto.getResidenceType())
                .putExtra(KEY_RESIDENCE_NAME , dto.getResidenceName()));
        this.finish();
    }

    @Override
    public void startShower(UserResidenceInListDTO dto) {
        startActivity(new Intent(this , HeaterActivity.class)
                .putExtra(INTENT_KEY_LOCATION ,dto.getResidenceName())
                .putExtra(INTENT_KEY_MAC_ADDRESS ,dto.getMacAddress())
                .putExtra(INTENT_KEY_SUPPLIER_ID , dto.getSupplierId()));
        this.finish();
    }

    @Override
    public void setLastNormalPosition(int position) {
        lastNormalPosition = position ;
    }

    @Override
    public void notifyAdapter(EditDormitoryAdaptor.UserResidenceWrapper wrapper  ,int currentPosition) {
        if (items != null && adaptor != null) {
            if (lastNormalPosition != -1) {
                EditDormitoryAdaptor.UserResidenceWrapper lastNormalWrapper = items.get(lastNormalPosition);
                lastNormalWrapper.setDefault(false);
                items.set(lastNormalPosition  , lastNormalWrapper);
            }
            if (currentPosition != -1){
                EditDormitoryAdaptor.UserResidenceWrapper currentNormalWrapper = items.get(currentPosition);
                currentNormalWrapper.setDefault(true);
                items.set(currentPosition,currentNormalWrapper);
                lastNormalPosition = currentPosition ;
            }
            adaptor.notifyDataSetChanged();
        }

    }

    @Override
    public void delateBathroomRecord(int position) {
        if (position != -1 && items.size() > 0 && adaptor != null){
            if (position != lastNormalPosition) {
                items.remove(position);
                adaptor.notifyDataSetChanged();
            }else{
                if (position == 0 ){
                    items.remove(position);
                    adaptor.notifyDataSetChanged();
                    showEmptyView();
                }else{
                    items.remove(position);
                    presenter.updateNormalBathroom(items.get(0) ,0);
                }
            }

        }
    }

    @Override
    protected void onRefresh() {
        if (!needRefresh) {
            setRefreshComplete();
            needRefresh = true;
            return;
        }
        refreshFlag = true;
        presenter.queryBathList();
    }

    @Override
    public void onLoadMore() {
        setLoadMoreComplete();
    }

    @Override
    protected void setRecyclerView(RecyclerView recyclerView) {
        adaptor = new EditDormitoryAdaptor(this, R.layout.item_dormitory, items);
        adaptor.setOnItemClickListener((userResidenceWrapper, position) ->
                presenter.updateNormalBathroom(userResidenceWrapper  , position));
        adaptor.setOnItemLongClickListener(() -> onSuccess("请左滑操作"));
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
        tvAddDormitory = getSubTitle();
        tvAddDormitory.setOnClickListener(v -> onAddDormitoryClick());
        //toolbar
        tvTitleThird.setOnClickListener(v -> onAddDormitoryClick());

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

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
