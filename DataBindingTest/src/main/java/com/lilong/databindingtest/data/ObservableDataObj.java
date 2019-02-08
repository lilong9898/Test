package com.lilong.databindingtest.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * DataBinding的数据源（可变型）
 * (1) 在inflate时，其中数据被设置到UI上
 * (2) 此后数据改变，会再被设置到UI上
 *
 * (2)中的功能，要求数据类继承{@link BaseObservable}，
 * 并在数据改变时，调用{@link BaseObservable#notifyPropertyChanged(int)}方法
 */
public class ObservableDataObj extends BaseObservable {

    private String observableStr = "observable_str";

    public void setObservableStr(String observableStr) {
        this.observableStr = observableStr;
        notifyPropertyChanged(BR.observableStr);
    }

    @Bindable
    public String getObservableStr() {
        return observableStr;
    }
}
