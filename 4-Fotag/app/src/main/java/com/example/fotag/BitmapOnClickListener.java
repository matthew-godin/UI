package com.example.fotag;

import android.graphics.Bitmap;
import android.view.View;

public abstract class BitmapOnClickListener implements View.OnClickListener {
    Bitmap bitmap;
    public BitmapOnClickListener(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
