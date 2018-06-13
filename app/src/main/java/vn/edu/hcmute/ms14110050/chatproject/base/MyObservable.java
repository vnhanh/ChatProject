package vn.edu.hcmute.ms14110050.chatproject.base;

import java.util.Observable;

/**
 * Created by Vo Ngoc Hanh on 6/8/2018.
 */

public class MyObservable extends Observable {

    protected void notify(Object object) {
        setChanged();
        notifyObservers(object);
    }
}
