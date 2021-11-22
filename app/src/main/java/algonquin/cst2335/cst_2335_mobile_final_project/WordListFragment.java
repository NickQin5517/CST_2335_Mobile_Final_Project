package algonquin.cst2335.cst_2335_mobile_final_project;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class WordListFragment extends Fragment {

    RecyclerView wordList;
    ArrayList<Word> resultsWords = new ArrayList<>();

    MyChatAdapter theAdapter = new MyChatAdapter();

    Word thisWord ;
    String definition;
    String word;
    String pronunciation;
    SQLiteDatabase db;
    Button searchbtn;
    int count = 1;


    private String stringURL;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View wordLayout = inflater.inflate(R.layout.search_results_layout, container, false);

        wordList = wordLayout.findViewById(R.id.myrecycler);
        wordList.setAdapter(new MyChatAdapter());

        String searchedContent = getArguments().getString("searchEdit2");
        EditText searchtext = wordLayout.findViewById(R.id.searchedittwo);
        searchtext.setText(searchedContent);
        String searchWord = searchtext.getText().toString();

        MyOpenHelper opener = new MyOpenHelper( getContext() );
        db = opener.getWritableDatabase();
//        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

        getWordFromServer(searchWord);

        searchbtn = wordLayout.findViewById(R.id.searchbutton);
        searchbtn.setOnClickListener(click -> {

            String searchWord2 = searchtext.getText().toString();
            getWordFromServer(searchWord2);
        });

        return wordLayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getWordFromServer(String searchtext){

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute( () -> {

            try {
   //             String searchWord = searchtext.getText().toString();

                stringURL = "https://owlbot.info/api/v4/dictionary/"
                        + URLEncoder.encode(searchtext, "UTF-8");

                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Token " + "cf1d44815d8d3eb21e689cd11783c73652a91af7" );
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                String text = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                JSONObject theDocument = new JSONObject( text );
                JSONArray definitionsArray = theDocument.getJSONArray("definitions");
  //              JSONObject position0 = definitionsArray.getJSONObject(0);
  //              definition = position0.getString("definition");
                word = theDocument.getString("word");
                pronunciation = theDocument.getString("pronunciation");

                getActivity().runOnUiThread( (  )  -> {

                    wordList.setAdapter(theAdapter);
                    wordList.setLayoutManager(new LinearLayoutManager(getContext()));
                    resultsWords.clear();
                    for(int i = 0; i<definitionsArray.length(); i++){

                        try {
                            JSONObject position0 = definitionsArray.getJSONObject(i);
                            definition = position0.getString("definition");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        thisWord = new Word(word,"//" + pronunciation + "//",definition);
                        resultsWords.add(thisWord);

                    }

 //                   resultsWords.remove(thisWord);
 //                   thisWord = new Word(word,"//" + pronunciation + "//",definition);
//                    resultsWords.add(thisWord);

                    theAdapter.notifyItemInserted( resultsWords.size()-1 );


                });;

            } catch (Exception e) {
                e.printStackTrace();
            }


        });

    }

    public void notifyWordAdded(Word chosenWord, int chosenPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
        builder.setMessage("Do you want to add the chosen word to your favourite word list: " + chosenWord.getWordName())
                .setTitle("Question: ")
                .setNegativeButton("No", (dislog, cl) -> {})
                .setPositiveButton("Yes", (dislog, cl) -> {

                    Word addedWord = resultsWords.get(chosenPosition);

                    ContentValues newRow = new ContentValues();
                    newRow.put(MyOpenHelper.col_word_name, thisWord.getWordName());
                    newRow.put(MyOpenHelper.col_pronunciation, thisWord.getPronunciationview());
                    newRow.put(MyOpenHelper.col_definition, thisWord.getWordDefinition());
                    long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_word_name, newRow);
                    thisWord.setId(newId+1);

                    theAdapter.notifyItemRemoved(chosenPosition);

                    System.out.println(addedWord.getId());

//                    db.execSQL(String.format( "Insert into %s values( \"%d\", \"%s\", \"%s\", \"%s\" );",
//                            MyOpenHelper.TABLE_NAME,addedWord.getId(), addedWord.getWordName(),
//                            addedWord.getPronunciationview(), addedWord.getWordDefinition()));


                    Snackbar.make(searchbtn, "You added word " + addedWord.getWordName(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {

                                theAdapter.notifyItemRemoved(chosenPosition);
                                System.out.println(addedWord.getId());
                                db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(addedWord.getId()-1)});

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

            itemView.setOnClickListener( click -> {

                SearchResultsPage parentActivity = (SearchResultsPage) getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(resultsWords.get(position), position);

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
        public void onBindViewHolder( MyRowViews holder, int position) {

            holder.wordNameText.setText(resultsWords.get(position).getWordName());
            holder.pronunciationText.setText(resultsWords.get(position).getPronunciationview());
            holder.wordDefinitionText.setText(resultsWords.get(position).getWordDefinition());

        }

        @Override
        public int getItemCount() {
            return resultsWords.size();
        }

    }

     static class Word{
        String wordName;
        String wordDefinition;
        String pronunciationview;
        long id;



         public Word(String wordName, String pronunciationview, String wordDefinition) {
            this.wordName = wordName;
            this.pronunciationview = pronunciationview;
            this.wordDefinition = wordDefinition;
        }

         public Word(String wordName, String pronunciationview, String wordDefinition,long id) {
             this.wordName = wordName;
             this.pronunciationview = pronunciationview;
             this.wordDefinition = wordDefinition;
             setId(id);
         }

         public void setId( long l) {
            id = l; };

         public long getId() {
             return id;
         }

        public String getWordName() {
            return wordName;
        }

        public String getPronunciationview() {
            return pronunciationview ;
        }

        public String getWordDefinition() {
            return wordDefinition;
        }

    }

}
