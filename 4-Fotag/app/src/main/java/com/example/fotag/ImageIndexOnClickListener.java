package com.example.fotag;

import android.graphics.Bitmap;
import android.view.View;

public abstract class ImageIndexOnClickListener implements View.OnClickListener {
    int imageIndex;
    public ImageIndexOnClickListener(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}