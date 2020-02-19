package com.naugo.naugo.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naugo.naugo.Adapter.MyBranchDepartureAdapter;
import com.naugo.naugo.Common.Common;
import com.naugo.naugo.Common.SpacesItemDecoration;
import com.naugo.naugo.Interface.IAllLocationLoadListener;
import com.naugo.naugo.Interface.IBranchLocationFromLoadListener;
import com.naugo.naugo.Model.Locate_from;
import com.naugo.naugo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllLocationLoadListener, IBranchLocationFromLoadListener {

    static BookingStep1Fragment instance;

    //collection variables
    CollectionReference allLocationref;
    CollectionReference branchref;

    IAllLocationLoadListener iAllLocationLoadListener;
    IBranchLocationFromLoadListener iBranchLocationFromLoadListener;

    //TextView
    @BindView(R.id.tv_to)
    TextView tv_to;
    @BindView(R.id.tv_yourLocation)
    TextView tv_yourLocation;

    //spinner Location
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    //spinner destination
    //@BindView(R.id.spinner2)
    //MaterialSpinner spinner2;


    @BindView(R.id.recycler_departure)
    RecyclerView recycler_departure;

    /*DATE METHOD
    @BindView(R.id.date_text)
    EditText date_text;

    DatePickerDialog datePickerDialog;
     */

    AlertDialog dialog;
    Unbinder unbinder;


    public static BookingStep1Fragment getInstance(){
        if(instance==null)
        {
            instance = new BookingStep1Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        allLocationref = FirebaseFirestore.getInstance().collection("AllLocation");
        iAllLocationLoadListener = this;
        iBranchLocationFromLoadListener = this;


        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one, container,false);

        unbinder = ButterKnife.bind(this, itemView);
        loadAllLocation();

        /*DatePicker
        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAA();
            }
        });
        */
        
        initView();

        return itemView;
    }


    private void initView() {
        recycler_departure.setHasFixedSize(true);
        recycler_departure.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_departure.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllLocation() {
        allLocationref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please Choose Harbor");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllLocationLoadListener.onAllLocationLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllLocationLoadListener.onAllLocationLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllLocationLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0 )
                {
                    loadBranchOfFrom(item.toString());
                    Toast.makeText(getContext(), "You Select "+ item, Toast.LENGTH_SHORT).show();
                }
                else
                    tv_yourLocation.setVisibility(View.VISIBLE);
                    tv_to.setVisibility(View.GONE);
                    recycler_departure.setVisibility(View.GONE);
            }
        });


        //spinner2.setItems(areaNameList);
    }

    private void loadBranchOfFrom(String departureFrom) {
        dialog.show();
        Common.city = departureFrom;

        branchref = FirebaseFirestore.getInstance()
                .collection("AllLocation")
                .document(departureFrom)
                .collection("Branch_from");

        branchref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Locate_from> list = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        Locate_from locate_from = documentSnapshot.toObject(Locate_from.class);
                        locate_from.setDestinationID(documentSnapshot.getId());
                        list.add(locate_from);
                    }


                    iBranchLocationFromLoadListener.onBranchFromLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLocationFromLoadListener.onBranchFromLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllLocationLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchFromLoadSuccess(List<Locate_from> locationList) {
        MyBranchDepartureAdapter adapter = new MyBranchDepartureAdapter(getActivity(), locationList);
        recycler_departure.setAdapter(adapter);
        recycler_departure.setVisibility(View.VISIBLE);
        tv_to.setVisibility(View.VISIBLE);
        tv_yourLocation.setVisibility(View.GONE);
        dialog.dismiss();

    }

    @Override
    public void onBranchFromLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
