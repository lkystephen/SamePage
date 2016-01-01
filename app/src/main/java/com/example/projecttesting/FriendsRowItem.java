package com.example.projecttesting;

public class FriendsRowItem {

    private String friendsName, fbid, distance;
    private long time;
    private int last_online;
    private int friends_status;
    private int friends_selection, friends_image;


    public FriendsRowItem(String friendsName, int last_online, String fbid, String distance, long updateTime) {

        this.friendsName = friendsName;
        this.last_online = last_online;
        this.fbid = fbid;
        this.distance = distance;
        this.time = updateTime;
    }

    public long getUpdateTime() {
        return time;
    }

    public String getDistance() {
        return distance;
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