package com.naugo.naugo.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Adapter.MyDriversAdapter;
import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Common.SpacesItemDecoration;
import com.naugo.naugo.Model.Drivers;
import com.naugo.naugo.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_drivers)
    RecyclerView recyclerDriver;



    private BroadcastReceiver driversdonereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Drivers> driversArrayList = intent.getParcelableArrayListExtra(Common.KEY_DRIVERS_LOAD_DONE);
            //create adapter late
            MyDriversAdapter adapter = new MyDriversAdapter(getContext(), driversArrayList);
            recyclerDriver.setAdapter(adapter);

        }
    };


    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if(instance==null)
        {
            instance = new BookingStep2Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(driversdonereceiver, new IntentFilter(Common.KEY_DRIVERS_LOAD_DONE));
    }

    @Override
    public void  onDestroy(){
        localBroadcastManager.unregisterReceiver(driversdonereceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container,false);


        unbinder = ButterKnife.bind(this, itemView);
        initView();

        return itemView;
    }

    private void initView() {
        recyclerDriver.setHasFixedSize(true);
        recyclerDriver.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerDriver.addItemDecoration(new SpacesItemDecoration(4));
    }
}
