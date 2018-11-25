package io.github.mikomw.coinz.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RateHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERDATA = "create table rateData(date varchar(20) primary key,SHIL float,DOLR float,QUID float,PENY float)";
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

