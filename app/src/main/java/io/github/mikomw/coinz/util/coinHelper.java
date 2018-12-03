package io.github.mikomw.coinz.util;

import java.util.ArrayList;

import io.github.mikomw.coinz.coin.Coin;

public class coinHelper {

    public static double sumCoinValue(ArrayList<Coin> coins){

        double ans = 0;

        for(Coin coin : coins){

            ans+=coin.getValue();

        }

        return ans;


    }

}
