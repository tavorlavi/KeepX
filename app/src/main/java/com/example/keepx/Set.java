package com.example.keepx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.common.HybridBinarizer;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Set extends AppCompatActivity {
    private ImageView anatush, docimage;
    private int bgColor = 0xFFFFFFFF;
    private int fgColor = 0xFF000000;
    private EditText settitle, setlocation, doctitle;
    private String extxt;
    private ImageView qr;
    private CardView ticket, doc;
    private TextView setdate, settime, settext;
    private String source;
    private Button submit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String strdate, strtime;
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Intent i = getIntent();
        Bitmap photo = (Bitmap) i.getExtras().get("img");
        ticket = findViewById(R.id.ticketset);
        doc = findViewById(R.id.docset);
        submit = findViewById(R.id.setsub);
//        Bitmap photo =  Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        source = (String) i.getExtras().get("source");
        settext = findViewById(R.id.settext);
        settext.setText("Add "+source);
        if (source.equals("ticket")){
            doc.setVisibility(View.GONE);
            anatush = findViewById(R.id.imgset);
            settitle = findViewById(R.id.settitle);
            setlocation = findViewById(R.id.setlocation);
            setdate = findViewById(R.id.setdate);
            settime = findViewById(R.id.settime);
            qr = findViewById(R.id.idIVQrcode);
            System.out.println();
            anatush.setImageBitmap(photo);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Ticketinfo");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            settime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Set.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    settime.setTextSize(20);
                                    settime.setText(hourOfDay + ":" + minute);
                                    strtime = hourOfDay + ":" + minute;
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });
            setdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(Set.this, new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    setdate.setTextSize(20);
                                    setdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    strdate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });


            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(photo);
            FirebaseVision firebaseVision = FirebaseVision.getInstance();
            FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

            task.addOnSuccessListener(firebaseVisionText -> {
                //Set recognized text from image in our TextView
                extxt = firebaseVisionText.getText();
                settitle.setText(extxt);
            });
            task.addOnFailureListener(e -> {
            });
            String a = redqr(photo);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("tickets");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    String filee = ""+FirebaseAuth.getInstance().getCurrentUser().getUid()+ UUID.randomUUID().toString()+".jpg";
                    StorageReference imageref = storageRef.child("images/tickets/"+filee);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = imageref.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(Set.this,"Fail to add ticket", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                    Ticketinfo ticketinfo = new Ticketinfo();
                    String ti, loc;
                    ti = settitle.getText().toString();
                    loc = setlocation.getText().toString();
                    if (ti.isEmpty()){
                        Toast.makeText(Set.this, "please enter title", Toast.LENGTH_SHORT).show();
                        return;}
                    ticketinfo.setTitle(ti);
                    if (a != null && !a.trim().isEmpty())
                        ticketinfo.setQr(a);
                    if (loc != null && !loc.trim().isEmpty())
                        ticketinfo.setLocation(loc);
                    if (strdate != null && !strdate.trim().isEmpty())
                        ticketinfo.setDate(strdate);
                    if (strtime != null && !strtime.trim().isEmpty())
                        ticketinfo.setTime(strtime);
                    ticketinfo.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ticketinfo.setImage(filee);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // inside the method of on Data change we are setting
                            // our object class to our database reference.
                            // data base reference will sends data to firebase.
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ti).setValue(ticketinfo);
                            // after adding this data we are showing toast message.
                            Toast.makeText(Set.this, "Ticket added", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainNav.class);
                            startActivity(i);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // if the data is not added or it is cancelled then
                            // we are displaying a failure toast message.
                            Toast.makeText(Set.this, "Fail to add Ticket " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            if (a != null && !a.trim().isEmpty()){
//                setlocation.setText(a);
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(a, BarcodeFormat.QR_CODE, 512, 512);
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
            else {
                qr.setVisibility(View.GONE);
            }
        }
        else{
            ticket.setVisibility(View.GONE);
            docimage = findViewById(R.id.setidimage);
            doctitle = findViewById(R.id.setidtitle);
            docimage.setImageBitmap(photo);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (doctitle.getText().toString().isEmpty()){
                        Toast.makeText(Set.this, "please enter title", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    String filee = ""+FirebaseAuth.getInstance().getCurrentUser().getUid()+ UUID.randomUUID().toString()+".jpg";
                    databaseReference = firebaseDatabase.getReference("documents");

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    StorageReference imageref = storageRef.child("images/documents/"+filee);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = imageref.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(Set.this,"Fail to add document", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // inside the method of on Data change we are setting
                            // our object class to our database reference.
                            // data base reference will sends data to firebase.
                            Docinfo docinfo = new Docinfo();
                            docinfo.setTitle(doctitle.getText().toString());
                            docinfo.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            docinfo.setImage(filee);
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(doctitle.getText().toString()).setValue(docinfo);
                            // after adding this data we are showing toast message.
                            Toast.makeText(Set.this, "Document added", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainNav.class);
                            startActivity(i);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // if the data is not added or it is cancelled then
                            // we are displaying a failure toast message.
                            Toast.makeText(Set.this, "Fail to add Document " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }





    }
    protected String redqr(Bitmap bitmap)
    {
        if (bitmap == null)
        {
            return null;
        }
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try
        {
            Result result = reader.decode(bBitmap);
            return result.toString();
        }
        catch (NotFoundException e)
        {
            return null;
        }
    }

}