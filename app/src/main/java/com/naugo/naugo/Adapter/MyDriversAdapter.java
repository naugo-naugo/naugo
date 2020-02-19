package com.naugo.naugo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Interface.IRecyclerItemSelectedListener;
import com.naugo.naugo.Model.Drivers;
import com.naugo.naugo.R;

import java.util.ArrayList;
import java.util.List;

public class MyDriversAdapter extends RecyclerView.Adapter<MyDriversAdapter.MyViewHolder> {

    Context context;
    List<Drivers> driversList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyDriversAdapter(Context context, List<Drivers> driversList) {
        this.context = context;
        this.driversList = driversList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_drivers, parent,false );

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_drivers_name.setText(driversList.get(position).getName());
        holder.txt_drivers_boatname.setText(driversList.get(position).getBoatname());
        holder.txt_drivers_departure.setText(driversList.get(position).getDepart_from());
        holder.txt_drivers_destination.setText(driversList.get(position).getDepart_to());
        holder.txt_drivers_departure_time.setText(driversList.get(position).getDeparting_time());
        holder.txt_drivers_destination_arrive.setText(driversList.get(position).getDeparting_arrive());
        holder.ratingBar.setRating((float)driversList.get(position).getRating());

        if(!cardViewList.contains(holder.card_Drivers))
            cardViewList.add(holder.card_Drivers);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set Background to white for not chosen
                for(CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                //set Background for choice
                holder.card_Drivers.setCardBackgroundColor(
                        context.getResources()
                        .getColor(R.color.naugoTheme)
                );

                //send local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_DRIVERS_SELECTED, driversList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return driversList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_drivers_name;
        TextView txt_drivers_departure;
        TextView txt_drivers_destination;
        TextView txt_drivers_departure_time;
        TextView txt_drivers_destination_arrive;
        TextView txt_drivers_boatname;
        RatingBar ratingBar;
        CardView card_Drivers;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_Drivers = (CardView)itemView.findViewById(R.id.card_Drivers);
            txt_drivers_name = (TextView)itemView.findViewById(R.id.txt_drivers_name);
            txt_drivers_boatname = (TextView)itemView.findViewById(R.id.txt_boat);
            txt_drivers_departure = (TextView)itemView.findViewById(R.id.txt_drivers_departure);
            txt_drivers_destination = (TextView)itemView.findViewById(R.id.txt_driver_destination);
            txt_drivers_departure_time = (TextView)itemView.findViewById(R.id.txt_drivers_departure_time);
            txt_drivers_destination_arrive = (TextView)itemView.findViewById(R.id.txt_drivers_destination_arrive);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_drivers);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
