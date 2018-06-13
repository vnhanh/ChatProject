package vn.edu.hcmute.ms14110050.chatproject.module.authentication.register;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.FIREBASE;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;
import vn.edu.hcmute.ms14110050.chatproject.model.register.RegisterRequest;

/**
 * Created by Vo Ngoc Hanh on 5/19/2018.
 */


public class RegisterViewModel extends BaseViewModel<RegisterContract.View> {

    // đăng ký user
    public void onSubmit(final RegisterRequest input) {
        showProgress();

        input.selfTrim();

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            String email = input.getEmail();
            String password = input.getPassword();
            Task<AuthResult> task = auth.createUserWithEmailAndPassword(email,password);
            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            //tạo thông tin cho khách hàng mới và up lên firebase
                            final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(FIREBASE.NODE_USERS);
                            final Map<String, Object> map = new HashMap<>();
                            final User user = User.getUser(input);
                            user.setUid(firebaseUser.getUid());
                            map.put(user.getUid(), user.toMap());

                            mDatabaseRef.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    //Không thể ghi thông tin của khách hàng lên database
                                    if (databaseError != null) {
                                        //thông báo lỗi
                                        Log.d("LOG", "Couldn't store user profile");

                                        hideProgress();
                                        if (isViewAttached()) {
                                            getView().notCreateUserData();
                                        }
                                    }
                                    //Đồng bộ thành công thông tin khách hàng lên database
                                    else{
                                        Log.d("LOG", "Created user profile success");

                                        hideProgress();
                                        if (isViewAttached()) {
                                            getView().registerSuccess();
                                        }
                                    }
                                }
                            });
                        }
                        // không get được FirebaseUser
                        else{
                            Log.d("LOG", "Can't get FirebaseUser");

                            hideProgress();
                            if (isViewAttached()) {
                                getView().notGetFirebaseUser();
                            }
                        }
                    }else{
                        hideProgress();
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            if (isViewAttached()) {
                                getView().weakPassword();
                            }
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            if (isViewAttached()) {
                                getView().badFormatEmail();
                            }
                        } catch(FirebaseAuthUserCollisionException e) {
                            if (isViewAttached()) {
                                getView().emailExist();
                            }
                        } catch(Exception e) {
                            Log.e("LOG", "STATUS_ERROR_CANT_REGISTER:" + e.getMessage());
                            if (isViewAttached()) {
                                getView().registerFailed();
                            }
                        }
                    }
                }
            });
        }else {
            if (isViewAttached()) {
                getView().notGetAuth();
            }
        }
    }

    public void onClickOpenLogin(View view) {
        if (isViewAttached()) {
            getView().clickLoginLink();
        }
    }

    private void showProgress() {
        if (isViewAttached()) {
            getView().showProgress();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }

    @Override
    public void onViewResumed() {

    }
}
