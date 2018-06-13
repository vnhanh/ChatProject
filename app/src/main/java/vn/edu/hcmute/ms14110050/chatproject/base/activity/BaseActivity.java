package vn.edu.hcmute.ms14110050.chatproject.base.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;


public abstract class BaseActivity<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseViewModel>
        extends AppCompatActivity{

    protected B binding;
    protected VM viewModel;

    protected final void setAndBindContentView(@LayoutRes int layoutResID, V viewCallback) {
        if (viewModel == null) {
            throw new IllegalStateException("viewModel must already be set via injection");
        }
        binding = DataBindingUtil.setContentView(this, layoutResID);

        viewModel.onViewAttach(viewCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.onViewResumed();
        }
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.onViewDetached();
        }
        viewModel = null;
        binding = null;
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public String string(@StringRes int resId) {
        return getResources().getString(resId);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        getViewModel().onViewResumed();
    }

    @Override
    public void onStart() {
        super.onStart();
        getViewModel().onViewAttached(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getViewModel().onViewDetached();
    }*/
}
