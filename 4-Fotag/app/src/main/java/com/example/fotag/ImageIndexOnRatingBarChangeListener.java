package com.example.fotag;

import android.view.View;
import android.widget.RatingBar;

public abstract class ImageIndexOnRatingBarChangeListener implements RatingBar.OnRatingBarChangeListener {
    int imageIndex;
    public ImageIndexOnRatingBarChangeListener(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}