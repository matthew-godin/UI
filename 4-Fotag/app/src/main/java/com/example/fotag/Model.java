package com.example.fotag;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import java.util.ArrayList;

class Model
{
    private static Model instance;
    static Model getInstance()
    {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    private ArrayList<Bitmap> images;
    private ArrayList<Bitmap> horizontalImages;
    private ArrayList<Bitmap> verticalImages;
    private ArrayList<Integer> stars;
    private int rating;
    Model() {
        images = new ArrayList<Bitmap>();
        stars = new ArrayList<Integer>();
        horizontalImages = new ArrayList<Bitmap>();
        verticalImages = new ArrayList<Bitmap>();
        rating = 0;
    }
    public int getNumVisibleImages() {
        int result = 0;
        for (int i = 0; i < stars.size(); ++i) {
            if (stars.get(i) >= rating) {
                ++result;
            }
        }
        return result;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public ArrayList<Bitmap> getImages() {
        return images;
    }
    public ArrayList<Integer> getNumStars() {
        return stars;
    }
    public void addImage(Bitmap image, int appWidth, int appHeight) {
        double maxWidth = appWidth * 0.8, horizontalMaxWidth = appHeight,
                horizontalMaxHeight = appWidth * 0.8,
                verticalMaxWidth = appWidth,
                verticalMaxHeight = appHeight * 0.8;
        double maxHeight = appHeight * 0.2;
        double ratio = (double)image.getWidth() / (double)image.getHeight();
        double width, height, horizontalWidth, horizontalHeight,
            verticalWidth, verticalHeight;
        if (ratio > maxWidth / maxHeight) {
            width = maxWidth;
            height = maxWidth / ratio;
        } else {
            width = maxHeight * ratio;
            height = maxHeight;
        }
        if (ratio > horizontalMaxWidth / horizontalMaxHeight) {
            horizontalWidth = horizontalMaxWidth;
            horizontalHeight = horizontalMaxWidth / ratio;
        } else {
            horizontalWidth = horizontalMaxHeight * ratio;
            horizontalHeight = horizontalMaxHeight;
        }
        if (ratio > verticalMaxWidth / verticalMaxHeight) {
            verticalWidth = verticalMaxWidth;
            verticalHeight = verticalMaxWidth / ratio;
        } else {
            verticalWidth = verticalMaxHeight * ratio;
            verticalHeight = verticalMaxHeight;
        }
        images.add(Bitmap.createScaledBitmap(image, (int)width,
                (int)height, true));
        horizontalImages.add(Bitmap.createScaledBitmap(image,
                (int)horizontalWidth, (int)horizontalHeight,true));
        verticalImages.add(Bitmap.createScaledBitmap(image,
                (int)verticalWidth, (int)verticalHeight,true));
        stars.add(0);
    }
    public void updateImageRating(int i, int rating) {
        if (i < stars.size()) {
            stars.set(i, rating);
        }
    }
    public Bitmap getVerticalImage(int i) {
        return verticalImages.get(i);
    }
    public Bitmap getHorizontalImage(int i) {
        return horizontalImages.get(i);
    }
    public void clearImages() {
        images.clear();
        stars.clear();
        horizontalImages.clear();
        verticalImages.clear();
    }
}