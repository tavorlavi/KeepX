package com.example.keepx;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Ticketsfrag extends Fragment {
    private CardView cd1, svcdt;
    private View rootView;
    private EditText search;
    private ImageButton x, back;
    private LinearLayout tickets;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private boolean found=true;

    private ImageButton st;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tickets, container, false);
        cd1 = rootView.findViewById(R.id.ttop);
        svcdt = rootView.findViewById(R.id.svcvt);
//        progressBar = rootView.findViewById(R.id.loadingprogressbartickets);
//        progressBar.show();
        tickets = rootView.findViewById(R.id.ticketslayout);
        mySwipeRefreshLayout = rootView.findViewById(R.id.reftickets);
        mySwipeRefreshLayout.setOnRefreshListener(() -> {
                LoadTickets();

                }
        );
        x = rootView.findViewById(R.id.xticketsearch);
        back = rootView.findViewById(R.id.backticketsearch);
        search = rootView.findViewById(R.id.eticketsearch);
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                for (int i=0;i<tickets.getChildCount();i++){
                    try {
                        final TicketView tick = (TicketView) tickets.getChildAt(i);
                        if (!tick.getTitle().contains(cs)){
                            tick.setVisibility(View.GONE);
                        }
                        else{
                            tick.setVisibility(View.VISIBLE);
                            found = true;}
                    }
                    catch (Exception e){

                    }
                }
                if (!found)
                    Toast.makeText(getContext(),"No tickets found", Toast.LENGTH_SHORT).show();
                found = false;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd1.setVisibility(View.VISIBLE);
                svcdt.setVisibility(View.GONE);
            }
        });
        st = rootView.findViewById(R.id.st);
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                svcdt.setVisibility(View.VISIBLE);
                cd1.setVisibility(View.GONE);
            }
        });
        svcdt.setVisibility(View.GONE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Ticketinfo");



        LoadTickets();
        // Inflate the layout for this fragment
        return rootView;
    }
    public void LoadTickets() {
        found=true;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tickets/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        tickets.removeAllViews();
        mySwipeRefreshLayout.setRefreshing(true);
        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                TicketView ticketView = new TicketView(getContext(), null);
                ticketView.setTitle(ticketinfo.getTitle());
                if (ticketinfo.getLocation() != null && !ticketinfo.getLocation().isEmpty())
                    ticketView.setLocation(ticketinfo.getLocation());
                ticketView.setFav(ticketinfo.getFav());
                if (ticketinfo.getDate() != null){
                    ticketView.setDate(ticketinfo.getDate());
                }
                if (ticketinfo.getQr()!=null){
                    ticketView.setQr(ticketinfo.getQr());
                }
                if (ticketinfo.getTime() != null){
                    ticketView.setTime(ticketinfo.getTime());
                }
                ticketView.setImage(ticketinfo.getImage());
                ticketView.setUid(ticketinfo.getUid());
                tickets.addView(ticketView);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                for (int i=0;i<tickets.getChildCount();i++){
                    try {
                        TicketView v =(TicketView)  tickets.getChildAt(i);
                        if (v.getImage().equals(ticketinfo.getImage())){
                            v.setImage(ticketinfo.getImage());
                            v.setFav(ticketinfo.getFav());
                            if (ticketinfo.getDate() != null){
                                v.setDate(ticketinfo.getDate());
                            }
                            if (ticketinfo.getTime() != null){
                                v.setTime(ticketinfo.getTime());
                            }
                            if (ticketinfo.getQr()!=null){
                                v.setQr(ticketinfo.getQr());
                            }
                            if (ticketinfo.getLocation() != null && !ticketinfo.getLocation().isEmpty())
                                v.setLocation(ticketinfo.getLocation());
                        }
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                for (int i=0;i<tickets.getChildCount();i++){
                    try {
                        TicketView v =(TicketView)  tickets.getChildAt(i);
                        if (v.getImage().equals(ticketinfo.getImage())){
                            tickets.removeView(v);
                        }
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mySwipeRefreshLayout.setRefreshing(false);

    }
}