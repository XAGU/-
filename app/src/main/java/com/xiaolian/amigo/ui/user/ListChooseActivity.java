package com.xiaolian.amigo.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.common.config.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 列表选择页面
 * @author zcd
 */

public class ListChooseActivity extends AppCompatActivity {
    public static final String INTENT_KEY_LIST_CHOOSE = "intent_key_list_choose";

    static List<ListChooseAdaptor.Item> items = new ArrayList<ListChooseAdaptor.Item>() {
        {
            add(new ListChooseAdaptor.Item("xx", true));
            add(new ListChooseAdaptor.Item("xx", false));
        }
    };


    ListChooseAdaptor adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_choose);
        ButterKnife.bind(this);
        final String conent = getIntent().getExtras().getString(INTENT_KEY_LIST_CHOOSE);
        adapter = new ListChooseAdaptor(items);
        adapter.setOnItemClickListener(new ListChooseAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String intentStr = conent;
                intentStr = intentStr + "dddddd";
                Intent intent = new Intent(ListChooseActivity.this, ListChooseActivity.class);
                intent.putExtra(INTENT_KEY_LIST_CHOOSE, intentStr);
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new RecycleViewDivider(this, RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
