package com.dajeong.chatbot.dajeongbot.adapter;


//import com.google.firebase.database.FirebaseDatabase;

//import com.dajeong.chatbot.dajeongbot.manager.SharedPreferenceManager;
//import com.dajeong.chatbot.dajeongbot.model.FirebaseImage;


/**
 * Created by doori on 2017-11-10.
 */
public class ImageListRecyclerAdapter {
//public class ImageListRecyclerAdapter extends RecyclerView.Adapter<ImageListRecyclerAdapter.ImageViewHolder> {
//    private final String TAG = "ImageRecyclerAdapter";
//
//    //private FirebaseDatabase mDatabase;
//
//
//    private Activity mActivity;
//   // private ArrayList<FirebaseImage> photoList;
//    private ArrayList<Bitmap> images;
//    private boolean isPhotoFragment;
//
//    private OnItemClickListener onItemClickListener;
//
//    /SharedPreferenceManager spm;
//
////    public ImageListRecyclerAdapter(Activity activity, ArrayList<FirebaseImage> photoList, ArrayList<Bitmap> images, boolean isPhotoFragment){
////        this.mActivity = activity;
////        this.photoList = photoList;
////        this.images = images;
////        this.isPhotoFragment = isPhotoFragment;
////    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//
//
//    @Override
//    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
//        return new ImageViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
//
//        spm = new SharedPreferenceManager(mActivity);
//
//        final FirebaseImage firebaseImage = photoList.get(position);
//        Bitmap bitmap = images.get(position);
//        // Reference to an image file in Firebase Storage
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//        Glide.with(mActivity)
//                .load(stream.toByteArray())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)// for local images
//                .centerCrop()
//                .into(holder.imageView);
//
//
//
//        if(!isPhotoFragment) {
//            if(firebaseImage.isSelected()){
//                holder.layoutSelect.setVisibility(View.VISIBLE);
//            }else{
//                holder.layoutSelect.setVisibility(View.INVISIBLE);
//            }
//        }
////
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    if (onItemClickListener != null) {
//                        onItemClickListener.OnItemClick(holder, position);
//                    }
//            }
//        });
//    }
//
//    public ArrayList<FirebaseImage> getPhotoList() {
//        return photoList;
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return photoList.size();
//    }
//
//    public static class ImageViewHolder extends RecyclerView.ViewHolder {
//        public ImageView imageView;
//        public RelativeLayout layoutSelect;
//
//
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.photo_iv);
//            layoutSelect = (RelativeLayout) itemView.findViewById(R.id.select_check_layout);
//
//        }
//    }
}