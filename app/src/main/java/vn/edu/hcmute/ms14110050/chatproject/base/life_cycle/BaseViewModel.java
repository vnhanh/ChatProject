package vn.edu.hcmute.ms14110050.chatproject.base.life_cycle;

import android.support.annotation.NonNull;


public abstract class BaseViewModel <V extends LifeCycle.View> implements LifeCycle.ViewModel<V> {

    private V view;

    @Override
    public void onViewAttach(@NonNull V viewCallback) {
        this.view = viewCallback;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    public final boolean isViewAttached() {
        return view != null;
    }

    public final V getView() {
        return view;
    }
}
