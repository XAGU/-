package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.MvpApp;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.di.componet.DaggerUserActivityComponent;
import com.xiaolian.amigo.di.componet.UserActivityComponent;
import com.xiaolian.amigo.di.module.UserActivityModule;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserCertificationActivity extends BaseActivity implements IUserCertificationView {

    @Inject
    IUserCertificationPresenter<IUserCertificationView> presenter;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_profession)
    TextView tvProfession;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_studentId)
    TextView tvStudentId;
    @BindView(R.id.tv_dormitory)
    TextView tvDormitory;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.rel_edit_department)
    RelativeLayout relEditDepartment;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.rel_edit_profession)
    RelativeLayout relEditProfession;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.rel_edit_grade)
    RelativeLayout relEditGrade;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.rel_edit_class)
    RelativeLayout relEditClass;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.rel_edit_studentId)
    RelativeLayout relEditStudentId;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.rel_edit_dormitory)
    RelativeLayout relEditDormitory;
    @BindView(R.id.submit_card_txt)
    TextView submitCardTxt;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.iv_third)
    ImageView ivThird;
    @BindView(R.id.student_card_ll)
    LinearLayout studentCardLl;
    @BindView(R.id.card_txt)
    TextView cardTxt;
    @BindView(R.id.iv_front_card)
    ImageView ivFrontCard;
    @BindView(R.id.tv_front_card)
    TextView tvFrontCard;
    @BindView(R.id.front_card_rl)
    RelativeLayout frontCardRl;
    @BindView(R.id.iv_back_card)
    ImageView ivBackCard;
    @BindView(R.id.tv_back_card)
    TextView tvBackCard;
    @BindView(R.id.back_card_rl)
    RelativeLayout backCardRl;

    private User user;


    private UserActivityComponent mActivityComponent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_certification);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mActivityComponent = DaggerUserActivityComponent.builder()
                .userActivityModule(new UserActivityModule(this))
                .applicationComponent(((MvpApp) getApplication()).getComponent())
                .build();
        mActivityComponent.inject(this);
        presenter.onAttach(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) referStatus(user);
    }

    private void referStatus(User user) {

        tvDepartment.setText(user.getDepartment());
        tvProfession.setText(user.getProfession());
        tvGrade.setText(user.getGrade());
        tvClass.setText(user.getCalsses());
        tvStudentId.setText(user.getStudentId());
        tvDormitory.setText(user.getDormitory());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    protected void setUp() {

    }
}
