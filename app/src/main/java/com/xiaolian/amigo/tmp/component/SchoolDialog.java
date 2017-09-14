package com.xiaolian.amigo.tmp.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.RegistryActivity;
import com.xiaolian.amigo.tmp.component.PickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caidong on 2017/9/1.
 */

public class SchoolDialog extends Dialog {

    Context context;

    @BindView(R.id.pv_schools)
    PickerView pv_schools;
    @BindView(R.id.tv_select_school)
    TextView tv_selectSchool;

    static List<String> schools = new ArrayList<String>() {
        {
            add("xxxxxxx1");
            add("xxxxxxx2");
            add("xxxxxxx3");
            add("xxxxxxx4");
            add("xxxxxxx5");
            add("xxxxxxx6");
            add("xxxxxxx7");
            add("xxxxxxx8");
        }
    };

    static String selectedSchool = schools.get(0);

    public SchoolDialog(Context context) {
        super(context, R.style.SchoolDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_school);
        ButterKnife.bind(this);

        pv_schools.setData(schools);
        pv_schools.setSelected(0);
        pv_schools.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedSchool = text;
                pv_schools.setSelected(schools.indexOf(selectedSchool));
            }
        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = 600;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }

    @OnClick(R.id.tv_select_school)
    void selectSchool() {
        Log.e("===========>", selectedSchool);
        TextView school = (TextView) ((RegistryActivity) context).findViewById(R.id.tv_school);
        school.setText(selectedSchool);
        ((RegistryActivity) context).toggleSumbitBtnStatus();
        this.dismiss();
    }

}
