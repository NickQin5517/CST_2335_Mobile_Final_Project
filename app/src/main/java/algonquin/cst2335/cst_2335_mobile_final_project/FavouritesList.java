package algonquin.cst2335.cst_2335_mobile_final_project;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * This class can display the user's favourite words list
 */
public class FavouritesList extends AppCompatActivity {

    FavouritesListFragment wordFragment;
    FragmentTransaction tx;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        wordFragment = new FavouritesListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentWord, wordFragment);
        tx.commit();
    }

    /**
     * This method will be called when a word fragment was clicked
     * @param word
     * @param position
     */
    public void userClickedWord(WordListFragment.Word word, int position) {

        FavouritesWDFragment wdFragment = new FavouritesWDFragment(word, position);
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentWord,wdFragment);
        tx.addToBackStack(null);
        tx.commit();

    }

    /**
     * The method notifyWordDeleted will be called when user try to delete a word from my favourite words list
     * @param chosenWord
     * @param chosenPosition
     */
    public void notifyWordDeleted(WordListFragment.Word chosenWord, int chosenPosition) {

        wordFragment.notifyWordDeleted(chosenWord, chosenPosition);
        tx.remove(wordFragment);

    }
}
