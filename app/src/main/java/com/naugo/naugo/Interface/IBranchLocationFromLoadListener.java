package com.naugo.naugo.Interface;

import com.naugo.naugo.Model.Locate_from;

import java.util.List;

public interface IBranchLocationFromLoadListener {
    void onBranchFromLoadSuccess(List<Locate_from> locationList);
    void onBranchFromLoadFailed(String message);
}
