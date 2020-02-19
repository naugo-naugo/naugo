package com.naugo.naugo.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Adapter.MyTimeSlotAdapter;
import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Common.SpacesItemDecoration;
import com.naugo.naugo.Interface.ITimeSlotLoadListener;
import com.naugo.naugo.Model.TimeSlot;
import com.naugo.naugo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    DocumentReference driverDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;

    //@BindView(R.id.card_Drivers)
    //CardView card_Drivers;

    @BindView(R.id.callNaugoWA)
    Button callNaugo;

    @BindView(R.id.callNaugoFB)
    Button callNaugoFB;

    @BindView(R.id.callNaugoSkype)
    Button callNaugoSkype;

    @BindView(R.id.callDriver)
    Button callDrivers;

    @BindView(R.id.smsDriver)
    Button smsDrivers;

    @BindView(R.id.showdriver)
    Button showdriver;

    @BindView(R.id.txt_contact)
    TextView txt_contact;

    @BindView(R.id.tvcontactdriver)
    TextView tvcontactdriver;

    @BindView(R.id.tvindonesianonly)
    TextView tvindonesianonly;

    @BindView(R.id.tvcontactus)
    TextView tvcontactus;

    @BindView(R.id.tvenglishonly)
    TextView tvenglishonly;


    //@BindView(R.id.calendar_view)
    //HorizontalCalendarView calendarView;
    //SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Calendar date = Calendar.getInstance();
            //date.add(Calendar.DATE, 0); //add current date
            loadPhoneNumberOfDriver(Common.currentDriver.getDriversID());
            //create adapter late
            //recycler_time_slot.setAdapter(adapter);
        }
    };

    private void loadPhoneNumberOfDriver(String driversName) {
        //dialog.show();
        //AllLocation/Kayubangkoa/Branch_from/Barang Caddi/Drivers/Driver1

        driverDoc = FirebaseFirestore.getInstance()
                .collection("AllLocation")
                .document(Common.city)
                .collection("Branch_from")
                .document(Common.currentFrom.getDestinationID())
                .collection("Drivers")
                .document(Common.currentDriver.getDriversID());

        //getInformation of This Driver

        driverDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists())
                    {
                        //Get Information of Booking
                        //If not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllLocation")
                                .document(Common.city)
                                .collection("Branch_from")
                                .document(Common.currentFrom.getDestinationID())
                                .collection("Drivers")
                                .document(Common.currentDriver.getDriversID())
                                .collection("phoneNumber");

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) {
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    } else {
                                        //if have appointment
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);

                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }

                }
            }
        });
    }

    static BookingStep3Fragment instance;
    public static BookingStep3Fragment getInstance(){
        if(instance==null)
        {
            instance = new BookingStep3Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
        //simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy"); // 28_03_2019
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0); //init current date
    }

    @Override
    public void onDestroy(){
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View itemView = inflater.inflate(R.layout.fragment_booking_step_three,container, false);
        //View phone = inflater.inflate(R.layout.layout_phone,container, false);
        unbinder = ButterKnife.bind(this, itemView);

        Button driverCallbtn = (Button)itemView.findViewById(R.id.callDriver);
        Button naugoCallbtnWA  = (Button)itemView.findViewById(R.id.callNaugoWA);
        Button naugoCallbtnFB  = (Button)itemView.findViewById(R.id.callNaugoFB);
        Button naugoCallbtnSkype  = (Button)itemView.findViewById(R.id.callNaugoSkype);
        Button driverSMSbtn  = (Button)itemView.findViewById(R.id.smsDriver);
        Button drivershowbtn = (Button)itemView.findViewById(R.id.showdriver);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drivershowbtn.performClick();
            }
        }, 10000);
         */

        drivershowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoneNumberOfDriver(Common.currentDriver.getDriversID());
                //card_Drivers.setVisibility(View.VISIBLE);
                callDrivers.setVisibility(View.VISIBLE);
                //smsDrivers.setVisibility(View.VISIBLE);
                callNaugo.setVisibility(View.VISIBLE);
                txt_contact.setVisibility(View.VISIBLE);
                tvcontactdriver.setVisibility(View.VISIBLE);
                tvindonesianonly.setVisibility(View.VISIBLE);
                callNaugoFB.setVisibility(View.VISIBLE);
                callNaugoSkype.setVisibility(View.VISIBLE);
                tvcontactus.setVisibility(View.VISIBLE);
                tvenglishonly.setVisibility(View.VISIBLE);
            }
        });

        driverCallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = (Common.currentDriver.getPhone());
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        driverSMSbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address", Common.currentDriver.getPhone());
                sendIntent.putExtra("sms_body","Hi Captain "+Common.currentDriver.getName()+"!"+" I want to Booking Your Boat on "+ Common.currentDate.getTime()+ " from "+Common.currentDriver.getDepart_from()+" to "+Common.currentDriver.getDepart_to());
                startActivity(sendIntent);
            }
        });

        naugoCallbtnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "+65 8668 5707";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        naugoCallbtnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peopleId = "100045325517518";
                Uri uri = Uri.parse("fb-messenger://user/");
                uri = ContentUris.withAppendedId(uri, Long.parseLong(peopleId));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        naugoCallbtnSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = "+65 8668 5707";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("skype:" + user_name));
                startActivity(i);
                //Intent sky = new Intent("android.intent.action.VIEW");
               // sky.setData(Uri.parse("skype:" + user_name));
                //startActivity(sky);
            }
        });

        init(itemView);

        return itemView;

    }

    private void init(View itemView) {
        //card_Drivers.setVisibility(View.VISIBLE);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        //recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));
        //loadPhoneNumberOfDriver(Common.currentDriver.getName(), Common.currentDriverPhone.getPhone());


        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 7); //7 Days Left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendar_view)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis())
                {
                    Common.currentDate = date; //this code wont load again if you select new day same with date selected
                    loadPhoneNumberOfDriver(Common.currentDriver.getDriversID());
                    //card_Drivers.setVisibility(View.VISIBLE);
                    callDrivers.setVisibility(View.VISIBLE);
                    //smsDrivers.setVisibility(View.VISIBLE);
                    callNaugo.setVisibility(View.VISIBLE);
                    txt_contact.setVisibility(View.VISIBLE);
                    tvcontactdriver.setVisibility(View.VISIBLE);
                    tvindonesianonly.setVisibility(View.VISIBLE);
                    callNaugoFB.setVisibility(View.VISIBLE);
                    callNaugoSkype.setVisibility(View.VISIBLE);
                    tvcontactus.setVisibility(View.VISIBLE);
                    tvenglishonly.setVisibility(View.VISIBLE);
                    //String name2 = (Common.currentDriver.getPhone());
                    //name.setText(name2);
                }
            }
        });



    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), timeSlotList);
        loadPhoneNumberOfDriver(Common.currentDriver.getDriversID()); //Ini yang kasih berganti setiap select new driver
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }



    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        loadPhoneNumberOfDriver(Common.currentDriver.getDriversID()); //Ini yang kasih berganti setiap select new driver
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }



}
