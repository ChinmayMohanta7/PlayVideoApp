package com.mobiotics.playvideoapp.view.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.mobiotics.playvideoapp.R;
import com.mobiotics.playvideoapp.model.Highlight;
import com.mobiotics.playvideoapp.model.repository.DBHelper;
import com.mobiotics.playvideoapp.presentor.HighlightAsyncTask;
import com.mobiotics.playvideoapp.view.adapters.HomeAdapter;
import com.mobiotics.playvideoapp.view.customview.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HighlightAsyncTask.OnSavedListener,SwipeRefreshLayout.OnRefreshListener,HomeAdapter.OnVideoClickListener {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private ExoPlayer exoPlayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout=findViewById(R.id.refresh_layout);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new HomeAdapter(this);
        adapter.setListener(this);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20,true,true));
        swipeRefreshLayout.setOnRefreshListener(this);
        new HighlightAsyncTask(this).execute();
    }

    @Override
    public void onSaved() {
        List<Highlight> highlights=new ArrayList<>();
        highlights.addAll(DBHelper.getInstance().getHighlights());
        adapter.setHighlights(highlights);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        new HighlightAsyncTask(this).execute();
    }

    @Override
    public void onVideoClicked(Highlight highlight,int position) {
        Intent intent=new Intent(this,DetailActivity.class);
        intent.putExtra("highlight",highlight);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
