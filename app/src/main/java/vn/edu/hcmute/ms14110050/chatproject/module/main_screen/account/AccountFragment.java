package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.fragment.BaseFragment;
import vn.edu.hcmute.ms14110050.chatproject.common.view.ImagePickerHelper;
import vn.edu.hcmute.ms14110050.chatproject.databinding.FragmentAccountBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.LayoutChangePasswordBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.LayoutVerifyAccountBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.user.User;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation.ChangePasswordValidation;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation.EditAccountValidation;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.account.validation.VerifyAccountValidation;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity.MainActivity;

import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_CAMERA;
import static vn.edu.hcmute.ms14110050.chatproject.common.constant.PERMISSION.REQUEST_IMAGE_GALLERY;


// TODO: Cần đồng bộ kích cỡ text input và button giữa các view
public class AccountFragment extends BaseFragment<FragmentAccountBinding, AccountContract.View, AccountViewModel>
        implements AccountContract.View{

    EditAccountValidation validation;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new AccountViewModel(getResources());
        View view = setAndBindContentView(inflater, container, R.layout.fragment_account, this);

        binding.setViewmodel(viewModel);
        validation = new EditAccountValidation(getContext());
        binding.vgEditAccount.setData(new User());
        binding.vgEditAccount.setValidation(validation);

        // gán listener lắng nghe thay đổi thông tin người dùng trên Firebase
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnUpdateUserListener(viewModel);
        }

        updateUserData(new User());

        initImage();

        initGenderSpinner();

        return view;
    }

    private void initImage() {
        binding.vgEditAccount.imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePickerHelper.openImagePicker(AccountFragment.this);
            }
        });
    }

    // Nhận ảnh trả về
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    Uri uri = data.getData();
                    viewModel.uploadImageProfile(uri);

                    break;

                case REQUEST_IMAGE_GALLERY:
                    uri = data.getData();
                    viewModel.uploadImageProfile(uri);
                    break;
            }
        }
    }

    private void initGenderSpinner() {
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_spinner_item, android.R.id.text1,
                        getResources().getStringArray(R.array.genders));
        binding.vgEditAccount.filterGender.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_password:
                viewModel.onClickChangePassword();
                return true;

            case R.id.menu_logout:
                viewModel.onClickLogoutMenuItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        validation.destroy();
        super.onDestroy();
    }

    @Override
    public void onSwitchViewGroup() {
        binding.txtMessage.setText("");
    }

    @Override
    public void uploadImageFailed() {
        Toast.makeText(getContext(), getString(R.string.upload_image_profile_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userProfileNotChanged() {
        Toast.makeText(getContext(), getString(R.string.user_info_not_changed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUserProfileSuccess() {
        Toast.makeText(getContext(), getString(R.string.change_user_profile_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUserProfileFailed() {
        showAllMessage(R.string.change_user_profile_failed);
    }

    @Override
    public void updateEmailFailed() {
        showAllMessage(R.string.change_email_user_failed);
    }

    @Override
    public void verifyPasswordFailed() {
        Toast.makeText(getContext(), getString(R.string.verify_password_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePasswordSuccess() {
        Toast.makeText(getContext(), getString(R.string.change_user_password_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePasswordFailed() {
        showAllMessage(R.string.change_user_password_failed);
    }

    @Override
    public void messageCountErrorProcessData() {
        showAllMessage(R.string.count_error_processing_data);
    }

    private void showAllMessage(@StringRes int idRes) {
        Toast.makeText(getContext(), getString(idRes), Toast.LENGTH_SHORT).show();
        binding.txtMessage.setText(getString(idRes));
        binding.txtMessage.setVisibility(View.VISIBLE);
    }

    // ViewModel load thông tin tài khoản thành công từ Firebase
    @Override
    public void loadUserProfile(User user) {
        if (user == null) {
            loadUserProfileFailed();
        }else{
            loadUserProfileSuccess(user);
        }
    }

    @Override
    public void reloadUserProfile(User profile) {
        updateUserData(profile);
    }

    private void loadUserProfileSuccess(User user) {
        updateUserData(user);
    }

    private void loadUserProfileFailed() {
        showAllMessage(R.string.update_user_failed);
    }

    // bảo đảm validation luôn giữ tham chiếu đến đối tượng User mới nhất
    private void updateUserData(User user) {
        binding.vgEditAccount.setData(user);
    }

    // cài đặt và hiển DatePicker chọn ngày sinh
    @Override
    public void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            // chạy 2 lần khi chọn xong
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.vgEditAccount.txtShowBirthdate.setText(day + "/" + (month + 1) + "/" + year);
            }
        };
        String str = binding.vgEditAccount.txtShowBirthdate.getText().toString();
        String temp[] = str.split("/");
        int day = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int year = Integer.parseInt(temp[2]);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), callback, year, month-1, day);
        dialog.setTitle(getString(R.string.choose_birthdate));
        dialog.show();
    }

    @Override
    public void showDialogVerifyAccount() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getView().getContext());
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final LayoutVerifyAccountBinding verifyAccountBinding = DataBindingUtil.inflate(inflater, R.layout.layout_verify_account, null, false);
        builder.setView(verifyAccountBinding.getRoot());
        verifyAccountBinding.setViewmodel(viewModel);
        verifyAccountBinding.setValidation(new VerifyAccountValidation());
        verifyAccountBinding.setPassword(new String());
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        verifyAccountBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel != null) {
                    viewModel.onSubmitVerifyAccount(dialog, verifyAccountBinding.getPassword());
                }
            }
        });
        verifyAccountBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showDialogChangePassword() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getView().getContext());
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final LayoutChangePasswordBinding changePasswordBinding =
                DataBindingUtil.inflate(inflater, R.layout.layout_change_password, null, false);
        builder.setView(changePasswordBinding.getRoot());
        changePasswordBinding.setViewmodel(viewModel);
        changePasswordBinding.setValidation(new ChangePasswordValidation());
        changePasswordBinding.setPassword(new String());
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        changePasswordBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel != null) {
                    viewModel.onSubmitChangePassword(dialog, changePasswordBinding.getPassword());
                }
            }
        });
        changePasswordBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void clearAllError() {
        binding.txtMessage.setText("");
    }
}
