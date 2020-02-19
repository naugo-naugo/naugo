package com.naugo.naugo.Common;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.naugo.naugo.Model.Drivers;
import com.naugo.naugo.Model.Locate_from;
import com.naugo.naugo.Model.UserModel;

import java.util.Calendar;

public class Common {
    public static final String USER_REFERENCES = "Users";
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_DEPARTURE_STORE = "DEPARTURE_SAVE";
    public static final String KEY_DRIVERS_LOAD_DONE = "DRIVERS_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_DRIVERS_SELECTED = "DRIVERS_SELECTED";
    public static final int TIME_SLOT_TOTAL = 1;
    public static final String IS_LOGIN = "false" ;
    public static UserModel currentUser;
    public static Locate_from currentFrom;
    public static int step = 0; //init first step = 0
    public static String city = "";
    public static Drivers currentDriver;
    public static String currentDriverPhone;
    public static Calendar currentDate= Calendar.getInstance();

    public static void setSpanstring(String welcome, String name, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(welcome);
        SpannableString spannableString = new SpannableString(name);
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldspan, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        textView.setText(builder, TextView.BufferType.SPANNABLE);
    }

    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "09:00 - 09:30";
            case 1:
                return "09:30 - 10:00";
            default:
                return "Closed";
        }
    }
}
