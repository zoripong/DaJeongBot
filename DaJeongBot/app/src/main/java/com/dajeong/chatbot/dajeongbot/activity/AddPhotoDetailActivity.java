package com.dajeong.chatbot.dajeongbot.activity;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import com.dajeong.chatbot.dajeongbot.adapter.ImageDetailRecyclerAdapter;
import com.dajeong.chatbot.dajeongbot.manager.ImageDetailRecyclerSetter;
//import com.dajeong.chatbot.dajeongbot.model.FirebaseImage;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;
import com.dajeong.chatbot.dajeongbot.R;

import static android.R.attr.id;

public class AddPhotoDetailActivity extends BaseActivity {
    private final String STRORAGE_PATH = "images/";

    private final String INTENT_PHOTO_EXTRA = "INTENT_PHOTO_EXTRA";
    private final String TAG = "PHOTO_DETAIL_ACTIVITY";

    private String mBookCode;
    private String mPeriod;
    private String mToday;
    private ArrayList<GalleryImage> selectedList;
    //private ArrayList<FirebaseImage> firebaseImages;

    private RecyclerView recyclerView;

    // Creating StorageReference and DatabaseReference object.
    //StorageReference storageReference;
   // DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_detail);


        Intent intent = getIntent();
        mBookCode = intent.getStringExtra("BOOK_CODE");
        mPeriod = intent.getStringExtra("DATE");
        mToday = intent.getStringExtra("SELECT_DATE");

        selectedList = (ArrayList<GalleryImage>) intent.getSerializableExtra(INTENT_PHOTO_EXTRA);
        Log.e(TAG , selectedList.toString());

        recyclerView = findViewById(R.id.recyclerview);
        final ImageDetailRecyclerSetter imageDetailRecyclerSetter = new ImageDetailRecyclerSetter(this, this);
        imageDetailRecyclerSetter.setRecyclerCardView(recyclerView, selectedList);


        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드 버튼을 누를 때
                //firebaseImages = new ArrayList<FirebaseImage>();

//                ImageDetailRecyclerAdapter adapter = imageDetailRecyclerSetter.getAdapter();
//
//                if(adapter != null) {
//                    //firebaseImages = adapter.getImageList(mToday);
//                    UploadImageFileToFirebaseStorage();
//                }else{
//                    Toast.makeText(AddPhotoDetailActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
//                }
//
//                Intent intent = new Intent(AddPhotoDetailActivity.this, TravelDetailActivity.class);
//                intent.putExtra("BOOK_CODE",mBookCode);
//                intent.putExtra("DATE", mPeriod);
//                startActivity(intent);
//                finish();


            }
        });
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = this.getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


//    public void UploadImageFileToFirebaseStorage() {
//        storageReference = FirebaseStorage.getInstance().getReference();
//        databaseReference = FirebaseDatabase.getInstance().getReference("BookInfo/"+mBookCode+"/Content/Images");
//
//        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setContentTitle("Image Upload")
//                .setContentText("이미지를 업로드하고 있습니다.")
//                .setSmallIcon(R.drawable.ic_upload)
//                .setOngoing(true);
//
//        for(int i = 0; i<firebaseImages.size();i++){
//            Uri uri = Uri.fromFile(new File(firebaseImages.get(i).getImageURI()));
//
//            long timeStamp = System.currentTimeMillis();
//            // Creating second StorageReference.
//            StorageReference storageReference2nd = storageReference.child(STRORAGE_PATH + timeStamp + "." + GetFileExtension(uri));
//            firebaseImages.get(i).setImageURI(STRORAGE_PATH + timeStamp + "." + GetFileExtension(uri));
//
//            // Adding addOnSuccessListener to second StorageReference.
//            final int finalI = i;
//            storageReference2nd.putFile(uri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
//                            @SuppressWarnings("VisibleForTests")
//                            String ImageUploadId = databaseReference.push().getKey();
//                            databaseReference.child(ImageUploadId).setValue(firebaseImages.get(finalI));
//
//                        }
//                    })
//                    // If something goes wrong .
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Hiding the progressDialog.
//                            hideProgressDialog();
//                            // Showing exception erro message.
//                            Toast.makeText(AddPhotoDetailActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//
//                    // On progress change upload time.
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Setting progressDialog Title.
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            Log.e(TAG,"Upload is " + progress + "% done");
//                            if(progress < 100.0){
//                            mBuilder.setProgress(100, (int) progress, false);
//                            // Displays the progress bar for the first time.
//                            mNotifyManager.notify(id, mBuilder.build());
//
//
//                            }else{
//                                if(finalI==firebaseImages.size()-1){
//                                mBuilder.setOngoing(false);
//                                mBuilder.setContentText("Upload complete")
//                                        .setProgress(0,0,false);
//                                mNotifyManager.notify(id, mBuilder.build());
//                                }
//                            }
//
//                        }
//                    });
//        }
//    }

    public void onBackPressed(){
        Intent intent = new Intent(this, AddPhotoActivity.class);
        intent.putExtra("BOOK_CODE", mBookCode);
        intent.putExtra("DATE", mPeriod);
        startActivity(intent);

        finish();
    }

}
