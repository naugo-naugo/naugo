package com.naugo.naugo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Model.TimeSlot;
import com.naugo.naugo.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {


    Context context;
    List<TimeSlot> timeSlotList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();

    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_phone, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.txt_drivers_name.setText(Common.currentDriver.getName());
        holder.txt_destination.setText(timeSlotList.get(i).getDepart_to());
        holder.txt_departure.setText(timeSlotList.get(i).getDepart_from());
        holder.txt_boatname.setText(timeSlotList.get(i).getBoatname());
        //holder.txt_drivers_phone.setText(timeSlotList.get(i).getPhone());
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_drivers_name, txt_boatname, txt_destination, txt_departure;
        CardView card_time_slot;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            card_time_slot = (CardView)itemView.findViewById(R.id.card_Time);
            txt_drivers_name = (TextView)itemView.findViewById(R.id.txt_drivers_name);
            txt_destination = (TextView)itemView.findViewById(R.id.txt_driver_destination2);
            txt_departure = (TextView)itemView.findViewById(R.id.txt_drivers_departure2);
            txt_boatname = (TextView)itemView.findViewById(R.id.txt_boat_name2);
        }

    }





}
