package com.example.fotag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    protected Bitmap doInBackground(String... urls) {
        try {
            return BitmapFactory.decodeStream((new URL(urls[0])).openConnection().getInputStream());
        } catch (Exception e) {
            Log.w("myApp", "OKOKOKO" + e.toString());
        }
        return null;
    }
}
