package com.xiaolian.amigo.ui.wallet;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.funds.WithdrawExplanationRespDTO;
import com.xiaolian.amigo.data.vo.CertificationStatusTypeEvent;
import com.xiaolian.amigo.data.vo.UserCertificationStatus;
import com.xiaolian.amigo.ui.user.UserCertificationActivity;
import com.xiaolian.amigo.ui.user.UserCertificationStatusActivity;
import com.xiaolian.amigo.ui.wallet.intf.IWithDrawView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.util.Constant.CERTIFICATION_NONE;
import static com.xiaolian.amigo.util.Constant.CERTIFICATION_PASS;

public class WithDrawActivity extends WalletBaseActivity implements IWithDrawView {

    public static final String KEY_WITHDRAW_TIME = "KEY_WITHDRAW_TIME";

    public static final String KEY_WITHDRAW_OBJECT = "KEY_WITHDRAW_OBJECT";

    public static final String KEY_WITHDRAW_DESCRIPTION = "KEY_WITHDRAW_DESCRIPTION";

    public static final String KEY_WITHDRAW_DATA = "KEY_WITHDRAW_DATA";

    public static final String KEY_CERTIFICATION_STATUS = "KEY_CERTIFICATION_STATUS";


    @Inject
    IWithDrawPresenter<IWithDrawView> presenter;


    @BindView(R.id.time)
    TextView tvTime;
    @BindView(R.id.object)
    TextView tvObject;
    @BindView(R.id.description)
    TextView tvDescription;
    @BindView(R.id.btn_submit)
    Button btnSubmit;


    private String time;
    private String object;
    private String description;
    private int status;

    private WithdrawExplanationRespDTO dto;


    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            time = getIntent().getStringExtra(KEY_WITHDRAW_TIME);
            object = getIntent().getStringExtra(KEY_WITHDRAW_OBJECT);
            description = getIntent().getStringExtra(KEY_WITHDRAW_DESCRIPTION);
            dto = getIntent().getParcelableExtra(KEY_WITHDRAW_DATA);
            status = getIntent().getIntExtra(KEY_CERTIFICATION_STATUS, -1);
            Log.e(TAG, "setUp: status =" + status );
        }
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setUp();
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setData();
    }


    private void setData() {
        if (dto == null) return;
        tvTime.setText("退款时间：" + dto.getTimeRange());
        if (dto != null && dto.isAll()) {
            tvObject.setVisibility(View.GONE);
            status = CERTIFICATION_PASS;

        }else{
            tvObject.setText("退款对象：" + dto.getRefundUser() + "(入学年份)");
        }
        tvDescription.setText("退款说明：" + dto.getExplanation());
        if (status == CERTIFICATION_PASS){
            btnSubmit.setText("朕知道了");
        }else{
            btnSubmit.setText("前往学生认证");
        }
    }

    @Override
    protected int setTitle() {
        return R.string.with_draw_title;
    }

    @Override
    protected int setLayout() {
        return R.layout.withdraw;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 学生认证状态改变
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventBusReceive(CertificationStatusTypeEvent certificationStatusTypeEvent) {
        if (certificationStatusTypeEvent != null){
            this.status = certificationStatusTypeEvent.getType();
        }

    }
    @OnClick({R.id.btn_submit})
    public void btnSubmit() {
        if (status == CERTIFICATION_PASS) {
            this.finish();
        }else if (status == CERTIFICATION_NONE){
            startActivity(this , UserCertificationActivity.class);
        }else{
            startActivity(this , UserCertificationStatusActivity.class);
        }
    }




}
