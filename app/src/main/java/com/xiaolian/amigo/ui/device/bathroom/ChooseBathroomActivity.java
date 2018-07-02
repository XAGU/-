package com.xiaolian.amigo.ui.device.bathroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.ZoomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择浴室
 *
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomActivity extends AppCompatActivity {
    private List<ChooseBathroomAdapter.ItemWrapper> items = new ArrayList<ChooseBathroomAdapter.ItemWrapper>() {
        {
            add(new ChooseBathroomAdapter.ItemWrapper("1"));
            add(new ChooseBathroomAdapter.ItemWrapper("2"));
            add(new ChooseBathroomAdapter.ItemWrapper("3"));
            add(new ChooseBathroomAdapter.ItemWrapper("4"));
        }
    };
    private ZoomRecyclerView recyclerView;
    private ChooseBathroomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bothroom);
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initRecyclerView() {
        adapter = new ChooseBathroomAdapter(this, R.layout.item_bathroom_small, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
