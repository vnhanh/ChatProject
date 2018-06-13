package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account;

import android.content.res.Resources;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant.FACEBOOK_PROVIDER_ID;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.AccountStatus.LAYOUT_CHANGE_PASSWORD;
import static vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.AccountStatus.LAYOUT_EDIT_ACCOUNT;

/**
 * Created by Vo Ngoc Hanh on 5/24/2018.
 */

public class AccountViewModel extends BaseViewModel<AccountContract.View> implements SimpleCallback<User> {
    WeakReference<Resources> resourcesWeakReference;
    BindableFieldTarget target;
    public ObservableField<Drawable> profileDrawable;

    public final ObservableBoolean progressStatus = new ObservableBoolean(false);
    public final ObservableBoolean verifyAccProgressStatus = new ObservableBoolean(false);
    public final ObservableBoolean changePassProgressStatus = new ObservableBoolean(false);

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    User user;

    public AccountViewModel(Resources resources) {
        resourcesWeakReference = new WeakReference<Resources>(resources);
    }

    @Override
    public void onViewAttach(@NonNull AccountContract.View viewCallback) {
        super.onViewAttach(viewCallback);
        init();
    }

    // Chạy lúc đầu
    private void init() {
        // init ảnh account mặc định
        profileDrawable = new ObservableField<Drawable>();
        profileDrawable.set(resourcesWeakReference.get().getDrawable(R.drawable.ic_profile_180dp_18dp));
        target = new BindableFieldTarget(profileDrawable, resourcesWeakReference.get());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // khi người dùng còn trạng thái đăng nhập trên Firebase
        if (firebaseUser != null) {
            databaseReference =
                    FirebaseDatabase.getInstance().getReference()
                            .child(FIREBASE.NODE_USERS)
                            .child(firebaseUser.getUid());
        }
    }

    /*
    * ONCLICK LISTENER
    * */
    // Khi người dùng bấm vào TextView hiển thị ngày sinh
    public void onClickBirthDate(View view) {
        if (isViewAttached()) {
            getView().showDatePickerDialog();
        }
    }

    // thông tin người dùng tạm thời
    private User tempData;

    // Khi người dùng bấm nút "LƯU"
    public void onSubmitSaveUserProfile(final User data) {
        // thông tin tài khoản không thay đổi
        if (user.equal(data)) {
            if (isViewAttached()) {
                getView().userProfileNotChanged();
            }
            return;
        }

        // nếu đăng nhập bằng facebook
        if (checkFacebookLoginUser()) {
            updateUserDataOnFirebase(data);
        }
        // nếu đăng nhập bằng email
        else{
            // ghi nhớ dữ liệu người dùng mới setting
            tempData = data;
            // ghi nhớ ngữ cảnh sau khi xác thực tài khoản
            typeContextOfVerifyAccount = LAYOUT_EDIT_ACCOUNT;
            // hiển thị dialog xác nhận tài khoản
            showDialogVerifyAccount();
        }
    }

    // Kiểm tra xem user đăng nhập bằng Facebook hay bằng email
    private boolean checkFacebookLoginUser() {
        for (UserInfo userInfo : firebaseUser.getProviderData()) {
            if (userInfo.getProviderId().equals(FACEBOOK_PROVIDER_ID)) {
                return true;
            }
        }
        return false;
    }

    private void updateUserProfileInfo() {
        if (tempData == null) {
            Log.d("LOG", getClass().getSimpleName() + ":updateUserProfileInfo:tempData is null");
            return;
        }
        // thông tin tài khoản đã thay đổi
        showProgress();

        if (user.getEmail().equals(tempData.getEmail())) {
            updateUserDataOnFirebase(tempData);
        }else{
            firebaseUser.updateEmail(tempData.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateUserDataOnFirebase(tempData);
                            }else{
                                // nếu người dùng đã login quá lâu
                                // sẽ không thể update email được
                                hideProgress();
                                if (isViewAttached()) {
                                    getView().updateEmailFailed();
                                }
                            }
                        }
                    });
        }
    }

    private void showDialogVerifyAccount() {
        if (isViewAttached()) {
            getView().showDialogVerifyAccount();
        }
    }

    // cờ ghi nhớ loại hành động sẽ làm sau khi xác thực tài khoản
    private int typeContextOfVerifyAccount = -1;

    /*
    * OnClick Dialog Verify Account
    * */
    public void onSubmitVerifyAccount(final AlertDialog dialog, String password) {
        // show progress cho dialog
        verifyAccProgressStatus.set(true);

        AuthCredential authCredential = EmailAuthProvider
                .getCredential(firebaseUser.getEmail(), password);
        firebaseUser.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideDialogProgress();

                            // nếu đang chỉnh sửa thông tin tài khoản
                            if (typeContextOfVerifyAccount == LAYOUT_EDIT_ACCOUNT) {
                                updateUserProfileInfo();
                            }
                            // nếu đang đổi mật khẩu
                            else{
                                if (isViewAttached()) {
                                    getView().showDialogChangePassword();
                                }
                            }
                        }
                        else{
                            hideDialogProgress();

                            if (isViewAttached()) {
                                getView().verifyPasswordFailed();
                            }
                        }
                    }

                    private void hideDialogProgress() {
                        // hide progress của dialog
                        verifyAccProgressStatus.set(false);
                        dialog.dismiss();
                    }
                });
    }

    // Khi người dùng bấm vào nút XÁC THỰC hoặc nút LƯU và đang ở chế độ thay đổi mật khẩu
    public void onSubmitChangePassword(final AlertDialog dialog, String password) {
        // show progress loading của dialog
        changePassProgressStatus.set(true);

        firebaseUser.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            hideDialogProgress();
                            if (isViewAttached()) {
                                getView().updatePasswordSuccess();
                            }
                        }else{
                            // nếu người dùng đã login quá lâu
                            // sẽ không thể update email được
                            hideDialogProgress();
                            if (isViewAttached()) {
                                getView().updatePasswordFailed();
                            }
                        }
                    }

                    private void hideDialogProgress() {
                        // hide progress của dialog
                        changePassProgressStatus.set(false);
                        dialog.dismiss();
                    }
                });
    }

    /*
    * Khi người dùng bấm các menu item
    * */
    // khi người dùng bấm menu item ĐỔI MẬT KHẨU
    public void onClickChangePassword() {
        // thông báo cho view biết sắp đổi layout
        changeViewGroup();
        // set indexView cho cả viewmodel và validation
        onChangeView(LAYOUT_CHANGE_PASSWORD);

        // ghi nhớ ngữ cảnh sau khi xác thực tài khoản
        typeContextOfVerifyAccount = LAYOUT_CHANGE_PASSWORD;
        showDialogVerifyAccount();
    }

    // khi người dùng bấm menu item LOGOUT
    // thông báo view đăng xuất và thay đổi UI tương ứng
    public void onClickLogoutMenuItem() {
        logout();
    }

    /*
    * END OnClick
    * */


    public final ObservableInt viewIndex = new ObservableInt(LAYOUT_EDIT_ACCOUNT);

    // thay đổi viewIndex ở ViewModel và Validation
    // viewIndex hỗ trợ xác định người dùng đang có hành động gì
    // EDIT_ACCOUNT hay VERIFY_PASSWORD hay CHANGE_PASSWORD
    private void onChangeView(int viewIndex) {
        this.viewIndex.set(viewIndex);
    }

    // update thông tin tài khoản
    private void updateUserDataOnFirebase(User data) {
        databaseReference.setValue(data.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    if (isViewAttached()) {
                        getView().updateUserProfileSuccess();
                    }
                }else{
                    if (isViewAttached()) {
                        getView().updateUserProfileFailed();
                    }
                }
            }
        });
    }

    // Khi người dùng bấm nút RESET để thay đổi thông tin tài khoản
    public void onResetAction(View view) {
        if (isViewAttached()) {
            // load lại ảnh
            loadProfileImage(user);

            // hiển thị các thông tin dạng văn bản lên UI
            if (isViewAttached()) {
                try {
                    User cloneProfile = (User) user.clone();
                    getView().reloadUserProfile(cloneProfile);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    Log.d("LOG", getClass().getSimpleName() + ":Couldn't clone user profile");
                    getView().messageCountErrorProcessData();
                }
            }
        }
    }

    // Khi người dùng bấm nút CANCEL để quay về màn hình thay đổi thông tin tài khoản
    // từ màn hình đổi mật khẩu
    public void onReturnEditAccountAction(View view) {
        changeViewGroup();
        onChangeView(LAYOUT_EDIT_ACCOUNT);
    }

    // thông báo cho view hiển thị UI khi đổi màn hình ( EDIT_ACCOUNT hoặc CHANGE_PASSWORD)
    private void changeViewGroup() {
        if (isViewAttached()) {
            getView().onSwitchViewGroup();
        }
    }

    // Thực hiện tác vụ lưu trữ và hiển thị thông tin mới của người dùng
    private void onUpdateUserData(User userData) {
        if (userData == null) {
            logout();
            return;
        }
        // thực hiện các công việc cần thiết khi get thông tin mới của người dùng
        updateUserData(userData);

        // load ảnh
        loadProfileImage(userData);
    }

    private void loadProfileImage(User userData) {
        Picasso.get().load(userData.getUrlImage())
                .error(R.drawable.ic_profile_180dp_18dp)
                .placeholder(R.drawable.ic_profile_180dp_18dp)
                .transform(new ProfileImageTransform(resourcesWeakReference.get()))
                .into(target);
    }

    // Thực hiện các công việc cần thiết khi get thông tin mới của người dùng
    private void updateUserData(User userData) {
        // lưu dữ liệu
        user = userData;

        // hiển thị các thông tin dạng văn bản lên UI
        if (isViewAttached() && user !=null) {
            try {
                User cloneProfile = (User) user.clone();
                getView().loadUserProfile(cloneProfile);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                Log.d("LOG", getClass().getSimpleName() + ":Couldn't clone user profile");
                getView().messageCountErrorProcessData();
            }
        }
    }

    private void showProgress() {
        if (isViewAttached()) {
            getView().clearAllError();
        }
        progressStatus.set(true);
    }

    private void hideProgress() {
        progressStatus.set(false);
    }

    public void uploadImageProfile(Uri uri) {
        showProgress();

        final StorageReference imageStorageRef =
                FirebaseStorage.getInstance().getReference()
                        .child(FIREBASE.STORAGE_IMAGES)
                        .child(FIREBASE.STORAGE_IMAGE_PREFIX + uri.getLastPathSegment());

        final UploadTask uploadTask =
                imageStorageRef.putFile(uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return imageStorageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    user.setUrlImage(downloadUri.toString());

                    updateUserDataOnFirebase(user);
                }else{
                    hideProgress();
                    if (isViewAttached()) {
                        getView().uploadImageFailed();
                    }
                }
            }
        });
    }

    // logout tài khoản trên firebase
    private void logout() {
        if (checkLoginState()) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            FirebaseAuth.getInstance().signOut();
        }
    }

    private boolean checkLoginState() {
        return firebaseUser != null && !StringUtils.isEmpty(firebaseUser.getUid());
    }

    /*
    * Implement OnChangeUserProfileListener
    * */

    @Override
    public void onSuccess(User user) {
        onUpdateUserData(user);
    }

    // Logout khi update dữ liệu thất bại
    @Override
    public void onError() {
        onUpdateUserData(null);
    }
}
