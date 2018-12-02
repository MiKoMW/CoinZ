package io.github.mikomw.coinz.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.github.mikomw.coinz.coin.Coin;

public class User implements Serializable {

    private final String UID;
    private String NickName;
    private String email;
    private Double balance;
    private List<String> friendUIDList;

    public User(String uid, String nickName, String email,Double balance,List<String> friendUIDList){
        this.UID = uid;
        this.NickName = nickName;
        this.email = email;
        this.balance = balance;
        this.friendUIDList = friendUIDList;
    }

    public User(String uid){
        this.UID = uid;
        this.NickName = uid;
        this.email = "";
        this.balance = 0.0;
        this.friendUIDList = new ArrayList<>();
    }

    public String toString() {
        String ans = this.email + "/" + this.UID;
        return ans;
    }


    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(this == obj) {
            return true;
        }

        if(this.getClass() != obj.getClass()) {
            return false;

        }
        User user = (User) obj;
        return (this.UID.equals(user.UID));
    }



    public int hashCode () {
        return (this.UID.hashCode());
    }


    public String getNickName(){
        return this.NickName;
    }

    public void setNickName(String nickName){
        this.NickName = nickName;
    }

    public Double getBalance(){
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void addBalance(Double addbalance){
        Double ans = this.balance + addbalance;
        this.balance = ans;
    }

    public void removeBalance(Double removebalance){
        Double ans = this.balance - removebalance;
        this.balance = ans;
    }

    public void setFriendUIDList(List<String> friendUIDList) {
        this.friendUIDList = friendUIDList;
    }

    public List<String> getFriendUIDList() {
        return friendUIDList;
    }

    public void addFriend(String friend){
        this.friendUIDList.add(friend);
    }

    public void removeFriend(String friend){
        this.friendUIDList.remove(friend);
    }

}
