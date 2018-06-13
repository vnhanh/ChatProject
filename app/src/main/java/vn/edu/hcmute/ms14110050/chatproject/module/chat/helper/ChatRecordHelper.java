package vn.edu.hcmute.ms14110050.chatproject.module.chat.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Vo Ngoc Hanh on 6/10/2018.
 */

public class ChatRecordHelper {

    public static void onStartRecord(@NonNull Activity context, @NonNull RecordListener listener) {
        if (hasRecordPermission(context)) {
            listener.onStartRecord();
        }else{
            requestPermission(context);
        }
    }

    public static void onStopRecord(@NonNull Activity context, @NonNull RecordListener listener) {
        listener.onStopRecord();
    }

    public static void onPushRecord(@NonNull RecordListener listener) {
        listener.onPushRecord();
    }

    private static final int REQUEST_PERMISSION_CODE = 1000;

    private static void requestPermission(Activity context) {
        ActivityCompat.requestPermissions(context, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private static boolean hasRecordPermission(Activity context) {
        int write_external_storage_result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int record_audio_result = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED
                && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    public interface RecordListener{
        void onStartRecord();

        void onStopRecord();

        // up file ghi âm
        void onPushRecord();
    }
}
