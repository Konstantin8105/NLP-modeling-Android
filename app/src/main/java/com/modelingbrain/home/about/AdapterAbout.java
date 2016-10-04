package com.modelingbrain.home.about;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.modelingbrain.home.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAbout extends RecyclerView.Adapter<AdapterAbout.ViewHolder>{

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();
    private final List<ModelAbout> items;

    private final ViewHolder.ClickListener clickListener;

    public AdapterAbout(ViewHolder.ClickListener clickListener, ArrayList<ModelAbout> items) {
        super();
        this.clickListener = clickListener;
        if(items == null)
            items = new ArrayList<>();
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.item_about;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelAbout item = items.get(position);

        holder.imageView.setImageResource(android.R.drawable.ic_menu_help);
        holder.titleAbout.setText(item.title);
        holder.titleLink.setText(item.link);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        final ImageView imageView;
        final TextView titleAbout;
        final TextView titleLink;
//        final View selectedOverlay;

        private final ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener){
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.icon);
            titleAbout = (TextView) itemView.findViewById(R.id.title_about);
            titleLink  = (TextView) itemView.findViewById(R.id.title_link);
//            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        public interface ClickListener {
            void onItemClicked(int position);
        }
    }
}
