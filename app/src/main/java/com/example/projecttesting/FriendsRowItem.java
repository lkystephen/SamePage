package com.example.projecttesting;

public class FriendsRowItem {

    private String friendsName, fbid, distance, time;
    private int last_online;
    private int friends_status;
    private int friends_selection, friends_image;


    public FriendsRowItem(String friendsName, int last_online, String fbid, String distance, String time) {

        this.friendsName = friendsName;
        this.last_online = last_online;
        this.fbid = fbid;
        this.distance = distance;
        this.time = time;
    }

    public String getTimeDifference() {
        return time;
    }

    public String getDistance() {
        return distance;
    }

    public int getFriendsStarredStatus() {
        return friends_status;
    }

    public void setFriendsStarredStatus(int friends_status) {
        this.friends_status = friends_status;
    }

    public void setFriendsSeletionStatus(int friends_selection) {
        this.friends_selection = friends_selection;
    }

    public int getFriendsSelectionStatus() {
        return friends_selection;
    }

   /* public int getLastOnline() {
        return last_online;
    }
*/
    public void setLastOnline(int last_online) {
        this.last_online = last_online;
    }

    public String getFbId() {
        return fbid;
    }

    public String getName() {
        return friendsName;
    }

    public void setName(String friendsName) {
        this.friendsName = friendsName;
    }

}