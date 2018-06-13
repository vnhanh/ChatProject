package vn.edu.hcmute.ms14110050.chatproject.module.authentication.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.OnFragmentChangeStateListener;
import vn.edu.hcmute.ms14110050.chatproject.common.ActivityUtils;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ActivityAuthenticationBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.LoginFragment;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.register.RegisterFragment;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity.MainActivity;

/*
* Võ Ngọc Hạnh - 14110050 - Lớp Chiều thứ 2
* */


public class AuthenticationActivity extends AppCompatActivity {
    public static final int OPEN_LOGIN = 0;
    public static final int OPEN_REGISTER = 1;
    public static final int LOGIN_SUCCESS = 2;
    public static final int REGISTER_SUCCESS = 3;

    public static void openScreen(@NonNull Activity context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkLogined()) {
            MainActivity.openScreen(this);
        }else{
            ActivityAuthenticationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);
            openFragment(LoginFragment.getInstance(mListener), getString(R.string.title_login));
        }
    }

    private boolean checkLogined() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null && !firebaseUser.getUid().equals("");
    }

    private void openFragment(Fragment fragment, String titleToolbar) {
        try {
            ActivityUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.frame_layout);
            getSupportActionBar().setTitle(titleToolbar);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AuthenticationActivity.this, getString(R.string.error_task_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private OnFragmentChangeStateListener mListener = new OnFragmentChangeStateListener() {
        @Override
        public void onChange(int status) {
            switch (status) {
                case OPEN_LOGIN:
                    openFragment(LoginFragment.getInstance(mListener), getString(R.string.title_login));
                    break;

                case OPEN_REGISTER:
                    openFragment(RegisterFragment.getInstance(mListener), getString(R.string.title_register));
                    break;

                case LOGIN_SUCCESS:
                    MainActivity.openScreen(AuthenticationActivity.this);
                    break;

                case REGISTER_SUCCESS:
                    MainActivity.openScreen(AuthenticationActivity.this);
                    break;

                default:
                    return;
            }
        }
    };
}
