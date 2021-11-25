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

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        switch (item.getItemId()) {

            case R.id.owlbotIcon:

                startActivity( new Intent( MainActivity.this, OwlbotActivity.class));

                break;
        }


        return super.onOptionsItemSelected(item);
    }

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


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener( (item)-> {

            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);

            return false;

        });

    }
}