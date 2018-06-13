package vn.edu.hcmute.ms14110050.chatproject.common.asynctask;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import vn.edu.hcmute.ms14110050.chatproject.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.chatproject.common.SendObject;

/**
 * Created by Vo Ngoc Hanh on 6/10/2018.
 */

public class ConvertBundleFirebaseValue<MODEL> extends AsyncTask<SendObject,Void,MODEL> {
    private GetCallback<SendObject> callback;
    private GenericTypeIndicator<MODEL> typeIndicator;
    private int index = -1;

    public ConvertBundleFirebaseValue(GetCallback<SendObject> callback, GenericTypeIndicator<MODEL> typeIndicator) {
        this.callback = callback;
        this.typeIndicator = typeIndicator;
    }

    @Override
    protected MODEL doInBackground(SendObject... sendObjects) {
        SendObject sendObject = sendObjects[0];
        if (sendObject == null || sendObject.getValue() == null) {
            return null;
        }
        DataSnapshot _dataSnapshot = (DataSnapshot) sendObject.getValue();
        if (_dataSnapshot.getValue() == null) {
            return null;
        }
        index = sendObject.getTag();

        return _dataSnapshot.getValue(typeIndicator);
    }

    @Override
    protected void onPostExecute(MODEL model) {
        super.onPostExecute(model);
        callback.onFinish(new SendObject(index, model));
    }
}
