package com.dulong.selectimage;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dulong on 2017/8/2.
 */

public abstract class RecyclerBaseAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    protected final int ITEM_VIEW_FLAG = -1;
    private volatile boolean CLICK_FLAG = true;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public abstract void onBindHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(ITEM_VIEW_FLAG, holder.getAdapterPosition());
        holder.itemView.setOnClickListener(onItemClickListener == null ? null : this);
        onBindHolder(holder, position);
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null && CLICK_FLAG) {
            CLICK_FLAG = false;
            int position = (int) view.getTag(ITEM_VIEW_FLAG);
            int itemViewType = getItemViewType(position);
            onItemClickListener.onItemClick(view, position, itemViewType);
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(600);
                    CLICK_FLAG = true;
                }
            }.start();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int itemType);
    }
}
