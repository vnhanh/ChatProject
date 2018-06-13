package vn.edu.hcmute.ms14110050.chatproject.module.authentication.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ActivityAuthenticationBinding;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.LoginFragment;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.register.RegisterFragment;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.ViewPagerAdapter;


public class AuthenticationActivity extends AppCompatActivity {

    ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAuthenticationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);

        initAdapter();
        binding.nonSwipeableViewpager.setAdapter(mAdapter);
        binding.nonSwipeableViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(mAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getSupportActionBar().setTitle(mAdapter.getPageTitle(0));
    }

    private void initAdapter() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mAdapter.addFragment(getString(R.string.title_login), new LoginFragment());
        mAdapter.addFragment(getString(R.string.title_register), new RegisterFragment());
    }

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.destroy();
        }
        super.onDestroy();
    }
}
