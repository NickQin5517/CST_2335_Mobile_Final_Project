package algonquin.cst2335.cst_2335_mobile_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * This class defines database.
 *
 * @author Yingying Zhao
 * @version 1.0
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Messages";
    public static final String col_photo_path = "PhotoPath";
    public static final String col_photographer = "Photographer";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_photographer + " TEXT,"
                + col_photo_path + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
