package vn.edu.hcmute.ms14110050.chatproject.base.recyclerview;

import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public abstract class BaseViewHolder<DATABINDING extends ViewDataBinding, VIEWMODEL extends Observable>
                            extends RecyclerView.ViewHolder implements Observer{

    protected DATABINDING binding;
    protected VIEWMODEL viewmodel;

    public BaseViewHolder(DATABINDING binding) {
        super(binding.getRoot());

        this.binding = binding;

        viewmodel = initViewModel();
        if (viewmodel != null) {
            viewmodel.addObserver(this);
        }
    }

    protected Resources getResources() {
        return binding != null ? binding.getRoot().getResources() : null;
    }

    protected abstract VIEWMODEL initViewModel();
}
