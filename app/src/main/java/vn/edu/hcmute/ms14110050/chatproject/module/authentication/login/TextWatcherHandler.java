package vn.edu.hcmute.ms14110050.chatproject.module.authentication.login;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Observable;
import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant;
import vn.edu.hcmute.ms14110050.chatproject.common.validation.IValidator;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class TextWatcherHandler extends Observable implements TextWatcher, IValidator{

    EditText editText;
    TextInputLayout textInputLayout;
    protected Pattern pattern;

    protected int errMsgId = -1;
    protected String errMsg;

    protected boolean isValid = false;

    public TextWatcherHandler() {

    }

    public TextWatcherHandler(String reg, @StringRes int errMsgId) {
        this.errMsgId = errMsgId;
        if (!StringUtils.isEmpty(reg)) {
            pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }
    }

    /*
    * IValidatior implement
    * */

    @Override
    public boolean getValid() {
        return this.isValid;
    }
    /*
    * END
    * */

    public void setEditText(EditText editText) {
        this.editText = editText;
        editText.addTextChangedListener(this);
        if (errMsg == null) {
            try {
                errMsg = editText.getContext().getString(errMsgId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTextInputLayout(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    public String getContent() {
        return editText.getText().toString();
    }

    /*
    * TextWatcher implement
    * */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (pattern != null) {
            isValid = pattern.matcher(editable.toString()).matches();

            setError();
            updateValid();
        }
    }

    /*
    * END
    * */

    protected void setError() {
        if (textInputLayout != null) {
            textInputLayout.setError(isValid ? "" : errMsg);
        } else if (editText != null) {
            editText.setError(isValid ? "" : errMsg);
        }
    }

    protected void updateValid() {
        setChanged();
        notifyObservers(Constant.TAG_VALIDATE);
    }

    // Call this method to destroy resource
    public void destroy() {
        textInputLayout = null;
        editText = null;
    }
}
