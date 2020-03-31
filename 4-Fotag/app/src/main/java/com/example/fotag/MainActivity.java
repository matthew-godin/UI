package com.example.fotag;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.StrictMode;
import android.util.DisplayMetrics;
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
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {
    final String APP_NAME = "Fotag Mobile";
    final int NUM_MSTRINGS = 14;
    final String[] MSTRINGS = { "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/bunny.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/chinchilla.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/deer.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/doggo.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/ducks.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/fox.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hamster.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hedgehog.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/husky.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/kitten.png",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/loris.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/puppy.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/running.jpg",
                                "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/sleepy.png" };
    Model model;
    int appWidth, appHeight;

    protected void clearImages() {
        model.clearImages();
        loadImages(model.getImages(),
                model.getNumStars(),
                model.getRating());
    }

    protected void loadImages(ArrayList<Bitmap> images,
                              ArrayList<Integer> stars,
                              int rating) {
        GridLayout gridLayout = ((GridLayout)findViewById(R.id.MainGridLayout));
        gridLayout.removeAllViews();
        int numImages = model.getNumVisibleImages();
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayout.setColumnCount(2);
            gridLayout.setRowCount((int)Math.ceil((numImages / 2.0)));
        } else {
            gridLayout.setColumnCount(1);
            gridLayout.setRowCount(numImages);
        }
        int actualCount = 0;
        for (int i = 0; i < images.size(); ++i) {
            if (stars.get(i) >= rating) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(images.get(i));
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.setMargins(5, 5, 5, 5);
                param.setGravity(Gravity.CENTER);
                if (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    param.columnSpec = GridLayout.spec(actualCount % 2, 1);
                    param.rowSpec = GridLayout.spec(actualCount / 2, 1);
                } else {
                    param.columnSpec = GridLayout.spec(0, 1);
                    param.rowSpec = GridLayout.spec(actualCount, 1);
                }
                imageView.setOnClickListener(new ImageIndexOnClickListener(i) {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getBaseContext(), ImageActivity.class);
                        myIntent.putExtra("ImageIndex", imageIndex);
                        startActivity(myIntent);
                    }
                });

                linearLayout.addView(imageView);
                RatingBar ratingBar = new RatingBar(this);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(1);
                ratingBar.setScaleX(0.5f);
                ratingBar.setScaleY(0.5f);
                ratingBar.setRating(stars.get(i));
                ratingBar.setOnRatingBarChangeListener(new ImageIndexOnRatingBarChangeListener(i) {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        model.updateImageRating(imageIndex,
                                (int) rating);
                        loadImages(model.getImages(),
                                model.getNumStars(),
                                model.getRating());
                    }
                });
                linearLayout.addView(ratingBar);
                linearLayout.setLayoutParams(param);
                gridLayout.addView(linearLayout);
                ++actualCount;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadImages(model.getImages(),
                model.getNumStars(),
                model.getRating());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = Model.getInstance();
        loadImages(model.getImages(),
                model.getNumStars(),
                model.getRating());
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
        ((ImageButton)findViewById(R.id.mloadButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Bitmap bmp;
                for (int i = 0; i < NUM_MSTRINGS; ++i) {
                    bmp = new LoadImageTask().doInBackground(MSTRINGS[i]);
                    if (bmp == null) {
                        ErrorLoadImageDialogFragment nextDialog = new ErrorLoadImageDialogFragment();
                        nextDialog.show(getSupportFragmentManager(), "ErrorLoadImageDialogFragment");
                    } else {
                        model.addImage(bmp, appWidth, appHeight);
                    }
                }
                loadImages(model.getImages(),
                        model.getNumStars(),
                        model.getRating());
            }
        });
        ((ImageButton)findViewById(R.id.clearButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                clearImages();
            }
        });
        ((RatingBar)findViewById(R.id.rating)).setRating(model.getRating());
        ((RatingBar)findViewById(R.id.rating)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.setRating((int)rating);
                loadImages(model.getImages(),
                        model.getNumStars(),
                        model.getRating());
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            appHeight = displayMetrics.widthPixels;
            appWidth = displayMetrics.heightPixels;
        } else {
            appHeight = displayMetrics.heightPixels;
            appWidth = displayMetrics.widthPixels;
        }
    }
}
