package com.example.fotag;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;


import androidx.lifecycle.ViewModelProviders;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bitmap bitmap = (Bitmap)getIntent().getParcelableExtra("Bitmap");
        ImageView imageView = new ImageView(this);//findViewById(R.id.imageActivityImage);
        imageView.setImageBitmap(bitmap);
        LinearLayout layout = (LinearLayout)findViewById(R.id.ImageLinearLayout);
        TextView textView = new TextView(this);
        textView.setText("KOKOKOK");
        layout.addView(textView);
        //layout.addView(imageView);
    }
}