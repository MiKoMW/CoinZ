package io.github.mikomw.coinz.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.github.mikomw.coinz.util.Date;

public class User implements Serializable {

    private final String UID;
    private String NickName;
    private String email;
    private Double balance;
    private List<Friends> friendList;
    private String lastPayUpdateDate;
    private int today_sale;
    private String lastCollectedUpdateDate;


    public User(String uid, String nickName, String email,Double balance,List<Friends> friendList){
        this.UID = uid;
        this.NickName = nickName;
        this.email = email;
        this.balance = balance;
        this.friendList = friendList;
        lastPayUpdateDate = Date.getDateInfo().today;
        lastCollectedUpdateDate = Date.getDateInfo().today;
        today_sale = 0;
    }

    public User(String uid){
        this.UID = uid;
        this.NickName = uid;
        this.email = "";
        this.balance = 0.0;
        this.friendList = new ArrayList<>();
        lastPayUpdateDate = Date.getDateInfo().today;
        lastCollectedUpdateDate = Date.getDateInfo().today;
        today_sale = 0;
    }

    public boolean ableToSale(){
        return today_sale < 25;
    }

    public void resetSale(){
        this.today_sale = 0;
    }

    public void sale_coin(){
        this.today_sale = today_sale + 1;
    }

    public int getToday_sale(){
        return today_sale;
    }

    public boolean isUpdated(String date){
        return this.lastPayUpdateDate.equals(date);
    }

    public void setLastPayUpdateDate(String date){
        this.lastPayUpdateDate = date;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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

    public String getUID(){
        return this.UID;
    }

    public void setFriendList(List<Friends> friendList) {
        this.friendList = friendList;
    }

    public List<Friends> getFriendUIDList() {
        return friendList;
    }

    public void addFriend(Friends friend){
        this.friendList.add(friend);
    }

    public void removeFriend(Friends friend){
        this.friendList.remove(friend);
    }

    public String getLastCollectedUpdateDate(){
        return this.lastCollectedUpdateDate;
    }

    public void setLastCollectedUpdateDate(String lastCollectedUpdateDate) {
        this.lastCollectedUpdateDate = lastCollectedUpdateDate;
    }
}
