package com.dajeong.chatbot.dajeongbot.Interface;

import com.dajeong.chatbot.dajeongbot.adapter.GalleryRecyclerAdapter;


public interface OnItemClickListener {
    void OnItemClick(GalleryRecyclerAdapter.PhotoViewHolder photoViewHolder, int position);
//    void OnItemClick(ImageListRecyclerAdapter.ImageViewHolder imageViewHolder, int position);
}
