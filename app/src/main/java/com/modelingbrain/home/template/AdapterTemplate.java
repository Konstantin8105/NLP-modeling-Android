package com.modelingbrain.home.template;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.modelingbrain.home.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTemplate extends RecyclerView.Adapter<AdapterTemplate.ViewHolder>{

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();
    private List<ElementList> items;

    private ViewHolder.ClickListener clickListener;

    public AdapterTemplate(ViewHolder.ClickListener clickListener, ArrayList<ElementList> items) {
        super();
        Log.d(TAG, "AdapterTemplate - start");
        this.clickListener = clickListener;
        this.items = items;
        Log.d(TAG, "AdapterTemplate - finish");
    }

    public ElementList get(int position){
        return items.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder - start");
        final int layout = R.layout.item;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        Log.d(TAG, "onCreateViewHolder - finish");
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder - start");
        final ElementList item = items.get(position);

        GradientDrawable bgShape = (GradientDrawable) holder.rectangle.getBackground();
        bgShape.setColor(item.getResourceColorRectangle());

        holder.imageView.setImageResource(item.getResourceImage());
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.secondSubTitle.setText(item.getSecondSubTitle());
        Log.d(TAG, "onBindViewHolder - finish");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        @SuppressWarnings("unused")
        private final String TAG = this.getClass().toString();

        ImageView   imageView;
        TextView    title;
        TextView    subTitle;
        TextView    secondSubTitle;
        View        selectedOverlay;
        View        rectangle;

        private ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener){
            super(itemView);
            Log.d(TAG, "ViewHolder - start");

            imageView = (ImageView) itemView.findViewById(R.id.icon);

            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subtitle);
            secondSubTitle = (TextView) itemView.findViewById(R.id.second_subtitle);

            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            rectangle = itemView.findViewById(R.id.rectangle_color);

            this.listener = listener;

            itemView.setOnClickListener(this);
            Log.d(TAG, "ViewHolder - finish");
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(v,getPosition());
            }
        }

        public interface ClickListener {
            void onItemClicked(View view, int position);
        }
    }
}
