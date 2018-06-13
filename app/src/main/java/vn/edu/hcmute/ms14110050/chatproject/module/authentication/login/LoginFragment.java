package vn.edu.hcmute.ms14110050.chatproject.module.authentication.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.OnFragmentChangeStateListener;
import vn.edu.hcmute.ms14110050.chatproject.base.fragment.BaseFragment;
import vn.edu.hcmute.ms14110050.chatproject.databinding.FragmentLoginBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.activity.AuthenticationActivity;


public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginContract.View, LoginViewModel>
         implements LoginContract.View{
    private static LoginFragment INSTANCE;
    private LoginValidation mValidation;
    private OnFragmentChangeStateListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment getInstance(OnFragmentChangeStateListener listener) {
        LoginFragment instance = new LoginFragment();
        instance.setListener(listener);
        return instance;
    }

    public void setListener(OnFragmentChangeStateListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new LoginViewModel();
        View view = setAndBindContentView(inflater, container, R.layout.fragment_login, this);
        binding.setViewmodel(viewModel);
        binding.setInput(new LoginRequest());
        mValidation = new LoginValidation();
        binding.setValidation(mValidation);
        return view;
    }

    @Override
    public void onDestroy() {
        if (mValidation != null) {
            mValidation.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null && viewModel.getCallbackManager() != null) {
            viewModel.getCallbackManager().onActivityResult(requestCode, resultCode, data);
        }
    }

    public static Fragment getInstance() {
        return INSTANCE;
    }

    /*
        * Implement LoginContract.View
        * */
    @Override
    public void notGetAuth() {
        Toast.makeText(getContext(), getString(R.string.cant_get_auth), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(getContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.LOGIN_SUCCESS);
        }
    }

    @Override
    public void notFoundUser() {
        Toast.makeText(getContext(), getString(R.string.login_failed_not_found_user), Toast.LENGTH_LONG).show();
        binding.edtUsername.setText("");
        binding.edtUsername.requestFocus();
        binding.edtPassword.setText("");
    }

    @Override
    public void loginFailed() {
        Toast.makeText(getContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        binding.edtPassword.setText("");
        binding.edtPassword.requestFocus();
    }

    @Override
    public void clickRegisterLink() {
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.OPEN_REGISTER);
        }
    }

    @Override
    public void showProgress() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressView.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        binding.progressView.getRoot().setVisibility(View.GONE);
    }
}
