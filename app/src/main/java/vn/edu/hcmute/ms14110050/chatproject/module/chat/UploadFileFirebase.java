package vn.edu.hcmute.ms14110050.chatproject.module.chat;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;

/**
 * Created by Vo Ngoc Hanh on 6/11/2018.
 */

public class UploadFileFirebase {
    public static void execute(final StorageReference reference, Uri uri, final SimpleCallback<Uri> callback){

        final UploadTask uploadTask =
                reference.putFile(uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    callback.onSuccess(downloadUri);
                }else{
                    callback.onError();
                }
            }
        });
    }
}
