package com.xiaolian.amigo.ui.user;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoPresenter;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleteInfoActivity extends UserBaseActivity implements ICompleteInfoView {

    @Inject
    ICompleteInfoPresenter<ICompleteInfoView> presenter;

    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.man)
    LinearLayout man;
    @BindView(R.id.radio_woman)
    RadioButton radioWoman;
    @BindView(R.id.woman)
    LinearLayout woman;
    @BindView(R.id.tv_add_dormitory)
    RelativeLayout tvAddDormitory;
    @BindView(R.id.choose_bathroom)
    Button chooseBathroom;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        setMainBackground(R.color.colorBackgroundGray);
    }

    @Override
    protected int setTitle() {
        return R.string.complete_info;
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_complete_info;
    }



    @OnClick(R.id.tv_add_dormitory)
    public void addDormitory() {
//        startActivity(new Intent(this , ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    @OnClick(R.id.woman)
    public void choseWoman(){
        radioMan.setChecked(false);
        radioWoman.setChecked(true);
    }

    @OnClick(R.id.man)
    public void choseMan(){
        radioMan.setChecked(true);
        radioWoman.setChecked(false);
    }
}
