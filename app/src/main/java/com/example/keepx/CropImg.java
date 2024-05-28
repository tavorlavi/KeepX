package com.example.keepx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.labters.documentscanner.DocumentScannerView;

public class CropImg extends AppCompatActivity {
private DocumentScannerView dcv;
private String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_img);
        dcv = findViewById(R.id.document_scanner);
        Intent i = getIntent();
        Bitmap photo = (Bitmap) i.getExtras().get("img");
        source = i.getStringExtra("source");
        dcv.setImage(photo);
    }


    public void apply(View view) {
        Intent i = new Intent(this, Set.class);
        i.putExtra("img", dcv.getCroppedImage());
        i.putExtra("source", source);
        startActivity(i);

    }
}