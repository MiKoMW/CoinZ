package io.github.mikomw.coinz.coin;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * This is a unit test which tests if the coins in our app can be distinguished properly.
 *
 * @author Songbo Hu
 */

public class CoinTest {

    @Test
    public void equalsTest(){
        Coin coin1 = new Coin("1",10.0,"GBP",new LatLng());
        Coin coin2 = new Coin("2",10.2,"GBP",new LatLng());
        Coin coin3 = new Coin("2",10.3,"CNY",new LatLng());
        Coin coin4 = new Coin("2",10.2,"GBP",new LatLng());

        assertNotEquals(coin1,coin2);
        assertEquals(coin3,coin2);
        assertEquals(coin3,coin4);
    }

    @Test
    public void hashTest(){
        Coin coin1 = new Coin("1",10.0,"GBP",new LatLng());
        Coin coin2 = new Coin("2",10.2,"GBP",new LatLng());
        Coin coin3 = new Coin("2",10.3,"CNY",new LatLng());
        Coin coin4 = new Coin("2",10.2,"GBP",new LatLng());

        HashSet<Coin> temp = new HashSet<>();
        temp.add(coin1);
        temp.add(coin2);
        temp.add(coin3);
        temp.add(coin4);
        assertEquals(temp.size(),2);
    }

}