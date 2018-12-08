package io.github.mikomw.coinz.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import io.github.mikomw.coinz.coin.Coin;

import static org.junit.Assert.*;

/**
 * This is a unit test tests if we could write and read serializable object into our local file.
 *
 * @author Songbo Hu
 */

public class SerializableManagerTest {
    private Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext  = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void readSerializable() {

        ArrayList<Coin> temp = new ArrayList<Coin>();
        Coin coin1 = new Coin("1",10.0,"GBP",new LatLng());
        temp.add(coin1);
        SerializableManager.saveSerializable(appContext,temp,"test.data");
        ArrayList<Coin> ans = SerializableManager.readSerializable(appContext,"test.data");
        assertEquals(ans.size(),1);
        assertEquals(coin1,temp.get(0));
    }

    @Test
    public void removeSerializable() {
        SerializableManager.removeSerializable(appContext,"test.data");
        ArrayList<Coin> ans = SerializableManager.readSerializable(appContext,"test.data");
        assertEquals(ans,null);
    }
}
