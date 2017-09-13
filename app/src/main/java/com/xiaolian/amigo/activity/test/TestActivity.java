package com.xiaolian.amigo.activity.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.test.widget.Cheeses;
import com.xiaolian.amigo.activity.test.widget.SimpleStringRecyclerViewAdapter;

import java.util.Arrays;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(Arrays.asList(Cheeses.sCheeseStrings)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
