package io.github.mikomw.coinz.db;

import android.support.annotation.IdRes;

public class ExchangeRate {

    String Date;

    float SHIL;
    float DOLR;
    float QUID;
    float PENY;

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public float getSHIL() {
        return SHIL;
    }

    public void setSHIL(float SHIL) {
        this.SHIL = SHIL;
    }

    public float getDOLR() {
        return DOLR;
    }

    public void setDOLR(float DOLR) {
        this.DOLR = DOLR;
    }

    public float getQUID() {
        return QUID;
    }

    public void setQUID(float QUID) {
        this.QUID = QUID;
    }


    public float getPENY() {
        return PENY;
    }

    public void setPENY(float PENY) {
        this.PENY = PENY;
    }


}
