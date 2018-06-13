package vn.edu.hcmute.ms14110050.chatproject.base.life_cycle;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Vo Ngoc Hanh on 5/23/2018.
 */

public interface LifeCycle {
    interface View{
        Context getContext();
    }

    interface ViewModel<V extends View>{
        void onViewAttach(@NonNull V viewCallback);

        void onViewResumed();

        void onViewDetached();
    }
}
