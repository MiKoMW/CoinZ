package io.github.mikomw.coinz.coin;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

/**
 * This class represent the four coins in our game. Each coin consist on unique ID,
 * a double value denotes its value, its currency, the longitude latitude of its position.
 * This class is passed our activity and the remote server regularly. This class implements
 * serializable in order to make it is easier to store and pass.
 *
 * @author Songbo Hu
 */

public class Coin implements Serializable {

    private String id;
    private double value;
    private String currency;
    private double lat;
    private double lng;



    // Two constructors for the class.
    public Coin(String id, double value, String currency, LatLng latLng) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.lat = latLng.getLatitude();
        this.lng = latLng.getLongitude();
    }

    // Setter and getter method for its private fields.

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
        return id + "/" + value + "/" + currency;
    }

    // Override its equals and hashcode function.
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
