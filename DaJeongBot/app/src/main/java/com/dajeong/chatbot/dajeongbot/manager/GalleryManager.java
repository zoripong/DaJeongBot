package com.dajeong.chatbot.dajeongbot.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.dajeong.chatbot.dajeongbot.model.GalleryImage;

import java.util.ArrayList;
import java.util.List;

// 이미지를 가져오기 위한 클래스
public class GalleryManager {
    private final String TAG = "GalleryManager";

    private Context mContext;

    public GalleryManager(Context context) {
        mContext = context;
    }


    /**
     * 갤러리 이미지 반환
     *
     * @return
     */
    public List<GalleryImage> getAllPhotoPathList() {

        ArrayList<GalleryImage> photoList = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);

        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {

            GalleryImage galleryImage = new GalleryImage(cursor.getString(columnIndexData),false);
            photoList.add(galleryImage);
        }

        cursor.close();

        return photoList;
    }


    // 이건 필요없지롱
    /**
     * 날짜별 갤러리 이미지 반환
     *
     * @return
     */
    public List<GalleryImage> getDatePhotoPathList() {




        ArrayList<GalleryImage> photoList = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };



        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);

        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            GalleryImage galleryImage = new GalleryImage(cursor.getString(columnIndexData),false);
            Log.e(TAG, galleryImage.toString());
            photoList.add(galleryImage);
        }
        cursor.close();
        return photoList;
    }


}
