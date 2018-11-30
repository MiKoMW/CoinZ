package io.github.mikomw.coinz.coin;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

public class Coin implements Serializable {

    private String id;
    private double value;
    private String currency;
    private double lat;
    private double lng;
    private boolean isFirstCollect;


    public Coin(String id, double value, String currency, LatLng latLng) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.isFirstCollect = false;
        this.lat = latLng.getLatitude();
        this.lng = latLng.getLongitude();
    }

    public Coin(String id, double value, String currency, LatLng latLng, boolean isFirstCollect) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.isFirstCollect = isFirstCollect;
        this.lat = latLng.getLatitude();
        this.lng = latLng.getLongitude();
    }

    public void setLocation(LatLng a) {
        this.lat = a.getLatitude();
        this.lng = a.getLongitude();    }

    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public LatLng getLatLng() {
        return new LatLng(this.lat,this.lng);
    }

    public String toString() {
        String ans = id + "/" + value + "/" + currency;
        return ans;
    }

    public Boolean isFirstCollect(){
        return this.isFirstCollect;
    }

    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(this == obj) {
            return true;
        }

        if(this.getClass() != obj.getClass()) {
            return false;

        }
        Coin co = (Coin) obj;
        return (this.id.equals(co.id));
    }



    public int hashCode () {
        return (this.id.hashCode());
    }

}
