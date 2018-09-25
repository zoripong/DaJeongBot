package com.dajeong.chatbot.dajeongbot.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.network.RetrofitService;

import java.io.File;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AWSMobileController {
    private final String TAG = "AWSMobileController";
    private static AWSMobileController instance;
    private Context mContext;
    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;

    public static AWSMobileController getInstance(Context context) {
        if( instance == null){
            instance = new AWSMobileController(context);
        }
        return instance;
    }

    private AWSMobileController(){}

    private AWSMobileController(Context context) {
        mContext = context;
        AWSMobileClient.getInstance().initialize(context, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Obtain the reference to the AWSCredentialsProvider and AWSConfiguration objects
                credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();

                // Use IdentityManager#getUserID to fetch the identity id.
                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                    @Override
                    public void onIdentityId(String identityId) {
                        Log.d(TAG, "Identity ID = " + identityId);

                        // Use IdentityManager#getCachedUserID to
                        //  fetch the locally cached identity id.
                        final String cachedIdentityId =
                                IdentityManager.getDefaultIdentityManager().getCachedUserID();
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.d(TAG, "Error in retrieving the identity" + exception);
                    }
                });
            }
        }).execute();

    }

    public void uploadWithTransferUtility( int accountId, File file ) {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(mContext)
                        .awsConfiguration(configuration)
                        .s3Client(new AmazonS3Client(credentialsProvider))
                        .build();

        // TODO: change the save path
        final String path = "uploads/" + String.valueOf(accountId)+"/"+file.getName();
        final TransferObserver uploadObserver = transferUtility.upload(path, file);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.e(TAG, uploadObserver.getBucket());
                    completeUpload(path);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.e(TAG, "이미지 업로드에 문제 발 생 문제 발 생!"+ex.toString());
                Toast.makeText(mContext,"이미지를 업로드하는데 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
            }

        });

        // If you prefer to poll for the data, instead of attaching a  listener, check for the state and progress in the observer.
        //
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
            Log.e(TAG,uploadObserver.getAbsoluteFilePath());
            completeUpload("");
        }

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }

    private void completeUpload(String filePath){
        String path = Util.getProperty("storage_url", mContext)+"/"+filePath;
        Log.e(TAG, "HI Upload is complete!");
        ((MainActivity)mContext).addUserImageChat(path);
    }

    private void downloadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(mContext)
                        .awsConfiguration(configuration)
                        .s3Client(new AmazonS3Client(credentialsProvider))
                        .build();

        TransferObserver downloadObserver =
                transferUtility.download(
                        "s3Folder/s3Key.txt",
                        new File("/path/to/file/localFile.txt"));

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d(TAG, "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == downloadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d(TAG, "Bytes Transferrred: " + downloadObserver.getBytesTransferred());
        Log.d(TAG, "Bytes Total: " + downloadObserver.getBytesTotal());
    }
}
