package com.example.fotag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.UiThread;
import androidx.fragment.app.DialogFragment;

import java.net.URL;

public class LoadImageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.LoadImageURLStyle);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View loadImageDialogView = inflater.inflate(R.layout.load_image, null);
        builder.setView(loadImageDialogView)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Bitmap bmp = new LoadImageTask().doInBackground(((EditText)loadImageDialogView.findViewById(R.id.loadImageURL)).getText().toString());
                            ((MainActivity)getActivity()).model.addImage(bmp,
                                    ((MainActivity)getActivity()).appWidth,
                                    ((MainActivity)getActivity()).appHeight);
                            ((MainActivity)getActivity()).loadImages(((MainActivity)getActivity()).model.getImages(),
                                    ((MainActivity)getActivity()).model.getNumStars(),
                                    ((MainActivity)getActivity()).model.getRating());
                        } catch (Exception e) {
                            Log.w("myApp", e.toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoadImageDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
