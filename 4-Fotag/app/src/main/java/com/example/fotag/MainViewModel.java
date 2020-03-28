package com.example.fotag;

import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private ArrayList<Bitmap> images;

    public MainViewModel() {
        images = new ArrayList<Bitmap>();
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void addImage(Bitmap image) {
        images.add(image);
    }

    public void clearImages() {
        images.clear();
    }
}
