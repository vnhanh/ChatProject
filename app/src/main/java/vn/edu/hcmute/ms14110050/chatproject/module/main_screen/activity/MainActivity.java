package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.SimpleCallback;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ActivityMainBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.activity.AuthenticationActivity;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.AccountFragment;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.HomeFragment;

/*
* Võ Ngọc Hạnh - 14110050 - Lớp Chiều thứ 2
* */

public class MainActivity extends BaseActivity<ActivityMainBinding, MainContract.View, MainViewModel>
        implements MainContract.View {

    ViewPagerAdapter mAdapter;
    ArrayList<SimpleCallback<User>> updateUserProfileListeners = new ArrayList<>();

    public static void openScreen(@NonNull Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainViewModel();
        if (!viewModel.checkLoginState()) {
            AuthenticationActivity.openScreen(this);
            return;
        }
        setAndBindContentView(R.layout.activity_main, this);

        initAdapter();
        initViewPagerAndTablyout();

        FacebookSdk.sdkInitialize(this);
    }

    private void initViewPagerAndTablyout() {
        binding.viewpager.setAdapter(mAdapter);

        // setup TabLayout
        binding.tablayout.setupWithViewPager(binding.viewpager);
        binding.tablayout.getTabAt(0).setIcon(R.drawable.ic_home_24dp_0dp).setText("");
        binding.tablayout.getTabAt(1).setIcon(R.drawable.ic_account_24dp_0dp).setText("");

        binding.tablayout.setSelectedTabIndicatorHeight(0);

        binding.tablayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#FF3448CA"), PorterDuff.Mode.SRC_IN);
        binding.tablayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FF3448CA"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tablayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initAdapter() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);

        HomeFragment homeFragment = new HomeFragment();
        AccountFragment accountFragment = new AccountFragment();

        mAdapter.addFragment("", homeFragment);
        mAdapter.addFragment("", accountFragment);
    }

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.destroy();
        }
        super.onDestroy();
    }

    /*
    * Implement MainContract.View
    * */

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoadUserData(User user) {
        for (SimpleCallback<User> callback : updateUserProfileListeners) {
            if (user != null) {
                callback.onSuccess(user);
            }else{
                callback.onError();
            }
        }
    }

    @Override
    public void showProgressLoadUserProfile() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressView.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressLoadUserProfile() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressView.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void logout() {
        showProgressLoadUserProfile();
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AuthenticationActivity.openScreen(MainActivity.this);
            }
        });
    }

    @Override
    public void showMessageLogoutFailed() {
        Toast.makeText(this, getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
    }

    // gán listener lắng nghe thay đổi thông tin người dùng trên Firebase
    public void setOnUpdateUserListener(SimpleCallback<User> listener) {
        if (updateUserProfileListeners == null) {
            updateUserProfileListeners = new ArrayList<>();
        }
        updateUserProfileListeners.add(listener);
    }
}
