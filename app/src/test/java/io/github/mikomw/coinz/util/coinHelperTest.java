package io.github.mikomw.coinz.util;

import android.app.Activity;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.mikomw.coinz.coin.Coin;

import static org.junit.Assert.*;

/**
 * This is a unit test tests the summation of the coins values working properly.
 *
 * @author Songbo Hu
 */


public class coinHelperTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void sumTest(){
        Coin coin1 = new Coin("1",10.0,"GBP",new LatLng());
        Coin coin2 = new Coin("2",10.1,"GBP",new LatLng());
        Coin coin3 = new Coin("2",11.1,"GBP",new LatLng());
        Coin coin4 = new Coin("2",16.15,"GBP",new LatLng());
        ArrayList<Coin> temp = new ArrayList<>();
        temp.add(coin1);
        temp.add(coin2);
        temp.add(coin3);
        temp.add(coin4);

        assertEquals(coinHelper.sumCoinValue(temp),47.35,0.01);



    }


}