package io.github.mikomw.coinz.db;


/**
 * A class to represent the exchange rates corresponding to gold value in a day.
 *
 * @author Songbo Hu
 */
public class ExchangeRate {

    private String Date;
    private double SHIL;
    private double DOLR;
    private double QUID;
    private double PENY;

    // Setter and getter method for private field.
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