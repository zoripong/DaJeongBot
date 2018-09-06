package com.dajeong.chatbot.dajeongbot.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.dajeong.chatbot.dajeongbot.Interface.OnItemClickListener;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;


public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.PhotoViewHolder> {
    private final String TAG = "GalleryRecyclerAdapter";

    private Activity mActivity;

    private int itemLayout;
    private List<GalleryImage> mPhotoList;
    private List<GalleryImage> mSelectedPhotoList;

    private OnItemClickListener onItemClickListener;

    private ImageView testview;

    private Dialog dialog;
    private ImageView preView;
    /**
     * PhotoList 반환
     * @return
     */
    public List<GalleryImage> getmPhotoList() {
        return mPhotoList;
    }

    /**
     * 선택된 PhotoList 반환
     * @return
     */
    public List<GalleryImage> getSelectedPhotoList(){
        return mSelectedPhotoList;
    }

    // 선택된 이미지를 다시 선택할 경우 return
    // 선택되지 않은 이미지를 선택했을 경우 선택된 이미지에 추가
    public void addSelectedPhotoList(GalleryImage galleryImage){
        if(mSelectedPhotoList.contains(galleryImage))
            return;

        mSelectedPhotoList.add(galleryImage);
    }

    // 선택된 이미지를 다시 선택할 경우 미선택으로 지정하기 위해서 선택된 이미지에서 제거
    public void removeSelectedPhotoList(GalleryImage galleryImage){
        mSelectedPhotoList.remove(galleryImage);
    }

    /**
     * 아이템 선택시 호출되는 리스너
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * 생성자
     * @param photoList
     * @param itemLayout
     */
    public GalleryRecyclerAdapter(Activity activity, List<GalleryImage> photoList, int itemLayout) {

        mActivity = activity;
        if(photoList==null)
            Log.i("null값", "photoList값이 null");
        else{
            Log.i("null값", photoList.toString()+"photoList값이 null 아님ㅁ");
        }
        this.mPhotoList = photoList;
        this.itemLayout = itemLayout;

        mSelectedPhotoList = new ArrayList<>();

    }

    /**
     * 레이아웃을 만들어서 Holer에 저장
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new PhotoViewHolder(view);
    }


    /**
     * listView getView 를 대체
     * 넘겨 받은 데이터를 화면에 출력하는 역할
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final PhotoViewHolder viewHolder, final int position) {

        final GalleryImage galleryImage = mPhotoList.get(position);

        Log.e(TAG, "Recycler : "+galleryImage.getImgPath());

        Glide.with(mActivity)
                .load(galleryImage.getImgPath())
                .apply(centerCropTransform())
                .transition(withCrossFade())
                .into(viewHolder.photoIv);

        //선택
        if(galleryImage.isSelected()){
            viewHolder.layoutSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.layoutSelect.setVisibility(View.INVISIBLE);
        }

        // 클릭하면 선택된것처럼 보일 수 있게
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(viewHolder, position);
                }
            }
        });

        // 길게 누르면 크게
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog = new Dialog(mActivity, R.style.MyDialog);
                dialog.setContentView(R.layout.dialog_image_preview);

               preView = dialog.findViewById(R.id.preview_iv);
                Log.e(TAG, "Dialog : "+galleryImage.getImgPath());

                final LinearLayout root = dialog.findViewById(R.id.root);

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.findViewById(R.id.preview_iv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                Point pt = new Point();
                mActivity.getWindowManager().getDefaultDisplay().getSize(pt);
                ((WindowManager) mActivity.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);
                double dwidth = (pt.x)*0.8;
                int width=(int)dwidth;
                Glide.with(mActivity)
                        .asBitmap()
                        .load(galleryImage.getImgPath())
                        .apply(new RequestOptions().override(width, width))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                preView.setImageBitmap(resource);
                                dialog.show();
                            }
                        });



                 return true;
            }
        });


    }



    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }


    /**
     * 뷰 재활용을 위한 viewHolder
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoIv;
        public RelativeLayout layoutSelect;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            photoIv = (ImageView) itemView.findViewById(R.id.photo_iv);
            layoutSelect = (RelativeLayout) itemView.findViewById(R.id.select_check_layout);
        }

    }
}

