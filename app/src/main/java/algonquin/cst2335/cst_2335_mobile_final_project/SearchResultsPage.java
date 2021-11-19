package algonquin.cst2335.cst_2335_mobile_final_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchResultsPage extends AppCompatActivity {

    WordListFragment wordFragment;
    FragmentTransaction tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        Intent fromPrevious = getIntent();
        String searchContent = fromPrevious.getStringExtra("searchEdit2");

        Bundle bundle = new Bundle();
        bundle.putString("searchEdit2", searchContent);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("searchedWord",searchContent);
        writer.apply();

        wordFragment = new WordListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        tx = fMgr.beginTransaction();
        wordFragment.setArguments(bundle);
        tx.add(R.id.fragmentWord, wordFragment);
        tx.commit();

    }

    public void userClickedMessage(WordListFragment.Word word, int position) {

        WordDetailsFragment wdFragment = new WordDetailsFragment(word, position);

        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentWord,wdFragment);
        tx.addToBackStack(null);
        tx.commit();

    }

    public void notifyWordAdded(WordListFragment.Word chosenWord, int chosenPosition) {

        wordFragment.notifyWordAdded(chosenWord, chosenPosition);
        tx.remove(wordFragment);

    }
}
