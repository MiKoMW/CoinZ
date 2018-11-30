package io.github.mikomw.coinz.db;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.github.mikomw.coinz.db.ExchangeRate;
import io.github.mikomw.coinz.db.RateHelper;

public class rateDBOperator {
    private RateHelper dbHelper;
    private SQLiteDatabase db;

    public rateDBOperator(Context context) {
        // Thinking!!
        dbHelper = new RateHelper(context, "/data/user/0/io.github.mikomw.coinz/files/rate.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }


    public void add(ExchangeRate rate) {
        db.execSQL("insert into rateData values(?,?,?,?,?)",
                new Object[] { rate.getDate(),rate.getSHIL(),rate.getDOLR(),rate.getQUID(),rate.getPENY()});
    }

    // 修改联系人
    public void update(ExchangeRate rate) {
        db.execSQL("update rateData set SHIL=?,DOLR=?,QUID=?,PENY=? where date=?",
                new Object[] { rate.getDate(),rate.getSHIL(),rate.getDOLR(),rate.getQUID(),rate.getPENY(),rate.getDate()});
    }

    // 删除联系人
    public void delete(String date) {
        db.execSQL("delete from rateData where date=?", new String[] { date });
    }

    // 查询联系人
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

    public List<String> queryAlllRate() {
        ArrayList<String> rates = new ArrayList<String>();
        Cursor c = db.rawQuery("select date from rateData", null);
        while (c.moveToNext()) {
            rates.add(c.getString(0));
        }
        c.close();
        return rates;

    }

    // 查询所有的联系人信息
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

    // 检验用户名是否已存在
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

