package io.github.mikomw.coinz.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * A helper class for our DB operator.
 *
 * @author Songbo Hu
 */
public class RateHelper extends SQLiteOpenHelper {
    private static final String CREATE_USERDATA = "create table rateData(date varchar(20) primary key,SHIL double,DOLR double,QUID double,PENY double)";
    private Context mContext;


    public RateHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
