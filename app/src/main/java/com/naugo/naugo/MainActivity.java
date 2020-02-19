package com.naugo.naugo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Model.UserModel;
import com.naugo.naugo.Remote.ICloudFunctions;
import com.naugo.naugo.Remote.RetrofitCloudClient;
import com.naugo.naugo.Service.BookingActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int APP_REQUEST_CODE = 7171; //any number OK

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ICloudFunctions cloudFunctions;

    private DatabaseReference userRef;
    private List<AuthUI.IdpConfig> providers;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);
        init();
        
    }

    private void init() {
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        dialog.setCanceledOnTouchOutside(false);
        cloudFunctions = RetrofitCloudClient.getInstance().create(ICloudFunctions.class);
        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null)
            {
                        checkUserFromFirebase(user);
            }
            else
            {
                phoneLogin();
            }
        };

    }

    private void checkUserFromFirebase(FirebaseUser user) {
        dialog.show();
        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            //Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent (MainActivity.this, HomeActivity.class);
                            //intent.putExtra(Common.IS_LOGIN, true);
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            goToHomeActivity(userModel);
                            //startActivity(intent);
                        }
                        else
                        {
                            showRegisterDialog(user);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void showRegisterDialog(FirebaseUser user) {
        androidx.appcompat.app.AlertDialog.Builder builder = new  androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Register");
        builder.setTitle("Please Fill Your Information");

        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        EditText edt_name = (EditText)itemView.findViewById(R.id.edt_name);
        //EditText edt_address = (EditText)itemView.findViewById(R.id.edt_address);
        EditText edt_phone = (EditText)itemView.findViewById(R.id.edt_phone);

        //set
        edt_phone.setText(user.getPhoneNumber());

        builder.setView(itemView);
        /*
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            goToWelcome();
            dialog.dismiss();
        });
         */
        builder.setPositiveButton("REGISTER", (dialogInterface, i) -> {
            if(TextUtils.isEmpty(edt_name.getText().toString()))
            {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                return;
            }
            /*
            else if(TextUtils.isEmpty(edt_address.getText().toString())){
                Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                return;
            }
             */

            UserModel userModel = new UserModel();
            userModel.setUid(user.getUid());
            userModel.setName(edt_name.getText().toString());
            //userModel.setAddress(edt_address.getText().toString());
            userModel.setPhone(edt_phone.getText().toString());

            userRef.child(user.getUid())
                    .setValue(userModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                dialogInterface.dismiss();
                                Toast.makeText(MainActivity.this, "Congratulation! Register Successfull!", Toast.LENGTH_SHORT).show();
                                goToHomeActivity(userModel);
                            }
                        }
                    });

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void goToHomeActivity(UserModel userModel) {
        Intent intent = new Intent (MainActivity.this, HomeActivity.class);
        Common.currentUser = userModel; // Important, must fill the value
        intent.putExtra(Common.IS_LOGIN, true);
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    private void goToWelcome() {
        Intent i = new Intent(MainActivity.this,WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    private void phoneLogin() {

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(),
                APP_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else
                {
                    // Toast.makeText(this, "Please Register First!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, BookingActivity.class);
        this.startActivity(intent);
    }

}
