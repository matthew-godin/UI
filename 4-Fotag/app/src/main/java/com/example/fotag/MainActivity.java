package com.example.fotag;

import android.app.ActionBar;
import android.content.Intent;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String APP_NAME = "Fotag Mobile";
    MainViewModel mainViewModel;

    protected void clearImages() {
        mainViewModel.clearImages();
        loadImages(mainViewModel.getImages());
    }

    protected void loadImages(ArrayList<Bitmap> images) {
        GridLayout gridLayout = ((GridLayout)findViewById(R.id.MainGridLayout));
        gridLayout.removeAllViews();
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayout.setColumnCount(2);
            gridLayout.setRowCount((int)Math.ceil((images.size() / 2.0)));
        } else {
            gridLayout.setColumnCount(1);
            gridLayout.setRowCount(images.size());
        }
        for (int i = 0; i < images.size(); ++i) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(images.get(i));
            //TextView imageView = new TextView(this);
            //imageView.setText("fdfdfdfd");
            //gridLayout.addView(imageView);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setMargins(5,5,5,5);
            param.setGravity(Gravity.CENTER);
            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                param.columnSpec = GridLayout.spec(i % 2, 1);
                param.rowSpec = GridLayout.spec(i / 2, 1);
            } else {
                param.columnSpec = GridLayout.spec(0, 1);
                param.rowSpec = GridLayout.spec(i, 1);
            }

            imageView.setOnClickListener(new BitmapOnClickListener(images.get(i))
            {
                @Override
                public void onClick(View v) {
                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        Intent myIntent = new Intent(getBaseContext(), ImageActivity.class);
                        myIntent.putExtra("Bitmap", byteArray);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        Log.w("myApp", "PLPLPLPLPLPL" + e.toString());
                    }
                }
            });
            //linearLayout.addView(imageView);
            RatingBar ratingBar = new RatingBar(this);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(1);
            ratingBar.setScaleX(0.5f);
            ratingBar.setScaleY(0.5f);
            linearLayout.addView(ratingBar);
            linearLayout.setLayoutParams(param);
            gridLayout.addView(linearLayout);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("myApp", "JIJIIJIJIJIJ");
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        loadImages(mainViewModel.getImages());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE);
        ((ImageButton)findViewById(R.id.loadButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LoadImageDialogFragment dialog = new LoadImageDialogFragment();
                dialog.show(getSupportFragmentManager(), "LoadImageDialogFragment");
            }
        });
        ((ImageButton)findViewById(R.id.clearButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clearImages();
            }
        });
    }
}
