package com.example.keepx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

public class MainNav extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{

private final static int pic_id = 1888;
private String source;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemActiveIndicatorColor(new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#B382DEFF")}));

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.fav);

    }
    IdFrag firstFragment = new IdFrag();
    Ticketsfrag secondFragment = new Ticketsfrag();
    Settingsfrag setfrag = new Settingsfrag();
    FavFragment favfrag = new FavFragment();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId() == R.id.id){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, firstFragment)
                        .commit();
                return true;}

        else if (item.getItemId() == R.id.tick){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, secondFragment)
                        .commit();
                return true;
        } else if (item.getItemId() == R.id.set) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, setfrag)
                    .commit();
            return true;
        }
        else if (item.getItemId() == R.id.fav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, favfrag)
                    .commit();
            return true;
        }
        return false;
    }

    public void create(View view) {
        System.err.println("camera: "+(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)+" storage: "+(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            // Create an alert dialog
            System.out.println("okkkkkksdpfmo");
            System.err.println("okkkkkksdpfmo");
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            if (view.getId() == R.id.createid){
                source = "document";
                builder.setTitle("Create Document");
                builder.setMessage("Upload an existing photo or capture a new one");
                builder.setPositiveButton("Upload from gallery", (dialogInterface, i) -> {
                    Intent iGallery = new Intent();
                    iGallery.setAction(Intent.ACTION_GET_CONTENT);
                    iGallery.setType("image/*");
                    startActivityForResult(iGallery, 2000);
                });
                builder.setNegativeButton("Open camera", (dialogInterface, i) -> {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, pic_id);
                });
                builder.show();
                return;}
            if (view.getId() == R.id.createticket)
                source = "ticket";
            builder.setTitle("Create Ticket");
            builder.setMessage("Upload an existing photo or capture a new one");
            builder.setPositiveButton("Upload from gallery", (dialogInterface, i) -> {
                Intent iGallery = new Intent();
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                iGallery.setType("image/*");
                startActivityForResult(iGallery, 2000);
            });
            builder.setNegativeButton("Open camera", (dialogInterface, i) -> {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, pic_id);
            });
            builder.show();
            return;}
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            System.err.println("camera permission not granted");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
            System.err.println("camera permission not granted");
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_MEDIA_IMAGES}, 3);
        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setTitle("Permission Denied");
                    builder.setMessage("You need to allow camera permission to use this feature");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    builder.show();
                }
                return;
                case 3:
                    if (grantResults.length > 0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted. Continue the action or workflow
                        // in your app.
                    }  else {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                        builder.setTitle("Permission Denied");
                        builder.setMessage("You need to allow storage permission to use this feature");
                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });
                        builder.show();
                    }
                    return;
            case 5:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setTitle("Permission Denied");
                    builder.setMessage("You need to allow storage permission to use this feature");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    builder.show();
                }
                return;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo == null) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainNav.class);
                startActivity(i);
            }
            Intent i = new Intent(getApplicationContext(), CropImg.class);
            i.putExtra("img", photo);
            i.putExtra("source", source);
            startActivity(i);
        }
        else if (requestCode == 2000) {
            Uri uri = data.getData();
            Intent i = new Intent(getApplicationContext(), CropImg.class);
            if (uri == null) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                Intent iu = new Intent(getApplicationContext(), MainNav.class);
                startActivity(iu);
            }
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                //make bitmap smaller so it can go in the intent
                Bitmap smaller = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/6, bitmap.getHeight()/6, true);
                i.putExtra("img", smaller);
                i.putExtra("source", source);
                startActivity(i);

            } catch (IOException e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                Intent iu = new Intent(getApplicationContext(), MainNav.class);
                startActivity(iu);}
            }


        else {
        Intent i = new Intent(getApplicationContext(), MainNav.class);
        startActivity(i);}
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}