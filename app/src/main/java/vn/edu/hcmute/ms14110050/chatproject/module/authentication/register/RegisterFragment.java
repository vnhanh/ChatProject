package vn.edu.hcmute.ms14110050.chatproject.module.authentication.register;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.OnFragmentChangeStateListener;
import vn.edu.hcmute.ms14110050.chatproject.base.fragment.BaseFragment;
import vn.edu.hcmute.ms14110050.chatproject.databinding.FragmentRegisterBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.register.RegisterRequest;
import vn.edu.hcmute.ms14110050.chatproject.module.authentication.activity.AuthenticationActivity;


public class RegisterFragment extends BaseFragment<FragmentRegisterBinding, RegisterContract.View, RegisterViewModel>
        implements RegisterContract.View {

    RegisterValidation mValidation;
    private OnFragmentChangeStateListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment getInstance(OnFragmentChangeStateListener listener) {
        RegisterFragment instance = new RegisterFragment();
        instance.setListener(listener);
        return instance;
    }

    public void setListener(OnFragmentChangeStateListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new RegisterViewModel();
        View view = setAndBindContentView(inflater, container, R.layout.fragment_register, this);
        binding.setViewmodel(viewModel);
        mValidation = new RegisterValidation();
        binding.setValidation(mValidation);
        binding.setData(new RegisterRequest());

        initFilterGender();

        return view;
    }

    private void initFilterGender() {
        String[] list = getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner, R.id.textView, list);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.filterGender.setAdapter(adapter);
    }

    /*
    * Implement RegisterContract.View
    * */

    @Override
    public void clickLoginLink() {
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.OPEN_LOGIN);
        }
    }

    // không thể lấy đối tượng chứng thực
    @Override
    public void notGetAuth() {
        Toast.makeText(getContext(), getString(R.string.cant_get_auth), Toast.LENGTH_SHORT).show();
    }

    // email đã tồn tại
    @Override
    public void emailExist() {
        Toast.makeText(getContext(), getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
        binding.edtEmail.requestFocus();
    }

    // email có định dạng kém
    @Override
    public void badFormatEmail() {
        Toast.makeText(getContext(), getString(R.string.register_failed_email_bad_format), Toast.LENGTH_SHORT).show();
        binding.edtEmail.requestFocus();
    }

    // mật khẩu yếu (dưới 6 ký tự)
    @Override
    public void weakPassword() {
        Toast.makeText(getContext(), getString(R.string.register_failed_weak_password), Toast.LENGTH_SHORT).show();
        binding.edtPassword.setText("");
        binding.edtConfirmPassword.setText("");
        binding.edtPassword.requestFocus();
    }

    // không rõ lỗi
    @Override
    public void registerFailed() {
        Toast.makeText(getContext(), getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
    }

    // đăng ký thành công, đã lấy được FirebaseUser nhưng không tạo được thông tin tài khoản mới
    @Override
    public void notCreateUserData() {
        Toast.makeText(getContext(), getString(R.string.couldnt_create_user_profile), Toast.LENGTH_LONG).show();
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.REGISTER_SUCCESS);
        }
    }

    // đăng ký thành công nhưng không lấy được FirebaseUser
    @Override
    public void notGetFirebaseUser() {
        Toast.makeText(getContext(), getString(R.string.register_success_couldnt_get_firebase_user), Toast.LENGTH_LONG).show();
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.OPEN_LOGIN);
        }
    }

    @Override
    public void registerSuccess() {
        Toast.makeText(getContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onChange(AuthenticationActivity.REGISTER_SUCCESS);
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

    @Override
    public void onDestroy() {
        if (mValidation != null) {
            mValidation.destroy();
        }
        super.onDestroy();
    }
}
