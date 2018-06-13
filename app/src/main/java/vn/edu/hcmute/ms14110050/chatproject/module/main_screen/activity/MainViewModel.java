package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.asynctask.ConvertFirebaseValue;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant.FACEBOOK_PROVIDER_ID;


/**
 * Created by Vo Ngoc Hanh on 5/23/2018.
 */

public class MainViewModel extends BaseViewModel<MainContract.View>{

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    User user;

    public MainViewModel() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
    }

    @Override
    public void onViewAttach(@NonNull MainContract.View viewCallback) {
        super.onViewAttach(viewCallback);

        authStateListener = new FirebaseAuth.AuthStateListener(){
            // khi signout
            // sẽ được gọi 2 lần
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                // kiểm tra có phải trạng thái đăng nhập mới hay không
                if (!checkLoginState()) {
                    logout();
                }
            }
        };
        auth.addAuthStateListener(authStateListener);

        startLoad();
    }

    private void logout() {
        if (auth != null) {
            auth.removeAuthStateListener(authStateListener);
        }
        if (databaseReference != null) {
            databaseReference.removeEventListener(userNodeListener);
        }
        if (isViewAttached()) {
            getView().logout();
        }
    }

    public boolean checkLoginState() {
        return firebaseUser != null && !StringUtils.isEmpty(firebaseUser.getUid());
    }

    private void startLoad() {
        // khi người dùng còn trạng thái đăng nhập trên Firebase
        if (checkLoginState()) {
            databaseReference =
                    FirebaseDatabase.getInstance().getReference()
                            .child(FIREBASE.NODE_USERS)
                            .child(firebaseUser.getUid());

            loadUserInfo();
        }
    }

    private void loadUserInfo() {
        showProgress();

        databaseReference.addValueEventListener(userNodeListener);
    }

    private void onUpdateUserData(User userData) {
        // lưu dữ liệu
        this.user = userData;

        // hiển thị các thông tin văn bản
        if (isViewAttached()) {
            getView().onLoadUserData(user);
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        if (databaseReference != null) {
            databaseReference.removeEventListener(userNodeListener);
        }
        if (auth != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    private void showProgress() {
        if (isViewAttached()) {
            getView().showProgressLoadUserProfile();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgressLoadUserProfile();
        }
    }

    ValueEventListener userNodeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.getValue() !=null) {
                new ConvertFirebaseValue<User>(new GetCallback<User>() {
                    @Override
                    public void onFinish(User user) {
                        hideProgress();
                        onUpdateUserData(user);
                    }
                }, new GenericTypeIndicator<User>(){}).execute(dataSnapshot);
            } else {
                if (firebaseUser != null) {
                    for (UserInfo userInfo : firebaseUser.getProviderData()) {
                        // nếu là user đăng nhập bằng facebook
                        if(userInfo.getProviderId().equals(FACEBOOK_PROVIDER_ID)){
                            onCreateUserWithFacebook();
                            return;
                        }
                    }
                }
                if (isViewAttached()) {
                    hideProgress();
                    getView().logout();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            hideProgress();
            onUpdateUserData(null);
        }
    };

    // Tạo hồ sơ người dùng trong trường hợp đăng nhập bằng facebook
    private void onCreateUserWithFacebook() {
        User user = new User();
        user.setUid(firebaseUser.getUid());
        user.setFullname(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setPhone(firebaseUser.getPhoneNumber());
        user.setUrlImage(firebaseUser.getPhotoUrl().toString());

        UserAccessor.createUserInfo(user, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("LOG", getClass().getSimpleName() + ":create User Info on Firebase successfully");
                    databaseReference.addValueEventListener(userNodeListener);
                }else{
                    if (isViewAttached()) {
                        hideProgress();
                        getView().logout();
                    }
                }
            }
        });
    }
}
