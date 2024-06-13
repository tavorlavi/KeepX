package com.example.keepx;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavFragment extends Fragment {
    private CardView cd1, svcdf;
    private View rootView;
    private ProgressDialog progressDialog;
    private EditText search;
    private ImageButton x, back, sf;
    LinearLayout favs;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private SwipeRefreshLayout mySwipeRefreshLayout;


        public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fav, container, false);
//        bottomNavigationView = rootView.findViewById(R.id.bottomNavigationView);

//        dont allowclicks until loaded
//        bottomNavigationView.setVisibility(View.GONE);
        cd1 = rootView.findViewById(R.id.ftop);
        favs = rootView.findViewById(R.id.favlayout);
        mySwipeRefreshLayout = rootView.findViewById(R.id.reffav);
        mySwipeRefreshLayout.setOnRefreshListener(() -> {
                Load();
                }
        );
        svcdf = rootView.findViewById(R.id.svcvf);
        search = rootView.findViewById(R.id.efavsearch);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Show the ProgressDialog
        progressDialog.show();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int j=0;j<favs.getChildCount();j++){
                    try {
                        System.out.println(favs.getChildAt(j).getClass());
                        TicketView v =(TicketView)  favs.getChildAt(j);
                        if (v.getTitle().toLowerCase().contains(search.getText().toString().toLowerCase())){
                            v.setVisibility(View.VISIBLE);
                        }
                        else {
                            v.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){

                    }
                    try {
                        System.out.println(favs.getChildAt(j).getClass());
                        Docview v =(Docview)  favs.getChildAt(j);
                        if (v.getTitle().toLowerCase().contains(search.getText().toString().toLowerCase())){
                            v.setVisibility(View.VISIBLE);
                        }
                        else {
                            v.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("after");
            }
        });
        x = rootView.findViewById(R.id.xfavsearch);
        back = rootView.findViewById(R.id.backfavsearch);
        sf = rootView.findViewById(R.id.sf);
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
                svcdf.setVisibility(View.GONE);
            }
        });
        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd1.setVisibility(View.GONE);
                svcdf.setVisibility(View.VISIBLE);
            }
        });
        svcdf.setVisibility(View.GONE);

        Load();
//        bottomNavigationView.setVisibility(View.VISIBLE);

        try {
            return rootView;
        }
        catch (Exception e){
            return null;
        }

    }
    public void Load() {
//        found=true;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tickets/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        favs.removeAllViews();
        mySwipeRefreshLayout.setRefreshing(true);
        try {
        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                if (ticketinfo.getFav()){

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
                    favs.addView(ticketView);}
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                System.out.println("jfjfjfjf");
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                for (int i=0;i<favs.getChildCount();i++){
                    try {
                        System.out.println(favs.getChildAt(i).getClass());
                        TicketView v =(TicketView)  favs.getChildAt(i);
                        if (v.getImage().equals(ticketinfo.getImage())){
                            favs.removeView(v);
                        }
                    }
                    catch (Exception e){

                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("jfjfjfjf");
                Ticketinfo ticketinfo = dataSnapshot.getValue(Ticketinfo.class);
                for (int i=0;i<favs.getChildCount();i++){
                    try {
                        System.out.println(favs.getChildAt(i).getClass());
                        TicketView v =(TicketView)  favs.getChildAt(i);
                        if (v.getImage().equals(ticketinfo.getImage())){
                            favs.removeView(v);
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
        });}
        catch (Exception e){
            System.out.println(e);
        }
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {




                databaseReference = firebaseDatabase.getReference("documents/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Docinfo docinfo = dataSnapshot.getValue(Docinfo.class);
                        if (docinfo.getFav()){
                            Docview doc = new Docview(getContext(), null);
                            doc.setTitle(docinfo.getTitle());
                            doc.setImage(docinfo.getImage());
                            doc.setFav(docinfo.getFav());
                            favs.addView(doc);}
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Docinfo docinfo = dataSnapshot.getValue(Docinfo.class);
                        for (int i=0;i<favs.getChildCount();i++){
                            try {
                                System.out.println(favs.getChildAt(i).getClass());
                                Docview v =(Docview)  favs.getChildAt(i);
                                if (v.getImage().equals(docinfo.getImage())){
                                    favs.removeView(v);
                                }
                            }
                            catch (Exception e){

                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Docinfo docinfo = dataSnapshot.getValue(Docinfo.class);
                        for (int i=0;i<favs.getChildCount();i++){
                            try {
                                System.out.println(favs.getChildAt(i).getClass());
                                Docview v =(Docview)  favs.getChildAt(i);
                                if (v.getImage().equals(docinfo.getImage())){
                                    favs.removeView(v);
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





            }
        });

        mySwipeRefreshLayout.setRefreshing(false);
        progressDialog.dismiss();

    }

}