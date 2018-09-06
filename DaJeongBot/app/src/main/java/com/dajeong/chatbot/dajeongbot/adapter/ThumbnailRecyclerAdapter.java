package com.dajeong.chatbot.dajeongbot.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

//import com.bumptech.glide.request.animation.GlideAnimation;

public class ThumbnailRecyclerAdapter extends RecyclerView.Adapter<ThumbnailRecyclerAdapter.ImageViewHolder> {
    Context context;
    Activity nowActivity;

    List<GalleryImage> items;

    public ThumbnailRecyclerAdapter(Context context, Activity nowActivity, List<GalleryImage> items){
        this.context = context;
        this.nowActivity = nowActivity;
        this.items = items;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        GalleryImage item = items.get(position);

//        Glide.with(context)
//                .load(item.getImgPath())
//                .asBitmap()
//                .thumbnail(0.4f)
//                .centerCrop()
//                .override(400,400)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        holder.imageView.setImageBitmap(resource);
//                    }
//                });
        Glide.with(context)
                .load(item.getImgPath())
                .thumbnail(0.4f)
                .apply(centerCropTransform())
                .transition(withCrossFade())
                .into(holder.imageView);

        holder.numberTextView.setText(String.valueOf(position+1));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView numberTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            numberTextView = itemView.findViewById(R.id.number_tv);
        }
    }
}

