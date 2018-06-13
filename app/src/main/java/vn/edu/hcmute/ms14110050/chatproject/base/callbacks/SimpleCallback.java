package vn.edu.hcmute.ms14110050.chatproject.base.callbacks;

/**
 * Created by Vo Ngoc Hanh on 5/21/2018.
 */

public interface SimpleCallback<DATA>  extends MyCallback{
    void onSuccess(DATA data);

    void onError();
}
