package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.ui.widget.ClearableEditText;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.util.Constant.CLASS;
import static com.xiaolian.amigo.util.Constant.DEPARTMENT;
import static com.xiaolian.amigo.util.Constant.PROFESSION;
import static com.xiaolian.amigo.util.Constant.STUDENT_ID;

public class EditUserInfoActivity extends BaseToolBarActivity {

    public static final String KEY_TYPE = "KEY_TYPE" ;  // 编辑的type
    public static final String KEY_DATA = "KET_DATA" ;  // 传输过来的数据

    public static final String KEY_BACK_DATA = "KEY_BACK_DATA" ;  // 返回的数据

    public static final String HINT_DEPARTMENT = "请输入院系名称" ;

    public static final String HINT_PROFESSION = "请输入专业名称";

    public static final String HINT_CLASS = "请输入班级";

    public static final String HINT_STUDENT_ID = "请输入学号" ;

    @BindView(R.id.edit_nickname)
    ClearableEditText editNickname;
    @BindView(R.id.bt_submit)
    Button btSubmit;

    private int type ;
    private String data ;

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null){
            type = getIntent().getIntExtra(KEY_TYPE ,-1);
            data = getIntent().getStringExtra(KEY_DATA );
        }
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        setMainBackground(R.color.colorBackgroundGray);
        editNickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        editNickname.setHint(setHint());
        if (!TextUtils.isEmpty(data)) {
            editNickname.setText(data);
            editNickname.setSelection(editNickname.getText().length());
            btSubmit.setEnabled(true);
        }
        ViewUtil.setEditPasswordInputFilter(editNickname);
        CommonUtil.showSoftInput(this, editNickname);
    }


    /**
     * 设置EditText 的hint
     */
    private String setHint(){
        switch (type){
            case DEPARTMENT:
                return HINT_DEPARTMENT;
            case PROFESSION:
                return HINT_PROFESSION;
            case CLASS:
                return HINT_CLASS;
            case STUDENT_ID:
                editNickname.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                return HINT_STUDENT_ID;
                default:
                    return "";
        }
    }

    @Override
    protected int setTitle() {
        switch (type){
            case DEPARTMENT:
                return R.string.edit_department;
            case PROFESSION:
                return R.string.edit_profession;
            case CLASS:
                return R.string.edit_class;
            case STUDENT_ID:
                return R.string.edit_student_id;
                default:
                    return R.string.edit_department ;
        }
    }


    @OnClick(R.id.bt_submit)
    public void submit(){
        Intent intent = getIntent();
        intent.putExtra(KEY_BACK_DATA ,editNickname.getText().toString().trim());
        intent.putExtra(KEY_TYPE ,type);
        this.setResult(RESULT_OK ,intent);
        this.finish();
    }



    @Override
    public void finish() {
        super.finish();
    }



    @Override
    protected int setLayout() {
        return R.layout.activity_edit_nickname;
    }

}
