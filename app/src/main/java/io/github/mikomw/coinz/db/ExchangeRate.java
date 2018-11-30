package io.github.mikomw.coinz.db;

import android.support.annotation.IdRes;

public class ExchangeRate {

    String Date;

    double SHIL;
    double DOLR;
    double QUID;
    double PENY;

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public double getSHIL() {
        return SHIL;
    }

    public void setSHIL(double SHIL) {
        this.SHIL = SHIL;
    }

    public double getDOLR() {
        return DOLR;
    }

    public void setDOLR(double DOLR) {
        this.DOLR = DOLR;
    }

    public double getQUID() {
        return QUID;
    }

    public void setQUID(double QUID) {
        this.QUID = QUID;
    }


    public double getPENY() {
        return PENY;
    }

    public void setPENY(double PENY) {
        this.PENY = PENY;
    }


}
