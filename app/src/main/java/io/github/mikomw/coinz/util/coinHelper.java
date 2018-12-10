package io.github.mikomw.coinz.util;

import java.util.ArrayList;

import io.github.mikomw.coinz.coin.Coin;

/**
 * A helper class help us to deal with the coin operation.
 *
 * @author Songbo Hu
 */

public class coinHelper {

    /**
     * A help function to sum all the coin values for a list of coins.
     * @param coins the list of coins we want to sum
     * @return the sum of the coin value
     */
    public static double sumCoinValue(ArrayList<Coin> coins){

        double ans = 0;

        for(Coin coin : coins){
            ans+=coin.getValue();
        }

        return ans;
    }
}