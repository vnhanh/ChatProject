package vn.edu.hcmute.ms14110050.chatproject.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.BaseViewModel;
import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;


public abstract class BaseFragment<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseViewModel>
        extends Fragment {

    protected B binding;
    protected VM viewModel;

    /* Sets the content view, creates the binding and attaches the view to the view model */
    protected final View setAndBindContentView(@NonNull LayoutInflater inflater,
                                               @Nullable ViewGroup container,
                                               @LayoutRes int layoutResID,
                                               V viewCallback) {
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false);
        if (viewModel == null) {
            throw new IllegalStateException("viewmodel must be create");
        }
        viewModel.onViewAttach(viewCallback);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.onViewResumed();
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onViewDetached();
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
}
