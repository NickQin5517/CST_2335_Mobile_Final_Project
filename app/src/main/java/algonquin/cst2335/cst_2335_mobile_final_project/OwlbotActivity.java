package algonquin.cst2335.cst_2335_mobile_final_project;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OwlbotActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owlbot_main);

        Button searchbtn = findViewById(R.id.searchbutton);
        EditText searchEdit = findViewById(R.id.searchedit);

        //       String str = searchEdit.getText().toString();
        searchbtn.setOnClickListener( click-> {
            String str;
            str = searchEdit.getText().toString();
            if(str.equals("")) {
                Toast.makeText(OwlbotActivity.this, "Please type a word !", Toast.LENGTH_LONG).show();
            }else {
                  Intent nextPage = new Intent(OwlbotActivity.this, SearchResultsPage.class);
                nextPage.putExtra("searchEdit2", searchEdit.getText().toString());

//                Intent nextPage2 = new Intent(OwlbotActivity.this, WordListFragment.class);
//                nextPage2.putExtra("searchEdit2", searchEdit.getText().toString());

                startActivity(nextPage);
            }
        });

        Button favouritesbtn = findViewById(R.id.favourbutton);
        favouritesbtn.setOnClickListener( click -> {

            Intent nextPage = new Intent(OwlbotActivity.this, FavouritesList.class);
            startActivity(nextPage);

        });


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String previous = prefs.getString("searchedWord","");
        searchEdit.setText(previous);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("searchedWord", searchEdit.getText().toString());
        writer.apply();


    }
}
