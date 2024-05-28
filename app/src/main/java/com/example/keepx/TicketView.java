package com.example.keepx;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;

public class TicketView extends RelativeLayout {
    private int myear, mmonth, mday, mhour, mminute;
    private TextView title, location, date, time, locationtitle, datetitle, timetitle;
    private ImageView qr, image;
    private ContentLoadingProgressBar progressBar;
    private String image1;
    private TimePickerDialog timePickerDialog;
        private DatePickerDialog datePickerDialog;
    private CardView cardView;
    private ImageButton reminder;
    private ImageButton locationicon, fav, notfav;
    private String title1, location1, date1, time1, qrdata, uid;


    public TicketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ticketview,  this);
        title = findViewById(R.id.title);
        reminder = findViewById(R.id.reminder);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                myear = year;
                                mmonth = monthOfYear;
                                mday = dayOfMonth;
                                timePickerDialog.show();

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                mhour = hourOfDay;
                                mminute = minute;
                                Calendar cal = Calendar.getInstance();
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("title", title1);
                                if (location1!=null&&!location1.isEmpty())
                                    intent.putExtra("eventLocation", location1);
                                cal.set(myear, mmonth, mday, mhour, mminute);
                                intent.putExtra("beginTime", cal.getTimeInMillis());
                                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                                startActivity(getContext(), intent, null);
                            }
                        }, hour, minute, false);
            }
        });
        location = findViewById(R.id.location);
        cardView = findViewById(R.id.ticketviewcard);
        cardView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("Long Clicked");
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Delete Ticket");
                builder.setMessage("Are you sure you want to delete this ticket?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("tickets/"+uid+"/"+title1).removeValue();
                    Toast.makeText(getContext(), "Ticket Deleted", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.setIcon(R.drawable.baseline_delete_24);
                builder.create();
                builder.show();
                return true;
            }
        });
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        progressBar = findViewById(R.id.loadticket);
        progressBar.setVisibility(GONE);
        locationtitle = findViewById(R.id.locationtitle);
        fav = findViewById(R.id.tifav);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        fav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.setVisibility(GONE);
                notfav.setVisibility(VISIBLE);
                Ticketinfo ticketinfo = new Ticketinfo();
                ticketinfo.setTitle(title1);
                ticketinfo.setUid(uid);
                ticketinfo.setImage(image1);
                ticketinfo.setFav(false);
                if (location1!=null&&!location1.isEmpty())
                    ticketinfo.setLocation(location1);
                if (date1!=null&&!date1.isEmpty())
                    ticketinfo.setDate(date1);
                if (time1!=null&&!time1.isEmpty())
                    ticketinfo.setTime(time1);
                if (qrdata!=null&& !qrdata.isEmpty())
                    ticketinfo.setQr(qrdata);
                databaseReference.child("tickets/"+uid+"/"+title1).setValue(ticketinfo);

            }
        });
        notfav = findViewById(R.id.tinotfav);
        notfav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notfav.setVisibility(GONE);
                fav.setVisibility(VISIBLE);
                Ticketinfo ticketinfo = new Ticketinfo();
                ticketinfo.setTitle(title1);
                ticketinfo.setUid(uid);
                ticketinfo.setImage(image1);
                ticketinfo.setFav(true);
                if (location1!=null&&!location1.isEmpty()) {
                    ticketinfo.setLocation(location1);
                }
                if (date1!=null&&!date1.isEmpty()) {
                    ticketinfo.setDate(date1);
                }
                if (time1!=null&&!time1.isEmpty()) {
                    ticketinfo.setTime(time1);
                }
                if (qrdata!=null&&!qrdata.isEmpty()) {
                    ticketinfo.setQr(qrdata);
                }
                databaseReference.child("tickets/"+uid+"/"+title1).setValue(ticketinfo);
            }
        });
        datetitle = findViewById(R.id.datetitle);
        timetitle = findViewById(R.id.timetitle);
        qr = findViewById(R.id.qr);
        qr.setVisibility(GONE);
        image = findViewById(R.id.imagetct);
        locationicon = findViewById(R.id.locationicon);
        locationicon.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q="+location1));;
            context.startActivity(intent);
        });
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicketView,0,0);
        try {
            title1 = a.getString(R.styleable.TicketView_Title);
            if (title1 == null){
                title1="No Title";
            }
            title.setText(title1);
            image1 = a.getString(R.styleable.TicketView_Image);
            if (image1 != null){
                setImage(image1);
            }
            location1 = a.getString(R.styleable.TicketView_Location);
            if (location1 == null || location1.isEmpty()){
                location.setVisibility(GONE);
                locationtitle.setVisibility(GONE);
                locationicon.setVisibility(GONE);
            }
            else {
                location.setText(location1);
            }
            date1 = a.getString(R.styleable.TicketView_Date);
            if (date1 == null || date1.isEmpty()){
                date.setVisibility(GONE);
                datetitle.setVisibility(GONE);
            }
            else {
                date.setText(date1);
            }
            time1 = a.getString(R.styleable.TicketView_Time);
            if (time1 == null || time1.isEmpty()){
                time.setVisibility(GONE);
                timetitle.setVisibility(GONE);
            }
            else {
                time.setText(time1);
            }
        } finally {
            a.recycle();
        }

    }
    public void setTitle(String t){
        if (t == null){
            t = "No Title";
        }
        title1 = t;
        title.setText(title1);
    }
    public void setLocation(String l){
        if (l == null || l.isEmpty()){
            location.setVisibility(GONE);
            locationtitle.setVisibility(GONE);
            locationicon.setVisibility(GONE);
            return;
        }
        location1 = l;
        location.setText(location1);
        location.setVisibility(VISIBLE);
        locationtitle.setVisibility(VISIBLE);
        locationicon.setVisibility(VISIBLE);
    }
    public void setDate(String d){
        if (d == null || d.isEmpty()){
            date.setVisibility(GONE);
            datetitle.setVisibility(GONE);
            return;
        }
        date1 = d;
        date.setText(date1);
        date.setVisibility(VISIBLE);
        datetitle.setVisibility(VISIBLE);
    }
    public void setTime(String t){
        if (t == null || t.isEmpty()){
            time.setVisibility(GONE);
            timetitle.setVisibility(GONE);
            return;
        }
        time1 = t;
        time.setText(time1);
        time.setVisibility(VISIBLE);
        timetitle.setVisibility(VISIBLE);
    }

    public void setImage(String img) {
        if (img == null || img.isEmpty()){
            image.setVisibility(GONE);
            return;
        }
        image1 = img;
        progressBar.setVisibility(VISIBLE);
        image.setVisibility(GONE);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference with an initial file path and name
        storageRef.child("images/tickets/"+img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(getContext()).load(uri).into(image);
                    image.setVisibility(VISIBLE);
                    progressBar.setVisibility(GONE);

                }
                catch (Exception e){
                    progressBar.setVisibility(VISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void setQr(String q){
        if (q == null || q.isEmpty()){
            qr.setVisibility(GONE);
            return;
        }
        qrdata = q;
        qr.setVisibility(VISIBLE);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(q, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qr.setImageBitmap(bmp);

        } catch (Exception e) {
            e.printStackTrace();
            qr.setVisibility(View.GONE);
        }
    }
    public String getImage(){
        return this.image1;
    }
    public String getTitle(){
        return this.title1;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setFav(boolean fav){
        if (fav){
            this.fav.setVisibility(VISIBLE);
            this.notfav.setVisibility(GONE);
        }
        else {
            this.fav.setVisibility(GONE);
            this.notfav.setVisibility(VISIBLE);
        }
    }
}

