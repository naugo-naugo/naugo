package com.naugo.naugo.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Interface.IRecyclerItemSelectedListener;
import com.naugo.naugo.Model.Locate_from;
import com.naugo.naugo.R;

import java.util.ArrayList;
import java.util.List;

public class MyBranchDepartureAdapter extends RecyclerView.Adapter<MyBranchDepartureAdapter.MyViewHolder> {

    Context context;
    List<Locate_from> locate_fromList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyBranchDepartureAdapter(Context context, List<Locate_from> locate_fromList) {
        this.context = context;
        this.locate_fromList = locate_fromList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_departure, parent,false );

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.txt_departure_name.setText(locate_fromList.get(i).getName());
        if(!cardViewList.contains(holder.card_departure))
            cardViewList.add(holder.card_departure);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set white background to non selected card

                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));



                //set selected background to naugo theme
                holder.card_departure.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.holo_blue_bright));

                //send broadcast to bookingactivity to enable next button
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_DEPARTURE_STORE, locate_fromList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locate_fromList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_departure_name;
        CardView card_departure;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_departure_name = (TextView)itemView.findViewById(R.id.txt_departure_name);
            card_departure = (CardView)itemView.findViewById(R.id.card_departure);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(itemView, getAdapterPosition() );
        }
    }
}
