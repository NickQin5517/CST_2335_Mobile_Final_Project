package algonquin.cst2335.cst_2335_mobile_final_project;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

/**
 * This class is main class that provide user entries of four applications to choice.
 *
 */

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pexelsBtn = findViewById(R.id.Pexels);
        pexelsBtn.setOnClickListener( click -> startActivity( new Intent( MainActivity.this, PexelsActivity.class)));

        Button owlbotBtn = findViewById(R.id.Owlbot);
        owlbotBtn.setOnClickListener( click -> startActivity( new Intent( MainActivity.this, OwlbotActivity.class)));

        Button covidtBtn = findViewById(R.id.Covid);
        covidtBtn.setOnClickListener( click -> startActivity( new Intent( MainActivity.this, CovidActivity.class)));

        Button carbonBtn = findViewById(R.id.Carbon);
        carbonBtn.setOnClickListener( click -> startActivity( new Intent( MainActivity.this, CarbonActivity.class)));




    }
}