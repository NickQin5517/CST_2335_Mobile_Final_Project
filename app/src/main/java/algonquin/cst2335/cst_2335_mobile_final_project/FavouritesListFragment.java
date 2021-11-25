package algonquin.cst2335.cst_2335_mobile_final_project;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavouritesListFragment extends Fragment {

    ArrayList<WordListFragment.Word> favouriteWords = new ArrayList<>();
    RecyclerView wordList;

    MyChatAdapter theAdapter;
    SQLiteDatabase db;
    Button delete;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View wordLayout = inflater.inflate(R.layout.favourites_layout, container, false);

        wordList = wordLayout.findViewById(R.id.myrsecondecycler);
        theAdapter = new MyChatAdapter();
        wordList.setAdapter(theAdapter);

        wordList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));

        MyOpenHelper opener = new MyOpenHelper( getContext() );
        db = opener.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int wordCol = results.getColumnIndex( MyOpenHelper.col_word_name);
        int pronunciationCol = results.getColumnIndex( MyOpenHelper.col_pronunciation);
        int definitionCol = results.getColumnIndex( MyOpenHelper.col_definition);

        while(results.moveToNext()) {

            long id = results.getInt(_idCol);
            String word = results.getString(wordCol);
            String pronunciation = results.getString(pronunciationCol);
            String definition = results.getString(definitionCol);

            favouriteWords.add(new WordListFragment.Word(word, pronunciation, definition, id));
        }

//        thisWord = new WordListFragment.Word("sistrum", "\\sistrəm \\", "noun." + "   " +
//                "a musical instrument of ancient Egypt consisting of a metal frame with transverse metal rods which rattled when the instrument was shaken." );
//        favouriteWords.add(thisWord);

        theAdapter.notifyItemInserted( favouriteWords.size()-1 );

        return wordLayout;
    }

    public void notifyWordDeleted(WordListFragment.Word chosenWord, int chosenPosition) {


        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("Do you want to delete the chosen word from your favourite word list:" + chosenWord.getWordName())
                .setTitle("Question: ")
                .setNegativeButton("No", (dislog, cl) -> {})
                .setPositiveButton("Yes", (dislog, cl) -> {

                    WordListFragment.Word removedWord = favouriteWords.get(chosenPosition);
                    favouriteWords.remove(chosenPosition);
                    theAdapter.notifyItemRemoved(chosenPosition);

                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedWord.getId())});

                    Snackbar.make(wordList, "You deleted word " + chosenWord.getWordName(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {
                                favouriteWords.add(chosenPosition, removedWord);
                                theAdapter.notifyItemRemoved(chosenPosition);

                                db.execSQL(String.format( "Insert into %s values( \"%d\", \"%s\", \"%s\", \"%s\" );",
                                        MyOpenHelper.TABLE_NAME,removedWord.getId(), removedWord.getWordName(),
                                        removedWord.getPronunciationview(), removedWord.getWordDefinition()));

                            }).show();

                })
                .create().show();



    }

    private class MyRowViews extends RecyclerView.ViewHolder{

        TextView wordNameText;
        TextView wordDefinitionText;
        TextView pronunciationText;

        public MyRowViews( View itemView) {
            super(itemView);
            //       getAbsoluteAdapterPosition() results error
            itemView.setOnClickListener( click -> {

                FavouritesList parentActivity = (FavouritesList) getContext();
                int position = getAbsoluteAdapterPosition();

                parentActivity.userClickedWord(favouriteWords.get(position), position);

            });

            wordNameText = itemView.findViewById(R.id.wordnameview);
            pronunciationText = itemView.findViewById(R.id.pronunciationview);
            wordDefinitionText = itemView.findViewById(R.id.worddefinitionview);

        }
    }

    class MyChatAdapter extends RecyclerView.Adapter <MyRowViews>{

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.word_layout, parent, false);
            MyRowViews initRow = new MyRowViews(loadedRow);
            return initRow;
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {

            holder.wordNameText.setText(favouriteWords.get(position).getWordName());
            holder.pronunciationText.setText(favouriteWords.get(position).getPronunciationview());
            holder.wordDefinitionText.setText(favouriteWords.get(position).getWordDefinition());

        }

        @Override
        public int getItemCount() {
            return favouriteWords.size();
        }

    }
}
