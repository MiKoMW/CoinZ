package io.github.mikomw.coinz.coin;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Coin {

    private String id;
    private float value;
    private String currency;
    private LatLng latLng;
    private boolean isFirstCollect;

    public Coin(String id, float value, String currency, LatLng latLng) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.latLng = latLng;
        this.isFirstCollect = false;
    }

    public Coin(String id, float value, String currency, LatLng latLng, boolean isFirstCollect) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.latLng = latLng;
        this.isFirstCollect = isFirstCollect;
    }

    public void setLocation(LatLng a) {
        this.latLng = a;
    }

    public String getId() {
        return id;
    }

    public Float getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public LatLng getLatLng() {
        return latLng;
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
