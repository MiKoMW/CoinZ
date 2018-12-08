package io.github.mikomw.coinz.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.mikomw.coinz.util.SerializableManager;

import static org.junit.Assert.*;
/**
 * This is a unit test tests if we could operate on our Sqlite database properly.
 * It checks the basic operations and the SQL languages in the class.
 *
 * @author Songbo Hu
 */
public class rateDBOperatorTest {
    private Context appContext;
    rateDBOperator rateDBOperator;
    @Before
    public void setUp() throws Exception {

        appContext  = InstrumentationRegistry.getTargetContext();
        rateDBOperator = new rateDBOperator(appContext);
    }

    @After
    public void tearDown() throws Exception{
        appContext.deleteDatabase("rate.db");
    }
    @Test
    public void dbTest() {

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate("20181208");
        exchangeRate.setDOLR(1.0);
        rateDBOperator.add(exchangeRate);
        assertTrue(rateDBOperator.CheckIsDataAlreadyInDBorNot("20181208"));
        exchangeRate = new ExchangeRate();
        exchangeRate.setDate("20181208");
        exchangeRate.setDOLR(1.2);
        rateDBOperator.update(exchangeRate);
        assertEquals(rateDBOperator.queryOne("20181208").getDOLR(),1.2,0.1);
        rateDBOperator.delete("20181208");
        assertFalse(rateDBOperator.CheckIsDataAlreadyInDBorNot("20181208"));
        rateDBOperator.add(exchangeRate);
        assertEquals(rateDBOperator.queryOne("20181208").getDate(),"20181208");
        assertTrue(rateDBOperator.queryAlllRate().contains("20181208"));
        assertEquals(rateDBOperator.queryAlllRate().size(),1);
        assertEquals(rateDBOperator.queryMany().size(),1);
    }
}