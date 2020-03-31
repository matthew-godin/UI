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
    Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        model = Model.getInstance();
        int imageIndex = (int)getIntent().getIntExtra("ImageIndex",0);
        ImageView imageView = findViewById(R.id.imageActivityImage);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            imageView.setImageBitmap(model.getHorizontalImage(imageIndex));
        } else {
            imageView.setImageBitmap(model.getVerticalImage(imageIndex));
        }
        ((RatingBar)findViewById(R.id.ratingImage)).setRating(model.getNumStars().get(imageIndex));
        ((RatingBar)findViewById(R.id.ratingImage)).setOnRatingBarChangeListener(new ImageIndexOnRatingBarChangeListener(imageIndex) {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.updateImageRating(imageIndex, (int)rating);
            }
        });
    }
}