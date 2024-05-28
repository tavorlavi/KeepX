package com.example.keepx;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IdFrag extends Fragment {

    private EditText search;
    private ImageButton x, back;
    private CardView cd1, svcdid;
    private View rootView;
    private ImageButton sid;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private LinearLayout docs;
    public IdFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_id, container, false);
        cd1 = rootView.findViewById(R.id.idtop);
        svcdid = rootView.findViewById(R.id.svcvid);
        search = rootView.findViewById(R.id.edocsearch);
        x = rootView.findViewById(R.id.xdocsearch);
        back = rootView.findViewById(R.id.backdocsearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (int j = 0; j < docs.getChildCount(); j++) {
                    Docview docview = (Docview) docs.getChildAt(j);
                    if (docview.getTitle().toLowerCase().contains(search.getText().toString().toLowerCase())) {
                        docview.setVisibility(View.VISIBLE);
                    } else {
                        docview.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
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
                svcdid.setVisibility(View.GONE);
                cd1.setVisibility(View.VISIBLE);
            }
        });
        sid = rootView.findViewById(R.id.sid);
        docs = rootView.findViewById(R.id.docslayout);
        mySwipeRefreshLayout = rootView.findViewById(R.id.refdoc);
        mySwipeRefreshLayout.setOnRefreshListener(() -> {
                LoadDocs();
                }
        );
        sid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                svcdid.setVisibility(View.VISIBLE);
                cd1.setVisibility(View.GONE);
            }
        });
        svcdid.setVisibility(View.GONE);
        LoadDocs();
        // Inflate the layout for this fragment
        return rootView;



    }
    public void LoadDocs(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("documents/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        docs.removeAllViews();
        mySwipeRefreshLayout.setRefreshing(true);
        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Docinfo docinfo = dataSnapshot.getValue(Docinfo.class);
                System.out.println(docinfo.getFav());
                Docview docview = new Docview(getContext(), null);
                docview.setTitle(docinfo.getTitle());
                docview.setImage(docinfo.getImage());
                docview.setFav(docinfo.getFav());
                docs.addView(docview);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Docinfo docinfo = dataSnapshot.getValue(Docinfo.class);
                for (int i=0;i<docs.getChildCount();i++){
                    try {
                        Docview v =(Docview)  docs.getChildAt(i);
                        if (v.getImage().equals(docinfo.getImage())){
                            docs.removeView(v);
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