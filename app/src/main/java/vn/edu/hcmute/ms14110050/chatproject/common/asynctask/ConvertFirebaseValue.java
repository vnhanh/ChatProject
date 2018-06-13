package vn.edu.hcmute.ms14110050.chatproject.common.asynctask;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.GetCallback;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ConvertFirebaseValue<MODEL> extends AsyncTask<DataSnapshot,Void,MODEL> {
    private GetCallback<MODEL> callback;
    private GenericTypeIndicator<MODEL> typeIndicator;

    public ConvertFirebaseValue(GetCallback<MODEL> callback, GenericTypeIndicator<MODEL> typeIndicator) {
        this.callback = callback;
        this.typeIndicator = typeIndicator;
    }

    @Override
    protected MODEL doInBackground(DataSnapshot... dataSnapshots) {
        DataSnapshot _dataSnapshot = dataSnapshots[0];
        if (_dataSnapshot == null || _dataSnapshot.getValue() == null) {
            return null;
        }

        return _dataSnapshot.getValue(typeIndicator);
    }

    @Override
    protected void onPostExecute(MODEL model) {
        super.onPostExecute(model);
        callback.onFinish(model);
    }
}
