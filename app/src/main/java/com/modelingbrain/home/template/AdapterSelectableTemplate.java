package com.modelingbrain.home.template;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.modelingbrain.home.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdapterSelectableTemplate extends SelectableAdapter<AdapterSelectableTemplate.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = AdapterSelectableTemplate.class.getSimpleName();
    private List<ElementList> items;

    private final ViewHolder.ClickListener clickListener;

    public AdapterSelectableTemplate(ViewHolder.ClickListener clickListener, ArrayList<ElementList> items) {
        super();

        this.clickListener = clickListener;

        if(items == null)
            items = new ArrayList<>();
        this.items = items;
    }

//    public void add(ElementList elementList){
//        items.add(elementList);
//    }

    public ElementList get(int position){
        return items.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.item;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ElementList item = items.get(position);


        GradientDrawable bgShape = (GradientDrawable) holder.rectangle.getBackground();
        bgShape.setColor(item.getResourceColorRectangle());

        holder.imageView.setImageResource(item.getResourceImage());
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.secondSubTitle.setText(item.getSecondSubTitle());

        // Highlight the item if it's selected
        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void swap(List<ElementList> list) {
        items = list;
        notifyDataSetChanged();
    }

//    public void removeAll(){
//        items.clear();
//        notifyDataSetChanged();
//    }

    private void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });
        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            items.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{
        final ImageView   imageView;
        final TextView    title;
        final TextView    subTitle;
        final TextView    secondSubTitle;
        final View        selectedOverlay;
        final View        rectangle;

        private final ClickListener listener;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.icon);

            title           = (TextView) itemView.findViewById(R.id.title);
            subTitle        = (TextView) itemView.findViewById(R.id.subtitle);
            secondSubTitle  = (TextView) itemView.findViewById(R.id.second_subtitle);

            selectedOverlay = itemView.findViewById(R.id.selected_overlay);
            rectangle       = itemView.findViewById(R.id.rectangle_color);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(v,getAdapterPosition());
            }
        }

        @SuppressWarnings("SimplifiableIfStatement")
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }

        public interface ClickListener {
            void onItemClicked(View view, int position);
            @SuppressWarnings("SameReturnValue")
            boolean onItemLongClicked(int position);
        }
    }
}
