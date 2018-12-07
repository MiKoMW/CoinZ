package io.github.mikomw.coinz.user;

import java.io.Serializable;

/**
 * This class represents a friend entity which only contains the necessary information in order to
 * privacy.
 *
 * @author Songbo Hu
 */


public class Friends implements Serializable {

    public String Nickname;
    public final String UID;

    public Friends(String uid, String nickname){
        this.Nickname = nickname;
        this.UID = uid;
    }

}
