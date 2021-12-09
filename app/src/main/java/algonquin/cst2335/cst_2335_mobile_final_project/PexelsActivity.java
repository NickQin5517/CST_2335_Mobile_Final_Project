package algonquin.cst2335.cst_2335_mobile_final_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class define Pexels activity.
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class PexelsActivity extends AppCompatActivity {

    ArrayList<PhotosBean> photosBeanArrayList = new ArrayList<>();
    private MyPhotoAdapter myPhotoAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexels);

        Toolbar myToolbar = findViewById(R.id.toolbar_car);
        setSupportActionBar(myToolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_car);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu_car);
        navigationView.setNavigationItemSelectedListener((item -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }));

        AppCompatEditText editText = findViewById(R.id.edittext);

        // load SharedPreferences data
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String content = prefs.getString("content", "");
        // set content
        editText.setText(content);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myPhotoAdapter = new MyPhotoAdapter();
        recyclerView.setAdapter(myPhotoAdapter);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> { // This function executes search action
            // save enter content
            String contentStr = editText.getText().toString();

            if (TextUtils.isEmpty(contentStr)) {
                Toast.makeText(this, "content must not null", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("content", contentStr);
            editor.apply();

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Getting pexels").setMessage("loading...")
                    .setView(new ProgressBar(PexelsActivity.this))
                    .show();

            // clear old data
            photosBeanArrayList.clear();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    String stringUrl = "https://api.pexels.com/v1/search?query=" + URLEncoder.encode(contentStr, "UTF-8");
                    URL url = new URL(stringUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Authorization", "563492ad6f91700001000001c1bc0b090e26439aa9d602f335499f8d");

                    InputStream in = httpURLConnection.getInputStream();
                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray photoArray = jsonObject.getJSONArray("photos");
                    for (int i = 0; i < photoArray.length(); i++) {
                        JSONObject jsonObject1 = photoArray.getJSONObject(i);
                        int width = jsonObject1.getInt("width");
                        int height = jsonObject1.getInt("height");
                        String photographer = jsonObject1.getString("photographer");
                        JSONObject src = jsonObject1.getJSONObject("src");
                        String tiny = src.getString("tiny");
                        PhotosBean photosBean = new PhotosBean();
                        photosBean.setWidth(width);
                        photosBean.setHeight(height);
                        photosBean.setUrl(tiny);
                        photosBean.setPhotographer(photographer);
                        // add data
                        photosBeanArrayList.add(photosBean);
                    }
                    runOnUiThread(() -> {
                        myPhotoAdapter.notifyDataSetChanged();
                        dialog.hide();
                    });
                } catch (IOException | JSONException ioe) {

                }
            });
            // send request
            // request(contentStr);
        });
    }

    /**
     * this method is used to create a menu on the screen
     * @param menu  menu object that passed in
     * @return   true if the menu successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pexel_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is used to navigate other applications which user click the corresponding menu
     * @param item different menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.car_to_owlbot:
                Intent car_owlbot = new Intent(this, OwlbotActivity.class);
                startActivity(car_owlbot);
                break;
            case R.id.save:
                startActivity(new Intent(this, SaveListActivity.class));
                break;

            case R.id.help_pexels:
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Tips")
                        .setMessage("Get to the corresponding image through the input text" +
                                "Author: Yingying Zhao , App name: Pexels")
                        .setNeutralButton("Got it!", (dialog, cl) -> {
                        })
                        .create()
                        .show();
                break;
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

        /**
         * This is constructor of the row
         */
        public MyRowViews(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            nameText = itemView.findViewById(R.id.textview);
            imageView.setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                PhotosBean photosBean = photosBeanArrayList.get(layoutPosition);
                Intent intent = new Intent(PexelsActivity.this, PhotoDetailActivity.class);
                intent.putExtra("photo", photosBean);
                startActivity(intent);
            });
        }
    }

    /**
     * This class defines the recyclerview adapter
     *
     * @author Yingying Zhao
     * @version 1.0
     */
    private class MyPhotoAdapter extends RecyclerView.Adapter<MyRowViews> {
        @NonNull
        @Override
        public MyRowViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View loadedRow = layoutInflater.inflate(R.layout.photo, parent, false);
            return new MyRowViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowViews holder, int position) {
            Glide.with(PexelsActivity.this).load(photosBeanArrayList.get(position).getUrl()).into(holder.imageView);
            holder.nameText.setText(photosBeanArrayList.get(position).getPhotographer());
        }

        @Override
        public int getItemCount() {
            return photosBeanArrayList.size();
        }
    }
}
