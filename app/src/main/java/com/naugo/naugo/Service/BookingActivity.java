package com.naugo.naugo.Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.naugo.naugo.Adapter.MyViewPageAdapter;
import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Common.NonSwipeViewPager;
import com.naugo.naugo.Model.Drivers;
import com.naugo.naugo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;


public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference driverRef;
    DocumentReference driverDoc;

    @BindView (R.id.step_view)
    StepView stepView;
    @BindView (R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView (R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView (R.id.btn_next_step)
    Button btn_next_step;


    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3 || Common.step > 0){
            btn_next_step.setText("Next");
            Common.step--; //decreasing
            viewPager.setCurrentItem(Common.step);
            if(Common.step <3)
            {
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if(Common.step < 3 || Common.step == 0){
            Common.step++; //increase
            if(Common.step == 1) //after choose destination
            {
                //btn_next_step.setText("Pick Time");
                if(Common.currentFrom != null){
                    loadDriversByFrom(Common.currentFrom.getDestinationID());
                }
                else if (Common.step == 2) //after choose driver
                {
                    //btn_next_step.setText("Contact Driver");
                    if (Common.currentDriver != null)
                        loadTimeSlotOfDriver(Common.currentDriver.getDriversID());
                }
            }
            viewPager.setCurrentItem(Common.step);

        }

        //Toast.makeText(this, ""+Common.currentFrom.getDestinationID(), Toast.LENGTH_SHORT).show();

    }

    private void loadTimeSlotOfDriver(String driversID) {
        //send Local Broadcast to Step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadDriversByFrom(String destinationID) {
        dialog.show();

        //Now Showing All Drivers
        ///AllLocation/Kayubangkoa/Branch_from/Barang Caddi/Drivers

        if(!TextUtils.isEmpty(Common.city))
        driverRef = FirebaseFirestore.getInstance()
                .collection("AllLocation")
                .document(Common.city)
                .collection("Branch_from")
                .document(destinationID)
                .collection("Drivers");

        driverRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Drivers> drivers = new ArrayList<>();
                        for(QueryDocumentSnapshot driversnapshot:task.getResult())
                        {
                            Drivers drivers1 = driversnapshot.toObject(Drivers.class);
                            drivers1.setPassword("");//Remove bcs this is clientApp

                            drivers1.setDriversID(driversnapshot.getId());//get ID of drivers
                            drivers.add(drivers1);
                        }
                        //send broadcast to booking step 2 fragment to load Recycler
                        Intent intent = new Intent(Common.KEY_DRIVERS_LOAD_DONE);
                        intent.putParcelableArrayListExtra(Common.KEY_DRIVERS_LOAD_DONE, drivers);
                        localBroadcastManager.sendBroadcast(intent);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if (step == 1)
                Common.currentFrom = intent.getParcelableExtra(Common.KEY_DEPARTURE_STORE);
                //Common.currentFrom = intent.getParcelableExtra(Common.KEY_DRIVERS_LOAD_DONE);
            else if (step == 2)
                Common.currentDriver = intent.getParcelableExtra(Common.KEY_DRIVERS_SELECTED);

            //Common.currentFrom = intent.getParcelableExtra(Common.KEY_DEPARTURE_STORE);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy(){
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //view
        viewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2); //we have 3 fragment
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //showStep

                stepView.go(position, true);
                if (position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //set disable button
                btn_next_step.setEnabled(false);

                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setColorButton() {

        if(btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.drawable.btnenabled);
        }
        else {
            btn_next_step.setBackgroundResource(R.drawable.btndisabled);
        }

        if(btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.drawable.btnenabled);
        }
        else {
            btn_previous_step.setBackgroundResource(R.drawable.btndisabled);
        }
    }


    private void setupStepView() {
        List<String> steplist = new ArrayList<>();
        steplist.add("Route");
        steplist.add("Choose Driver");
        steplist.add("Contact Us");
        stepView.setSteps(steplist);

    }

}

