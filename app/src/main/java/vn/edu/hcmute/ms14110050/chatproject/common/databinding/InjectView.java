package vn.edu.hcmute.ms14110050.chatproject.common.databinding;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import vn.edu.hcmute.ms14110050.chatproject.module.authentication.login.TextWatcherHandler;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class InjectView {

    @BindingAdapter("addTextWatcher")
    public static void addTextWatcher(EditText editText, TextWatcherHandler handler) {
        handler.setEditText(editText);
    }

    @BindingAdapter("addErrorViewer")
    public static void addErrorViewer(TextInputLayout textInputLayout, TextWatcherHandler handler) {
        handler.setTextInputLayout(textInputLayout);
    }
}
