package algonquin.cst2335.cst_2335_mobile_final_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Activity that show the databse pexels
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class SaveListActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    // databse list
    ArrayList<PhotosBean> photosBeanArrayList = new ArrayList<>();
    private MyPhotoAdapter myPhotoAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("save list");
        myToolbar.setSubtitle("Author: Yingying Zhao Version: 1.0");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Add the default back icon
        getSupportActionBar().setHomeButtonEnabled(true); //Set the back button available


        MyOpenHelper opener = new MyOpenHelper(this);
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        while (results.moveToNext()) {
            int _idCol = results.getColumnIndex("_id");
            int photoPath = results.getColumnIndex(MyOpenHelper.col_photo_path);
            int photographer = results.getColumnIndex(MyOpenHelper.col_photographer);
            int id = results.getInt(_idCol);
            String photoPathStr = results.getString(photoPath);
            String photographerStr = results.getString(photographer);
            PhotosBean photosBean = new PhotosBean();
            photosBean.setPhotographer(photographerStr);
            photosBean.setUrl(photoPathStr);
            photosBean.setId(id);
            photosBeanArrayList.add(photosBean);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myPhotoAdapter = new MyPhotoAdapter();
        recyclerView.setAdapter(myPhotoAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toolbar back click
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This class defines the structure of the each row in RecyclerView
     *
     * @author Yingying Zhao
     * @version 1.0
     */
    private class MyRowViews extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameText;

        public MyRowViews(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            nameText = itemView.findViewById(R.id.textview);
        }
    }

    /**
     * This class defines the database adapter
     *
     * @author Yingying Zhao
     * @version 1.0
     */
    private class MyPhotoAdapter extends RecyclerView.Adapter<MyRowViews> {
        @NonNull
        @Override
        public SaveListActivity.MyRowViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View loadedRow = layoutInflater.inflate(R.layout.photo, parent, false);
            return new SaveListActivity.MyRowViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(@NonNull SaveListActivity.MyRowViews holder, int position) {
            Bitmap bitmap = BitmapFactory.decodeFile(photosBeanArrayList.get(position).getUrl());
            holder.imageView.setImageBitmap(bitmap);
            holder.nameText.setText(photosBeanArrayList.get(position).getPhotographer());
            holder.imageView.setOnClickListener(v -> {
                Log.e("TAG", "position = " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveListActivity.this);
                builder.setMessage("Do you want to delete the photo?")
                        .setTitle("Question:")
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    PhotosBean photosBean = photosBeanArrayList.get(position);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            photosBeanArrayList.remove(position);
                                            myPhotoAdapter.notifyItemRemoved(position);
                                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(photosBean.getId())});
                                        }
                                    });

                                    Snackbar.make(holder.imageView, "You delete photo #" + position, Snackbar.LENGTH_LONG)
                                            .setAction("Undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            photosBeanArrayList.add(position, photosBean);
                                                            myPhotoAdapter.notifyItemInserted(position);
                                                            Toast.makeText(SaveListActivity.this, "Undo Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    ContentValues newRow = new ContentValues();
                                                    newRow.put(MyOpenHelper.col_photo_path, photosBean.getUrl());
                                                    newRow.put(MyOpenHelper.col_photographer, photosBean.getPhotographer());
                                                    db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_photo_path, newRow);
                                                }
                                            })
                                            .show();

                                }
                            });
                        })
                        .create().show();
            });
        }

        @Override
        public int getItemCount() {
            return photosBeanArrayList.size();
        }
    }
}
