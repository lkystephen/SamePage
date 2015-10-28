package com.example.projecttesting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by edmundlee on 6/13/15.
 */
public interface MainAct {
    //handle results of AsyncTask
    //for login
    public void handleLoginResults(boolean isNewUser, Users users) ;

    //returning hashmap w loc data, key is fbid
    public void handleGetFrdsLocResults(HashMap<String, OtherUser> masterListwLoc);
}
