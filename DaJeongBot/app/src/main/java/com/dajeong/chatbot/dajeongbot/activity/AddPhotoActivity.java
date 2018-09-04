package com.dajeong.chatbot.dajeongbot.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dajeong.chatbot.dajeongbot.adapter.GalleryRecyclerAdapter;
import com.dajeong.chatbot.dajeongbot.Interface.OnItemClickListener;
import com.dajeong.chatbot.dajeongbot.manager.GalleryManager;
import com.dajeong.chatbot.dajeongbot.manager.GridDividerDecoration;
import com.dajeong.chatbot.dajeongbot.manager.ThumbnailRecyclerSetter;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;
import com.dajeong.chatbot.dajeongbot.R;

public class AddPhotoActivity extends AppCompatActivity {
    private final String TAG = "AddPhotoActivity";
    private final String INTENT_PHOTO_EXTRA = "INTENT_PHOTO_EXTRA";
    private final int READ_EXTERNAL_STORAGE_CODE = 5;
    private GalleryManager mGalleryManager;

    private RecyclerView galleryRecycler;
   // private Spinner mDateSpinner;
    private GalleryRecyclerAdapter galleryRecyclerAdapter;

    private String mBookCode;
    private String mPeriod;

    private ArrayList<String> dateList;

    private RecyclerView thumbnailRecycler;
    private ArrayList<GalleryImage> thumbnailImages;
    private ThumbnailRecyclerSetter thumbnailRecyclerSetter;

    private int mPosition;
    private List<GalleryImage> galleryImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        Intent intent = getIntent();
        mBookCode = intent.getStringExtra("BOOK_CODE");
        mPeriod = intent.getStringExtra("DATE");


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한 없음
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "이미지를 불러오는데 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
                // 권한 요청
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_CODE);

        }else{
            // 권한이 있음
            initLayout();
            init();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLayout();
                    init();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_CODE);
                }
                return;
            }
        }
    }


    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        galleryRecycler = (RecyclerView) findViewById(R.id.recyclerview);
        thumbnailRecycler = (RecyclerView) findViewById(R.id.thumbnail_recycler);

        findViewById(R.id.canceL_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPhotoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDone();
            }
        });

        //mDateSpinner = findViewById(R.id.date_spinner);

        //DateListManager dateListManager = new DateListManager();
        //Date[] dates = dateListManager.convertDates(mPeriod);

        //dateList = dateListManager.makeDateList(dates[0], dates[1]);

        //String stringArray[] = new String[dateList.size()];
        //stringArray = dateList.toArray(stringArray);

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
        //        android.R.layout.simple_spinner_item, stringArray);

       // mDateSpinner.setAdapter(adapter);

    //    mPosition = dateList.size()-1;
        //mDateSpinner.setSelection(mPosition);

//        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                mPosition = i;
//                int dates[] = convertDate(dateList.get(mPosition));
//                galleryImages = mGalleryManager.getDatePhotoPathList(dates[0], dates[1], dates[2]);
//                initRecyclerGallery();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    /**
     * 데이터 초기화
     */
    private void init() {
        //갤러리 리사이클러뷰 초기화
        mGalleryManager = new GalleryManager(getApplicationContext());
       // int dates[] = convertDate(dateList.get(mPosition));
        galleryImages =  mGalleryManager.getDatePhotoPathList();
        initRecyclerGallery();
        galleryRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        galleryRecycler.setItemAnimator(new DefaultItemAnimator());
        galleryRecycler.addItemDecoration(new GridDividerDecoration(getResources(), R.drawable.divider_recycler_gallery));

        thumbnailImages = new ArrayList<>();
        thumbnailRecyclerSetter = new ThumbnailRecyclerSetter(getApplicationContext(), this);

    }

    /**
     * 확인 버튼 선택 시
     */
    private void selectDone() {
        List<GalleryImage> selectedPhotoList = galleryRecyclerAdapter.getSelectedPhotoList();

        if(selectedPhotoList.size()==0){
            Toast.makeText(AddPhotoActivity.this, "이미지를 선택해주세요 :D", Toast.LENGTH_SHORT).show();
            return;
        }
//        for (int i = 0; i < selectedPhotoList.size(); i++) {
//            Log.e(TAG, ">>> selectedPhotoList   :  " + selectedPhotoList.get(i).getImgPath());
//        }

        Intent intent = new Intent(this, AddPhotoDetailActivity.class);
        intent.putExtra(INTENT_PHOTO_EXTRA, (Serializable) selectedPhotoList);
        intent.putExtra("BOOK_CODE", mBookCode);
        intent.putExtra("DATE", mPeriod);
//        intent.putExtra("SELECT_DATE", dateList.get(mPosition));

        startActivity(intent);

        finish();

    }

    /**
     * 갤러리 리사이클러뷰 초기화
     */
    private void initRecyclerGallery() {
        galleryRecyclerAdapter = new GalleryRecyclerAdapter(AddPhotoActivity.this, galleryImages, R.layout.item_photo);
        galleryRecyclerAdapter.setOnItemClickListener(mOnItemClickListener);
        galleryRecycler.setAdapter(galleryRecyclerAdapter);
    }


    /**
     * 리사이클러뷰 아이템 선택시 호출 되는 리스너
     */
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void OnItemClick(GalleryRecyclerAdapter.PhotoViewHolder photoViewHolder, int position) {

            GalleryImage galleryImage = galleryRecyclerAdapter.getmPhotoList().get(position);

            if(galleryImage.isSelected()){
                galleryImage.setSelected(false);
                galleryRecyclerAdapter.removeSelectedPhotoList(galleryImage);
            }else{
                galleryImage.setSelected(true);
                galleryRecyclerAdapter.addSelectedPhotoList(galleryImage);
            }

            galleryRecyclerAdapter.getmPhotoList().set(position, galleryImage);
            galleryRecyclerAdapter.notifyDataSetChanged();

            List<GalleryImage> selectedPhotoList = galleryRecyclerAdapter.getSelectedPhotoList();
            thumbnailRecyclerSetter.setRecyclerCardView(thumbnailRecycler, selectedPhotoList);

            if(selectedPhotoList.size() == 0)
                findViewById(R.id.total_tv).setVisibility(View.INVISIBLE);
            else{
                findViewById(R.id.total_tv).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.total_tv)).setText(String.valueOf(selectedPhotoList.size())); // 몇개의 이미지가 선택되었나 보여줌
            }

        }

//        @Override
//        public void OnItemClick(ImageListRecyclerAdapter.ImageViewHolder imageViewHolder, int position) {
//
//        }
    };

    /* 볼필요 ㄴㄴ
    *  string 날짜 -> int array (yy, mm, dd)
    * */
//    private int[] convertDate(String date){
//
//        String[] stringDate = date.split("\\.");
//        int [] intDate = new int[stringDate.length];
//        for(int i = 0; i<stringDate.length; i++){
//            intDate[i] = Integer.parseInt(stringDate[i]);
//        }
//
//        return intDate;
//    }
}
