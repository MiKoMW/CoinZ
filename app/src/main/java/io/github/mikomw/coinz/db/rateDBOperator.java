package io.github.mikomw.coinz.db;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * A helper class to help us abstract the SQL language from our app.
 * We can operate on our DB by calling method from this operator.
 *
 * @author Songbo Hu
 */
public class rateDBOperator {
    private RateHelper dbHelper;
    private SQLiteDatabase db;

    // Constructor.
    public rateDBOperator(Context context) {
        dbHelper = new RateHelper(context, context.getFilesDir().getPath() + "/rate.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    // Add a term in our DB
    public void add(ExchangeRate rate) {
        db.execSQL("insert into rateData values(?,?,?,?,?)",
                new Object[] { rate.getDate(),rate.getSHIL(),rate.getDOLR(),rate.getQUID(),rate.getPENY()});
    }

    // update a item in our DB
    public void update(ExchangeRate rate) {
        db.execSQL("update rateData set SHIL=?,DOLR=?,QUID=?,PENY=? where date=?",
                new Object[] { rate.getDate(),rate.getSHIL(),rate.getDOLR(),rate.getQUID(),rate.getPENY(),rate.getDate()});
    }

    // delete an item in our DB
    public void delete(String date) {
        db.execSQL("delete from rateData where date=?", new String[] { date });
    }

    // Query one day exchange rate
    public ExchangeRate queryOne(String date) {
        ExchangeRate rate = new ExchangeRate();
        Cursor c = db.rawQuery("select * from rateData where date= ?", new String[] { date });

        while (c.moveToNext()) {
            rate.setDate(c.getString(0));
            rate.setSHIL(c.getDouble(1));
            rate.setDOLR(c.getDouble(2));
            rate.setQUID(c.getDouble(3));
            rate.setPENY(c.getDouble(4));
        }

        c.close();
        return rate;
    }

    // Return all exchange dates which are stored in our DB
    public List<String> queryAlllRate() {
        ArrayList<String> rates = new ArrayList<String>();
        Cursor c = db.rawQuery("select date from rateData", null);
        while (c.moveToNext()) {
            rates.add(c.getString(0));
        }
        c.close();
        return rates;

    }

    // Query all the exchange rate in our DB.
    public List<ExchangeRate> queryMany() {
        ArrayList<ExchangeRate> rates = new ArrayList<ExchangeRate>();
        Cursor c = db.rawQuery("select * from rateData", null);
        while (c.moveToNext()) {
            ExchangeRate rate = new ExchangeRate();
            rate.setDate(c.getString(0));
            rate.setSHIL(c.getDouble(1));
            rate.setDOLR(c.getDouble(2));
            rate.setQUID(c.getDouble(3));
            rate.setPENY(c.getDouble(4));
            rates.add(rate);
        }
        c.close();
        return rates;
    }

    // Check if a date is already in our DB
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from rateData where date =?";
        Cursor cursor = db.rawQuery(Query, new String[] { value });
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}

