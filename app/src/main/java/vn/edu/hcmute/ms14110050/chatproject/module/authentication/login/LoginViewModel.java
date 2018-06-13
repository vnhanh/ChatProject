package vn.edu.hcmute.ms14110050.chatproject.module.authentication.login;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Arrays;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.model.login.LoginRequest;

/**
 * Created by Vo Ngoc Hanh on 5/19/2018.
 */

public class LoginViewModel extends BaseViewModel<LoginContract.View> {
    private FirebaseAuth auth;
    private CallbackManager callbackManager;

    private static final String EMAIL = "email";

    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void onClickFacebookLogin(View view) {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view;
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(LoginFragment.getInstance());
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LOG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("LOG", "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LOG", "facebook:onError", error);

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("LOG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG", "signInWithCredential:success");
                            if (isViewAttached()) {
                                getView().loginSuccess();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void onSubmit(final LoginRequest input) {
        showProgress();
        input.trimSelft();

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth != null) {
            auth.signInWithEmailAndPassword(input.getEmail(), input.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // đăng nhập thành công
                            if (task.isSuccessful()) {
                                hideProgress();
                                if (isViewAttached()) {
                                    getView().loginSuccess();
                                }
                            }
                            // đăng nhập không thành công
                            else{
                                hideProgress();
                                Log.d("LOG", "login failed:email:" + input.getEmail());
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    if (isViewAttached()) {
                                        getView().notFoundUser();
                                    }
                                }
                                // lỗi không xác định
                                catch (Exception e) {
                                    e.printStackTrace();
                                    if (isViewAttached()) {
                                        getView().loginFailed();
                                    }
                                }
                            }
                        }
                    });
        }else{
            hideProgress();
            if (isViewAttached()) {
                getView().notGetAuth();
            }
        }
    }

    public void onClickLinkRegister(View view) {
        if (isViewAttached()) {
            getView().clickRegisterLink();
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
