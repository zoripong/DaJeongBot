package com.dajeong.chatbot.dajeongbot.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
    private ArrayList<String> directoryList;

    private RecyclerView thumbnailRecycler;
    private ArrayList<GalleryImage> thumbnailImages;
    private ThumbnailRecyclerSetter thumbnailRecyclerSetter;

    private int mPosition;
    private List<GalleryImage> galleryImages;

    private Spinner mDateSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 액션바 없애기
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_photo);

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



    }

    /**
     * 데이터 초기화
     */
    private void init() {
        //갤러리 리사이클러뷰 초기화
        mGalleryManager = new GalleryManager(getApplicationContext());
        galleryImages =  mGalleryManager.getDatePhotoPathList();
        //이미지 폴더 얻기
        dateList = new ArrayList<>();
        dateList.add("모든 사진");
        for (int i = 0; i < galleryImages.size(); i++) {
            String directory=galleryImages.get(i).getImgPath();
            int index=directory.lastIndexOf("/");
            directory=directory.substring(0,index);
            index=directory.lastIndexOf("/");
            directory=directory.substring(index+1);
            dateList.add(directory);
        }
        //중복제거
        directoryList = new ArrayList<String>();
        for (int i = 0; i < dateList.size(); i++) {
            if (!directoryList.contains(dateList.get(i))) {
                directoryList.add(dateList.get(i));
            }
        }

        mDateSpinner = findViewById(R.id.folder_spinner);
        //스피너 창 크기 조절
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(mDateSpinner);

            popupWindow.setHeight(1100);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        String stringArray[] = new String[directoryList.size()];
        stringArray = directoryList.toArray(stringArray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_textview_align, stringArray);
        adapter.setDropDownViewResource(R.layout.spinner_textview_align);
        adapter.notifyDataSetChanged();
        mDateSpinner.setSelection(0);
        mDateSpinner.setAdapter(adapter);

        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
                mPosition = i;
                mDateSpinner.setSelection(mPosition);
                String directoryName = directoryList.get(mPosition);
                galleryImages = mGalleryManager.getDirectoryPhotoPathList(directoryName);
                initRecyclerGallery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


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
        for (int i = 0; i < selectedPhotoList.size(); i++) {
            Log.e(TAG, ">>> selectedPhotoList   :  " + selectedPhotoList.get(i).getImgPath());
        }

        Intent intent = new Intent();
        intent.putExtra(INTENT_PHOTO_EXTRA, (Serializable) selectedPhotoList);
        setResult(RESULT_OK,intent);
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
