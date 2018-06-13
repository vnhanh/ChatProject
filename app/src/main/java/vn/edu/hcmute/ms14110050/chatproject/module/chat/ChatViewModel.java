package vn.edu.hcmute.ms14110050.chatproject.module.chat;

import android.databinding.ObservableBoolean;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.MESSAGE_TYPE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_chats.Message;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.helper.ChatRecordHelper;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatRequestManager;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ResponseLoadUsers;

import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_ADD_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_CHANGE_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.MESSAGE_EVENT.TAG_REMOVE_MESSAGE;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_LOADED_TWO_USERS_SUCCESS;
import static vn.edu.hcmute.ms14110050.chatproject.module.chat.request_manager.ChatTags.TAG_SEND_MESSAGE_FAILED;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class ChatViewModel extends BaseViewModel<ChatContract.View> implements Observer, ChatRecordHelper.RecordListener{
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;

    boolean isGetedUserUid = false;

    ChatRequestManager requestManager;
    MessagesHandler messagesHandler;

    public final ObservableBoolean isLoading = new ObservableBoolean(true);


    public ChatViewModel() {
        messagesHandler = new MessagesHandler();
        messagesHandler.addObserver(this);
    }

    @Override
    public void onViewAttach(@NonNull ChatContract.View viewCallback) {
        super.onViewAttach(viewCallback);
        // show loading two users profile
        showProgress();

        // khởi tạo RequestManager
        initRequestManager();
        initAuth();
    }

    // Chỉ nên gọi sau hàm onViewAttach
    public void setChatterUserUid(String chatterUid) {
        requestManager.receiveChatterUserUid(chatterUid);
    }

    private void initAuth() {
        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener(){
            // khi signout
            // sẽ được gọi 2 lần
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                initFirebaseUser();

                // kiểm tra có phải trạng thái đăng nhập mới hay không
                if (!checkLoginState()) {
                    //logout
                    logout();
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
    }

    private void initFirebaseUser() {
        if (firebaseUser == null) {
            firebaseUser = auth.getCurrentUser();
        }

        if (!isGetedUserUid && requestManager != null) {
            isGetedUserUid = true;
            String _uid = firebaseUser.getUid();

            requestManager.receiveUserUid(_uid);
        }
    }

    private boolean checkLoginState() {
        return firebaseUser != null && !StringUtils.isEmpty(firebaseUser.getUid());
    }

    private void initRequestManager() {
        requestManager = new ChatRequestManager(messagesHandler);
        requestManager.addObserver(this);
    }

    private void logout() {
        removeAllListeners();
        if (isViewAttached()) {
            getView().onExit();
        }
    }

    @Override
    public void onViewDetached() {
        removeAllListeners();
        super.onViewDetached();
    }

    private void removeAllListeners() {
        if(requestManager!=null) {
            requestManager.deleteObserver(this);
        }
    }

    /*
    * ONCLICK CÁC BUTTON
    * */

    public void onClickImagePicker() {
        if (isViewAttached()) {
            getView().onClickImagePicker();
        }
    }

    // Khi người dùng enter message
    public void onEnterMessage(String message) {
        if (requestManager != null && !StringUtils.isEmpty(message)) {
            requestManager.sendMessage(message, MESSAGE_TYPE.TEXT);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof SendObject) {
            SendObject object = (SendObject) o;
            int tag = object.getTag();
            switch (tag) {
                /*
                * NOTIFY FROM REQUEST MANAGER
                * */
                case TAG_LOADED_TWO_USERS_SUCCESS:
                    // hide progress
                    hideProgress();

                    if (isViewAttached()) {
                        ResponseLoadUsers response = (ResponseLoadUsers) object.getValue();
                        getView().onUpdateUserUid(response);
                    }
                    break;

                case TAG_SEND_MESSAGE_FAILED:
                    if (isViewAttached()) {
                        getView().onSendMessageFailed();
                    }
                    break;

                /*
                * NOTIFY FROM MESSAGES HANDLER
                * */
                case TAG_ADD_MESSAGE:
                    if (isViewAttached()) {
                        getView().onAddMessage((Message) object.getValue());
                    }
                    break;
                case TAG_CHANGE_MESSAGE:
                    if (isViewAttached()) {
                        getView().onChangeMessage((Message) object.getValue());
                    }
                    break;
                case TAG_REMOVE_MESSAGE:
                    if (isViewAttached()) {
                        getView().onRemoveMessage((Message) object.getValue());
                    }
                    break;
            }
        }
    }

    private void showProgress() {
        isLoading.set(true);
    }

    private void hideProgress() {
        isLoading.set(false);
    }

    public void uploadImage(Uri imageUri) {
        showProgress();

        final StorageReference imageStorageRef =
                FirebaseStorage.getInstance().getReference()
                        .child(FIREBASE.STORAGE_IMAGES)
                        .child(FIREBASE.STORAGE_IMAGES_MESSAGE)
                        .child(FIREBASE.STORAGE_IMAGE_PREFIX
                                + imageUri.getLastPathSegment());

        UploadFileFirebase.execute(imageStorageRef, imageUri, new SimpleCallback<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                hideProgress();
                if (requestManager != null) {
                    requestManager.sendMessage(uri.toString(), MESSAGE_TYPE.IMAGE);
                }
            }

            @Override
            public void onError() {
                hideProgress();
                if (isViewAttached()) {
                    getView().uploadImageFailed();
                }
            }
        });
    }

    /**
     * IMPLEMENT ChatRecordHelper.RecordListener
     */

    // đường dẫn lưu file ghi âm
    File recordFile;
    MediaRecorder mediaRecorder;

    private void setupMediaRecorder() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
        recordFile = getOutputFile();

        recordFile.getParentFile().mkdirs();
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
    }

    @Override
    public void onStartRecord() {
        setupMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStopRecord() {
        if (mediaRecorder != null) {

            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    public void onPushRecord() {
        Uri audioUri = Uri.fromFile(recordFile);

        final StorageReference reference =
                FirebaseStorage.getInstance().getReference()
                        .child(FIREBASE.STORAGE_AUDIOS)
                        .child(FIREBASE.STORAGE_AUDIOS_MESSAGE)
                        .child(FIREBASE.STORAGE_RECORD_PREFIX
                                + audioUri.getLastPathSegment());

        UploadFileFirebase.execute(reference, audioUri, new SimpleCallback<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                recordFile.delete();
                if (requestManager != null) {
                    requestManager.sendMessage(uri.toString(), MESSAGE_TYPE.SOUND);
                }
            }

            @Override
            public void onError() {
                recordFile.delete();
                if (isViewAttached()) {
                    getView().uploadRecordFailed();
                }
            }
        });
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss SSS", Locale.getDefault());
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/RECORD/Record_"
                + dateFormat.format(new Date())
                + ".3gp");
    }
}
