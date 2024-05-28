package com.example.keepx;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Docview extends RelativeLayout {
private TextView title;
private ImageView image;
private String title1;
private String image1;
private CardView cardView;
private ImageButton fav, notfav;
    public Docview(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.docview,  this);
        title = findViewById(R.id.doctitle);
        image = findViewById(R.id.docimage);
        cardView = findViewById(R.id.docviewcard);
        cardView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Delete Document");
                builder.setMessage("Are you sure you want to delete this document?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("documents/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+title1).removeValue();
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.setIcon(R.drawable.baseline_delete_24);
                builder.show();
                return true;
            }});
        fav = findViewById(R.id.docfav);
        notfav = findViewById(R.id.docnotfav);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        fav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.setVisibility(GONE);
                System.out.println("okkkkkk");
                notfav.setVisibility(VISIBLE);
                Docinfo docinfo = new Docinfo(image1, title1, FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                databaseReference.child("documents/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+title1).setValue(docinfo);
                System.out.println("okkkkkk");
            }
        });
        notfav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.setVisibility(VISIBLE);
                notfav.setVisibility(GONE);
                Docinfo docinfo = new Docinfo(image1, title1, FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                databaseReference.child("documents/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+title1).setValue(docinfo);
                System.out.println("okkkkkk");
            }
        });
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicketView,0,0);
        try {
            title1 = a.getString(R.styleable.Docview_TitleDoc);
            if (title1 == null){
//                title.setVisibility(GONE);
            }
            else {
                title.setText(title1);
            }
        } finally {
            a.recycle();
        }
    }
    public void setTitle(String ti){
        title1 = ti;
        title.setText(ti);
    }
    public String getTitle(){
        return title1;
    }
    public void setImage(String img){
        image1 = img;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference with an initial file path and name
        storageRef.child("images/documents/"+img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(getContext()).load(uri).into(image);
                } catch (Exception e){
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void setFav(boolean fav1){
        if (fav1){
            fav.setVisibility(VISIBLE);
            notfav.setVisibility(GONE);
        }
        else {
            fav.setVisibility(GONE);
            notfav.setVisibility(VISIBLE);
        }
    }

    public String getImage() {
        return image1;
    }
}
