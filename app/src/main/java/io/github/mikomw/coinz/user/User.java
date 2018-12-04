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
    private List<String> friendUIDList;
    private String lastUpdateDate;
    private int today_sale;

    public User(String uid, String nickName, String email,Double balance,List<String> friendUIDList){
        this.UID = uid;
        this.NickName = nickName;
        this.email = email;
        this.balance = balance;
        this.friendUIDList = friendUIDList;
        lastUpdateDate = Date.getDateInfo().today;
        today_sale = 0;
    }

    public User(String uid){
        this.UID = uid;
        this.NickName = uid;
        this.email = "";
        this.balance = 0.0;
        this.friendUIDList = new ArrayList<>();
        lastUpdateDate = Date.getDateInfo().today;
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
        return this.lastUpdateDate.equals(date);
    }

    public void setLastUpdateDate(String date){
        this.lastUpdateDate = date;
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
