package com.naugo.naugo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkcurrentuser();
    }

    public void signUp(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    void checkcurrentuser(){
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

}
