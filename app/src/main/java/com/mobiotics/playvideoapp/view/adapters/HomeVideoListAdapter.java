package com.mobiotics.playvideoapp.view.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class HomeVideoListAdapter extends RecyclerView.Adapter<HomeVideoListAdapter.HighlightViewHolder> {
    private Context context;
    private List<Highlight> highlights;
    private OnHighlightClickLitener litener;
    private int lastCheckedPosition;

    public HomeVideoListAdapter(Context context,List<Highlight> highlights, int selectedPosition) {
        this.context = context;
        this.highlights=highlights;
        this.lastCheckedPosition=selectedPosition;
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.detail_list_items,viewGroup,false);
        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder viewHolder, int position) {
        Highlight highlight=highlights.get(position);
        Picasso.get().load(Uri.parse(highlight.getUrl()))
                .into(viewHolder.thumbnail);
        viewHolder.title.setText(highlight.getTitle());
        viewHolder.desc.setText(highlight.getDescription());
        viewHolder.containerView.setSelected(position == lastCheckedPosition);
        if (position==lastCheckedPosition){
            viewHolder.title.setTextColor(ContextCompat.getColor(context,R.color.white));
            viewHolder.desc.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else {
            viewHolder.title.setTextColor(ContextCompat.getColor(context,R.color.black));
            viewHolder.desc.setTextColor(ContextCompat.getColor(context,R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return highlights!=null?highlights.size():0;
    }

    public void setLitener(OnHighlightClickLitener litener) {
        this.litener = litener;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder{

        private ImageView thumbnail;
        private TextView title,desc;
        private View containerView;
        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.thumbnail);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            containerView=itemView.findViewById(R.id.containerView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (litener!=null){
                        litener.onHighlightCLicked(highlights.get(getAdapterPosition()),getAdapterPosition());
                        lastCheckedPosition = getAdapterPosition();

                    }
                }
            });
        }
    }
    public interface OnHighlightClickLitener{
        void onHighlightCLicked(Highlight highlight,int position);
    }
}
