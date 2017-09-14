package com.dulong.selectimage;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.List;

/**
 * 图片列表适配器
 * Created by dulong on 2017/9/11.
 */

public class PictureAdapter extends RecyclerBaseAdapter {
    static final int TYPE_PICTURE = 0;
    static final int TYPE_ADD = 1;
    private final LayoutInflater inflater;
    private final List<String> data;

    PictureAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ADD) {
            ((PictureViewHolder) holder).sdv_picture.setImageResource(R.drawable.content_icon_addpicture_default);
            ((PictureViewHolder) holder).iv_delete.setVisibility(View.GONE);
        } else {
            load(Uri.fromFile(new File(data.get(position))), ((PictureViewHolder) holder).sdv_picture, 100, 100);
            ((PictureViewHolder) holder).iv_delete.setTag(ITEM_VIEW_FLAG, holder);
            ((PictureViewHolder) holder).iv_delete.setOnClickListener(this);
            ((PictureViewHolder) holder).iv_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PictureViewHolder(inflater.inflate(R.layout.adapter_release_picture, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size())
            return TYPE_ADD;
        else return TYPE_PICTURE;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    private void load(Uri uri, SimpleDraweeView draweeView, int width, int height) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))// 压缩图片
                .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true) //自动播放gif动画
                .build();

        draweeView.setController(controller);
    }

    private class PictureViewHolder extends RecyclerView.ViewHolder {

        public final SimpleDraweeView sdv_picture;
        private final ImageView iv_delete;

        PictureViewHolder(View itemView) {
            super(itemView);
            sdv_picture = (SimpleDraweeView) itemView.findViewById(R.id.sdv_picture);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    private volatile boolean CLICK_FLAG = true;

    @Override
    public void onClick(View view) {
        if (view instanceof RelativeLayout) {
            super.onClick(view);
            return;
        }
        if (CLICK_FLAG) {
            CLICK_FLAG = false;
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) view.getTag(ITEM_VIEW_FLAG);
            data.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemChanged(getItemCount() - 1);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    SystemClock.sleep(500);
                    CLICK_FLAG = true;
                }
            }.start();
        }
    }
}
