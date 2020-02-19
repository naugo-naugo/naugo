package com.naugo.naugo.ui.options;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Model.UserModel;
import com.naugo.naugo.R;

import java.util.List;

public class OptionsFragment extends Fragment {

    private OptionsViewModel optionsViewModel;
    Context context;
    List<UserModel> userModelList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionsViewModel =
                ViewModelProviders.of(this).get(OptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SignOut")
                .setMessage("Do You Want to Sign Out?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
         */
        final TextView textView = root.findViewById(R.id.text_send);
        final TextView username = root.findViewById(R.id.txt_name);
        final TextView phone = root.findViewById(R.id.txt_phone);
        //final TextView location = root.findViewById(R.id.txt_location);
       username.setText(Common.currentUser.getName());
       phone.setText(Common.currentUser.getPhone());
       //location.setText(Common.currentUser.getAddress());
        optionsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);

            }
        });

        return root;
    }
}