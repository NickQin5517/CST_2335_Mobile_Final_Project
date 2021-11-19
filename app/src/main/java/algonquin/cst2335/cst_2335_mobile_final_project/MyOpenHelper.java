package algonquin.cst2335.cst_2335_mobile_final_project;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "MyWordDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "WordsList";
    public static final String col_id = "_id";
    public static final String col_word_name = "WordName";
    public static final String col_pronunciation = "Pronunciation";
    public static final String col_definition = "Definition";



    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( String.format( " Create table %s ( %s INTEGER PRiMARY KEY AUTOINCREMENT, "
                + " %s TEXT, "
                + " %s TEXT, %s TEXT );", TABLE_NAME, col_id, col_word_name, col_pronunciation, col_definition));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(db);

    }
}
