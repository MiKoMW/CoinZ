package io.github.mikomw.coinz.user;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.mikomw.coinz.util.Date;

import static org.junit.Assert.*;

/**
 * This is a unit test which tests all the major functionality of the user class.
 *
 * @author Songbo Hu
 */

public class UserTest {

    private User thisuser;
    @Before
    public void setUp() throws Exception {
        thisuser = new User("123","user","user@test.com",100.0,new ArrayList<>());
    }

    @Test
    public void ableToSale() {
        assertTrue(thisuser.ableToSale());
        for(int con = 0;con<25; con++){
            thisuser.sale_coin();
        }
        assertTrue(!thisuser.ableToSale());

    }

    @Test
    public void resetSale() {
        thisuser.resetSale();
        assertTrue(thisuser.ableToSale());

    }



    @Test
    public void getToday_sale() {
        for(int con = 0;con<12; con++){
            thisuser.sale_coin();
            assertEquals(thisuser.getToday_sale(),con+1);
        }
    }

    @Test
    public void isUpdated() {


        thisuser.setLastCollectedUpdateDate("1");
        thisuser.setLastPayUpdateDate("1");
        assertTrue(!thisuser.isUpdated(Date.getDateInfo().today));
        assertTrue(!thisuser.getLastCollectedUpdateDate().equals(Date.getDateInfo().today));

        thisuser.setLastCollectedUpdateDate(Date.getDateInfo().today);
        thisuser.setLastPayUpdateDate(Date.getDateInfo().today);
        assertTrue(thisuser.isUpdated(Date.getDateInfo().today));
        assertEquals(thisuser.getLastCollectedUpdateDate(),(Date.getDateInfo().today));

    }

    Friends afriend = new Friends("1","2");
    @Test
    public void addFriend() {
        thisuser.addFriend(afriend);
        assertEquals(thisuser.getFriendList().size(),1);
    }

    @Test
    public void removeFriend() {
        thisuser.removeFriend(afriend);
        assertEquals(thisuser.getFriendList().size(),0);

    }
}