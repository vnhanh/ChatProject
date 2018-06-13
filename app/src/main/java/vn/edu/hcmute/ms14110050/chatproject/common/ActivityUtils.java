package vn.edu.hcmute.ms14110050.chatproject.common;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class ActivityUtils {
    public static void replaceFragment(FragmentManager manager, Fragment fragment, @IdRes int layoutRes) throws Exception {
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(layoutRes, fragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        } catch (Exception e) {
            throw e;
        }
    }
}
