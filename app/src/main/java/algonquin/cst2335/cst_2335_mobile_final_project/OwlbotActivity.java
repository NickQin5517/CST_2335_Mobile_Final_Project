package algonquin.cst2335.cst_2335_mobile_final_project;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        Button helpbtn = findViewById(R.id.helpbutton);
        helpbtn.setOnClickListener( click -> {

            AlertDialog.Builder builder = new AlertDialog.Builder( this);
            builder.setMessage("1. Please enter the word that you want to search in the search bar.\n"+
                    "2. You can click the each words displayed in the result page to see the details including pronunciation and definition.\n "+
                    "3. You can click \"ADD\" button to add the word to favourite's word list.")
                    .setTitle("Help Instructions")
                    .setNegativeButton("CLOSE", (dislog, cl) -> {}).create().show();;


        });


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String previous = prefs.getString("searchedWord","");
        searchEdit.setText(previous);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("searchedWord", searchEdit.getText().toString());
        writer.apply();


    }
}
