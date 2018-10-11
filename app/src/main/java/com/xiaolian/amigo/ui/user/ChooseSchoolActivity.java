package com.xiaolian.amigo.ui.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author  wcm
 * @data 18/10/11
 */
public class ChooseSchoolActivity extends UserBaseActivity implements IChooseSchoolView {

    @Inject
    IChooseSchoolPresenter<IChooseSchoolView> presenter;
    @BindView(R.id.school_ry)
    RecyclerView schoolRy;
    @BindView(R.id.search)
    EditText search;


    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);
        getRlToolBar().setBackgroundResource(R.color.white);
        getSvMainContainer().setBackgroundResource(R.color.white);
//        SoftInputUtils.showSoftInputFromWindow(this,  search);
        search.setOnEditorActionListener((v, actionId, event) ->{
            onSuccess(search.getText().toString());
             return false ;
                }
        );
    }



    private void initSchoolRy(){


    }


    @Override
    protected int setTitle() {
        return R.string.choose_school_title;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_school;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
