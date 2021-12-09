package algonquin.cst2335.cst_2335_mobile_final_project;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the Activity that show the pexels detail
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class PhotoDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        PhotosBean photo = (PhotosBean) getIntent().getSerializableExtra("photo");
        PhotoDetailFragment photoDetailFragment = new PhotoDetailFragment(photo);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, photoDetailFragment)
                .commit();
    }
}
