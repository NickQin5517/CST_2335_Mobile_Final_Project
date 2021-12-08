package algonquin.cst2335.cst_2335_mobile_final_project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the fragment that holds the results
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class PhotoDetailFragment extends Fragment {

    PhotosBean photosBean;

    public PhotoDetailFragment(PhotosBean photosBean) {
        this.photosBean = photosBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // The original image,and The height and width of the image and the URL of the image
        View detailsView = inflater.inflate(R.layout.details_layout, container, false);
        String original = photosBean.getUrl();
        Integer width = photosBean.getWidth();
        Integer height = photosBean.getHeight();
        ImageView imageView = detailsView.findViewById(R.id.imageview);
        imageView.setOnLongClickListener(v -> {

            MyOpenHelper opener = new MyOpenHelper(getContext());
            SQLiteDatabase db = opener.getWritableDatabase();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to save the photo?")
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                String url = photosBean.getUrl();
                                int i = url.lastIndexOf("/");
                                url = url.substring(i + 1);
                                int i1 = url.indexOf(".");
                                String iconName = url.substring(0, i1);
                                Log.e("TAG", "iconName = " + iconName);
                                try {
                                    URL imgUrl = new URL(photosBean.getUrl());
                                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, getActivity().openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                    }
                                } catch (IOException ioe) {

                                }
                                // save to database
                                ContentValues newRow = new ContentValues();
                                newRow.put(MyOpenHelper.col_photo_path, getActivity().getFilesDir() + "/" + iconName + ".png");
                                newRow.put(MyOpenHelper.col_photographer, photosBean.getPhotographer());
                                db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_photo_path, newRow);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "save success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    })
                    .create().show();
            return false;
        });
        TextView textView1 = detailsView.findViewById(R.id.textview1);
        TextView textView2 = detailsView.findViewById(R.id.textview2);
        TextView textView3 = detailsView.findViewById(R.id.textview3);
        textView3.setOnClickListener(v -> {
            Uri uri = Uri.parse(original);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        Glide.with(this).load(original).into(imageView);
        textView1.setText("width: " + width);
        textView2.setText("height: " + height);
        textView3.setText(original);
        return detailsView;
    }
}
