package io.github.mikomw.coinz.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a unit test which tests the date we have for the url are consistent with the date in a month.
 *
 * @author Songbo Hu
 */


public class DateTest {

    @Test
    public void getDateInfo() {
        assertEquals(Date.getDateInfo().monthURL,Date.get30DaysUrl());
        for(String st: Date.getDateInfo().month)
        {
            boolean temp = false;

            for(String temp_st :Date.get30DaysUrl())
                temp = temp || temp_st.contains(st);
            assertTrue(temp);

        }
    }
}