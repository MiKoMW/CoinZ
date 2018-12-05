package io.github.mikomw.coinz.user;

import java.io.Serializable;

public class Friends implements Serializable {

    public String Nickname;
    public final String UID;

    public Friends(String uid, String nickname){
        this.Nickname = nickname;
        this.UID = uid;
    }

}
