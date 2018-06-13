package vn.edu.hcmute.ms14110050.chatproject.common.databinding;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import vn.edu.hcmute.ms14110050.chatproject.common.StringUtils;

/**
 * Created by Vo Ngoc Hanh on 5/21/2018.
 */

public class SpinnerBindingUtils {

    /*
    * Cho spinner có giá trị String
    * */
    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void setValue(Spinner spinner, String value, final InverseBindingListener inverseBindingAdapter) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inverseBindingAdapter.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!StringUtils.isEmpty(value)) {
            int pos = ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(value);
            spinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static String getStringValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }

    /*
    * Cho spinner có giá trị boolean
    * */
    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void setValue(final Spinner spinner, final boolean value, final InverseBindingListener inverseBindingAdapter) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inverseBindingAdapter.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int pos = value ? 0 : 1;
        spinner.setSelection(pos, true);
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static boolean getBooleanValue(Spinner spinner) {
        return spinner.getSelectedItemPosition() == 0;
    }
}
