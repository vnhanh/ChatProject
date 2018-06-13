package vn.edu.hcmute.ms14110050.chatproject.common.validation;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import vn.edu.hcmute.ms14110050.chatproject.common.constant.Constant;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public abstract class BaseValidation implements Observer{
    protected ArrayList<IValidator> validators = new ArrayList<>();
    public final ObservableBoolean valid = new ObservableBoolean(false);

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Integer) {
            Integer tag = (Integer) o;
            if (tag == Constant.TAG_VALIDATE) {
                checkValid();
            }
        }
    }

    protected void checkValid() {
        boolean check = true;
        for (IValidator validator : validators) {
            if (!validator.getValid()) {
                check = false;
                break;
            }
        }
        valid.set(check);
    }

    public void registerValidator(IValidator validator) {
        if (validator instanceof Observable) {
            ((Observable) validator).addObserver(this);
        }
        validators.add(validator);
    }

    public void destroy() {

    }
}
