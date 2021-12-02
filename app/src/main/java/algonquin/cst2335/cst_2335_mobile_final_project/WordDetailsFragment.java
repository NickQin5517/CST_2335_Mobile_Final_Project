package algonquin.cst2335.cst_2335_mobile_final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * This class extends Fragment implement details of every fragment and use details_layout to display
 * single WordDetailsFragment object's arrangement
 */
public class WordDetailsFragment extends Fragment {

   WordListFragment.Word chosenWord;
   int chosenPosition;

   /**
    * a two parameter constructor of type WordDetailsFragment
    * @param word
    * @param position
    */
   public WordDetailsFragment(WordListFragment.Word word, int position){
      chosenWord = word;
      chosenPosition = position;

   }


   @Override
   public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

      View detailsView = inflater.inflate(R.layout.details_layout, container, false);

      TextView wordNameView = detailsView.findViewById(R.id.detailsWordName);
      TextView wordProView = detailsView.findViewById(R.id.detailsWordPro);
      TextView wordDefinitionView = detailsView.findViewById(R.id.detailsWordDefinition);

      wordNameView.setText("Word Name: " + chosenWord.getWordName());
      wordProView.setText("Pronunciation: " + chosenWord.getPronunciationview());
      wordDefinitionView.setText("Definition: " + chosenWord.getWordDefinition());

      Button closeButton = detailsView.findViewById(R.id.closebtn);
      closeButton.setOnClickListener( closeClicked -> {
         getParentFragmentManager().beginTransaction().remove(this).commit();
      });

      Button addButton = detailsView.findViewById(R.id.addbtn);
      addButton.setOnClickListener( addClicked -> {

         SearchResultsPage parentActivity = (SearchResultsPage) getContext();
         parentActivity.notifyWordAdded(chosenWord,chosenPosition);
         getParentFragmentManager().beginTransaction().remove(this).commit();


      });

      return detailsView;
   }
}
