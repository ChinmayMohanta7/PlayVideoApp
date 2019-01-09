package com.mobiotics.playvideoapp.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiotics.playvideoapp.R;
import com.mobiotics.playvideoapp.model.Highlight;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private List<Highlight> highlights;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.home_items,viewGroup,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder viewHolder, int position) {
        Highlight highlight=highlights.get(position);
        Picasso.get().load(highlight.getThumb())
                .into(viewHolder.thumb);
        viewHolder.title.setText(highlight.getTitle());
        viewHolder.desc.setText(highlight.getDescription());

    }

    @Override
    public int getItemCount() {
        return highlights.size();
    }

    public void setHighlights(List<Highlight> highlights) {
        this.highlights=highlights;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumb;
        public TextView title;
        public TextView desc;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb=itemView.findViewById(R.id.thumbnail);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
        }
    }
}
