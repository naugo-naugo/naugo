package com.naugo.naugo.Interface;

import java.util.List;

public interface IAllLocationLoadListener {
    void onAllLocationLoadSuccess(List<String> areaNameList);
    void onAllLocationLoadFailed(String message);
}
