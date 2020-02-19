package com.naugo.naugo.ui.options;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OptionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Naugo Options Menu");
    }

    public LiveData<String> getText() {
        return mText;
    }
}