package com.mobiotics.playvideoapp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mobiotics.playvideoapp.model.Highlight;

import java.util.List;

public class HomeVideoListAdapter extends RecyclerView.Adapter<HomeVideoListAdapter.HighlightViewHolder> {
    private Context context;
    private List<Highlight> highlights;

    public HomeVideoListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return highlights!=null?highlights.size():0;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder{

        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
