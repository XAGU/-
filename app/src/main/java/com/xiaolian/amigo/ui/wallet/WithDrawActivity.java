package com.xiaolian.amigo.ui.wallet;

import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.funds.WithdrawExplanationRespDTO;
import com.xiaolian.amigo.ui.wallet.intf.IWithDrawView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithDrawActivity extends WalletBaseActivity implements IWithDrawView {

    public static final String KEY_WITHDRAW_TIME = "KEY_WITHDRAW_TIME";

    public static final String KEY_WITHDRAW_OBJECT = "KEY_WITHDRAW_OBJECT";

    public static final String KEY_WITHDRAW_DESCRIPTION = "KEY_WITHDRAW_DESCRIPTION";

    public static final String KEY_WITHDRAW_DATA = "KEY_WITHDRAW_DATA" ;


    @Inject
    IWithDrawPresenter<IWithDrawView> presenter;


    @BindView(R.id.time)
    TextView tvTime;
    @BindView(R.id.object)
    TextView tvObject;
    @BindView(R.id.description)
    TextView tvDescription;


    private String time ;
    private String object ;
    private String description ;

    private WithdrawExplanationRespDTO dto ;

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null){
            time = getIntent().getStringExtra(KEY_WITHDRAW_TIME );
            object = getIntent().getStringExtra(KEY_WITHDRAW_OBJECT);
            description = getIntent().getStringExtra(KEY_WITHDRAW_DESCRIPTION);
            dto = getIntent().getParcelableExtra(KEY_WITHDRAW_DATA);
        }
    }

    @Override
    protected void initView() {
        setUp();
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setData();
//        tvTime.setText(time);
//        tvObject.setText(object);
//        tvDescription.setText(description);
    }


    private void setData(){
        if (dto == null)  return ;
         tvTime.setText("退款说明：" + dto.getTimeRange());
        tvObject.setText("退款对象：" +dto.getRefundUser());
        tvDescription.setText("退款说明：" +dto.getExplanation());
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
    }


    @OnClick({R.id.btn_submit})
    public void btnSubmit(){
        this.finish();
    }

}
