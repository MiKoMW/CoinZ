package io.github.mikomw.coinz.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a unit test tests if we could operate on shared preference properly.
 *
 * @author Songbo Hu
 */

public class SharedPreferencesUtilTest {
    private Context appContext;
    @Before
    public void setUp() throws Exception {
        appContext  = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void getBoolean() {
        assertTrue(SharedPreferencesUtil.getBoolean(appContext,"test_bool",true));
    }

    @Test
    public void putBoolean() {
        SharedPreferencesUtil.putBoolean(appContext,"test_bool",false);
        assertTrue(!SharedPreferencesUtil.getBoolean(appContext,"test_bool",true));
        SharedPreferencesUtil.putBoolean(appContext,"test_bool",true);
        assertTrue(SharedPreferencesUtil.getBoolean(appContext,"test_bool",true));
    }
}