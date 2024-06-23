package com.example.keepx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
private ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_img);
        dcv = findViewById(R.id.document_scanner);
        Intent i = getIntent();
        Bitmap photo = (Bitmap) i.getExtras().get("img");
        source = i.getStringExtra("source");
        dcv.setImage(photo);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_CANCELED) {
                finish();
            }
        });
    }


    public void apply(View view) {
        Intent i = new Intent(this, Set.class);
        i.putExtra("img", dcv.getCroppedImage());
        i.putExtra("source", source);
//        startActivity(i);
        resultLauncher.launch(i);

    }
}