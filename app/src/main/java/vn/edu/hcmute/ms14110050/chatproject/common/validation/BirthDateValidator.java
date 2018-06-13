package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class BirthDateValidator extends Observable implements TextViewBindingAdapter.AfterTextChanged, IValidator{
    private WeakReference<Resources> weakResources;
    public final ObservableField<String> error = new ObservableField<>("");
    private boolean isValid = false;

    public BirthDateValidator(Resources resources) {
        this.weakResources = new WeakReference<Resources>(resources);
    }

    @Override
    public boolean getValid() {
        return isValid;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String dateStr = editable.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date birthDate = null;
        try {
            birthDate = sdf.parse(dateStr);
            checkValid(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG", "User class:setBirthDateStrValue():parse date string:error: " + e.getMessage());
            error.set(weakResources.get().getString(R.string.validate_birthdate_err2));
        }
    }

    private void checkValid(Date birthDate) {
        Date now = new Date();
        long timeDiff = now.getTime() - birthDate.getTime();
        double age = timeDiff / 3.15576e+10;
        isValid = age > 5;

        setError();
        updateValid();
    }

    private void setError() {
        if (!isValid) {
            error.set(weakResources.get().getString(R.string.validate_birthdate_err));
        }else{
            error.set("");
        }
    }

    protected void updateValid() {
        setChanged();
        notifyObservers(Constant.TAG_VALIDATE);
    }

    public void destroy() {
        weakResources = null;
    }
}
