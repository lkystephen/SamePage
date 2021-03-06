package com.example.projecttesting;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by edmundlee on 6/13/15.
 */
public class User extends AsyncTask<Void, Void, Boolean> implements Users, Parcelable {
    // valuables
    public String fbid;
    public String userId;
    public String username;
    private static final String TAG = "ConnectDB";
    public String toOutput;
    private TextView welcomeText;
    public Boolean newUser;
    public List<EventTypes> eventsAttending;
    public List<EventTypes> eventsInvited;
    public List<EventTypes> eventsOrganised;
    public List<EventTypes> eventsRejected;
    public MainAct userHandler;
    public UpdateResult starHandler;
    private String regID;
    public ArrayList<OtherUser> friends;
    public ArrayList<OtherUser> stars;
    HashMap<String, OtherUser> mapOfFrds;

    //  private List<String> idsOfFriends;

    public User() {

    }

    //backdoor constructor
    public User(String password) {
        if (password.compareTo("Wessi") == 0) {
            fbid = "testing fbid";
            regID = "testing regid";
            username = "Edmund Lee, ofc";
            //userHandler = mainAct;

            //initialise
            eventsAttending = new ArrayList<EventTypes>();
            eventsInvited = new ArrayList<EventTypes>();
            eventsOrganised = new ArrayList<EventTypes>();
            eventsRejected = new ArrayList<EventTypes>();
            friends = new ArrayList<OtherUser>();
            eventsAttending.add(new EventType("Wessi"));
            OtherUser json_tmp = new OtherUser("fbid", null, "username");
            friends.add(json_tmp);
        }
    }

    // constructor
    public User(String userid, MainAct mainAct) {
        fbid = userid;
        //username = username_input;

        //initialise
        eventsAttending = new ArrayList<EventTypes>();
        eventsInvited = new ArrayList<EventTypes>();
        eventsOrganised = new ArrayList<EventTypes>();
        eventsRejected = new ArrayList<EventTypes>();
    }

    //constructor for parcel
    public User(Parcel in) {
        //get all string
        String[] parcelData = new String[4];
        in.readStringArray(parcelData);
        fbid = parcelData[0];
        userId = parcelData[1];
        username = parcelData[2];
        regID = parcelData[3];

        //initialise lists
        eventsAttending = new ArrayList<EventTypes>();
        eventsInvited = new ArrayList<EventTypes>();
        eventsOrganised = new ArrayList<EventTypes>();
        eventsRejected = new ArrayList<EventTypes>();
        friends = new ArrayList<OtherUser>();
        stars = new ArrayList<OtherUser>();
        in.readList(eventsAttending, EventType.class.getClassLoader());
        in.readList(eventsInvited, EventType.class.getClassLoader());
        in.readList(eventsOrganised, EventType.class.getClassLoader());
        in.readList(eventsRejected, EventType.class.getClassLoader());
        in.readList(friends, OtherUser.class.getClassLoader());
        in.readList(stars, OtherUser.class.getClassLoader());
        //public Boolean newUser;
    }


    public User(String uid, String inputUsername, String regId, MainAct mainAct) {
        fbid = uid;
        regID = regId;
        username = inputUsername;
        userHandler = mainAct;
        Log.i(TAG, userHandler.getClass().toString());

        //initialise
        eventsAttending = new ArrayList<EventTypes>();
        eventsInvited = new ArrayList<EventTypes>();
        eventsOrganised = new ArrayList<EventTypes>();
        eventsRejected = new ArrayList<EventTypes>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //get regid
        String link = "http://letshangout.netau.net/main.php";
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            JSONObject json_toSend = new JSONObject();
            json_toSend.put("fb_id", fbid);
            json_toSend.put("username", username);
            json_toSend.put("regID", regID);

            //send the POST out
            //sending out the POST
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(json_toSend.toString());
            Log.i("json", json_toSend.toString());
            out.flush();
            out.close();

            // read
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.i("jsonFrmServer", sb.toString());

            JSONObject json_fromServer = new JSONObject(sb.toString());
            userId = json_fromServer.getString("userid");
            String isNewUser = json_fromServer.getString("isNew");
            // for first time user
            if (isNewUser.equals("true")) {
                newUser = true;
            }
            // for existing users
            else {
                newUser = false;
                //get events
                //events pending
                if (json_fromServer.isNull("0")) {
                    Log.i("json", "no events pending");
                } else {
                    JSONArray json_events_invited = json_fromServer.getJSONArray("0");
                    // convert events to arraylist
                    int arrSize = json_events_invited.length();
                    for (int i = 0; i < arrSize; ++i) {
                        //get event ids
                        JSONObject json_event_tmp = json_events_invited.getJSONObject(i);
                        //create events
                        EventType event_tmp = new EventType(json_event_tmp);
                        //to set parameters for event
                        // To do..
                        eventsInvited.add(event_tmp);
                    }
                    //sort eventsInvited
                    Collections.sort(eventsInvited);

                    int arr_test = eventsInvited.size();
                    for (int j = 0; j < arr_test; ++j) {
                        Log.i("events test in User", eventsInvited.get(j).toString());
                    }
                }

                //events organised
                if (json_fromServer.isNull("3")) {
                    Log.i("json", "no events organised");
                } else {
                    JSONArray json_events_organised = json_fromServer.getJSONArray("3");
                    int arrSize_org = json_events_organised.length();
                    for (int i = 0; i < arrSize_org; ++i) {
                        //get event ids
                        JSONObject json_event_tmp = json_events_organised.getJSONObject(i);
                        //create events
                        EventType event_tmp = new EventType(json_event_tmp);
                        //to set parameters for event
                        eventsOrganised.add(event_tmp);
                    }
                    //sort
                    Collections.sort(eventsOrganised);
                }
                //for events attending
                if (json_fromServer.isNull("1")) {
                    Log.i("json", "no events attending");
                } else {
                    JSONArray json_events_attending = json_fromServer.getJSONArray("1");
                    int arrSize_a = json_events_attending.length();
                    for (int i = 0; i < arrSize_a; ++i) {
                        //get event ids
                        JSONObject json_event_tmp = json_events_attending.getJSONObject(i);
                        //create events
                        EventType event_tmp = new EventType(json_event_tmp);
                        //to set parameters for event
                        eventsAttending.add(event_tmp);
                    }
                    //sort
                    Collections.sort(eventsAttending);
                }

                //events rejected
                if (json_fromServer.isNull("2")) {
                    Log.i("json", "no events rejceted");
                } else {
                    JSONArray json_events_rejected = json_fromServer.getJSONArray("2");
                    int arrSize_r = json_events_rejected.length();
                    for (int i = 0; i < arrSize_r; ++i) {
                        //get event ids
                        JSONObject json_event_tmp = json_events_rejected.getJSONObject(i);
                        //create events
                        EventType event_tmp = new EventType(json_event_tmp);
                        //to set parameters for event
                        eventsRejected.add(event_tmp);
                    }
                    //sort
                    Collections.sort(eventsRejected);
                }
            }
            Log.i(TAG, "step 2");
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        } finally {
            urlConnection.disconnect();
        }
        //get friends - testing
        Bundle param = new Bundle();
        param.putString("fields", "id,name");
        param.putInt("limit", 3000);
        // var to store friends
        friends = new ArrayList<OtherUser>();
        stars = new ArrayList<OtherUser>();
        //setup a general callback for each graph request sent, this callback will launch the next request if exists.
        final GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    JSONArray json_friends_raw = response.getJSONObject().getJSONArray("data");
                    for (int i = 0; i < json_friends_raw.length(); i++) {
                        JSONObject json_friend = new JSONObject();
                        String fbid_tmp = json_friends_raw.getJSONObject(i).getString("id");
                        String username_tmp = json_friends_raw.getJSONObject(i).getString("name");
                        // get loc as well
                        Log.i("User", "getting friend" + Integer.toString(i) + "'s location");

                        String link = "http://letshangout.netau.net/getfrdsloc.php";
                        HttpURLConnection urlConnection = null;

                        try {
                            URL url = new URL(link);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setDoOutput(true);
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setRequestProperty("Content-Type", "application/json");
                            urlConnection.setRequestProperty("Accept", "application/json");

                            JSONObject json_toSend = new JSONObject();
                            json_toSend.put("fbid", fbid_tmp);
                            json_toSend.put("uid", userId);
                            //send the POST out
                            //sending out the POST
                            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                            out.print(json_toSend.toString());
                            out.flush();
                            out.close();

                            // read
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            // Read Server Response
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                                break;
                            }
                            JSONObject json_fromServer = new JSONObject(sb.toString());
                            Log.i("getting star frds", json_fromServer.toString());
                            if (json_fromServer.getBoolean("hasLoc")) {
                                double lat_tmp = json_fromServer.getDouble("lat");
                                double long_tmp = json_fromServer.getDouble("longitude");
                                double lat_o_tmp = json_fromServer.getDouble("lat_old");
                                double long_o_tmp = json_fromServer.getDouble("long_old");
                                String uid_tmp = json_fromServer.getString("userid");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1_tmp = sdf.parse(json_fromServer.getString("timestamp"));
                                Date date2_tmp = sdf.parse(json_fromServer.getString("timestamp_old"));
                                long timestamp_tmp = date1_tmp.getTime();
                                long timestamp_o_tmp = date2_tmp.getTime();
                                Log.i("did i get frds", fbid_tmp);
                                OtherUser otherUser_tmp = new OtherUser(fbid_tmp, uid_tmp, username_tmp, lat_tmp, long_tmp, lat_o_tmp, long_o_tmp, timestamp_tmp, timestamp_o_tmp);
                                friends.add(otherUser_tmp);
                                if (json_fromServer.getBoolean("isStar")) {
                                    stars.add(otherUser_tmp);
                                    Log.i("getting star frds", otherUser_tmp.toString());
                                }
                            } else {
                                String uid_tmp = json_fromServer.getString("userid");
                                OtherUser otherUser_tmp = new OtherUser(fbid_tmp, uid_tmp, username_tmp);
                                friends.add(otherUser_tmp);
                                if (json_fromServer.getBoolean("isStar")) {
                                    stars.add(otherUser_tmp);
                                    Log.i("getting star frds", otherUser_tmp.toString());

                                }
                            }
                        } catch (Exception ex) {
                            Log.i("getting frds' loc", "Error :" + ex.getMessage());
                        }

                        //    Log.i("Get friends 1", json_friend.toString());
                    }
                    //get next batch of results of exists
                    GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                    if (nextRequest != null) {
                        nextRequest.setCallback(this);
                        nextRequest.executeAndWait();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //send first request, the rest should be called by the callback
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me/friends", param, HttpMethod.GET, graphCallback).executeAndWait();

        return true;
    }

    ;

    protected void onPostExecute(Boolean yesorno) {
        if (yesorno) {
            //  addFrdsToStar(this.getMasterList());
            Log.i(TAG,"Finish User onPostExecute");
            userHandler.handleLoginResults(newUser, null);
            return;
        } else {
            //do sth
        }
    }


    public String sendResult() {
        if (toOutput != null) {
            Log.i(TAG, "ok");

        } else {
            Log.i(TAG, "shit");
        }

        return toOutput;
    }

    @Override
    public String getUserId() {
        // TODO Auto-generated method stub
        return userId;
    }

    @Override
    public String getFBId() {
        // TODO Auto-generated method stub
        return fbid;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    @Override
    public ImageView getProfilePic() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFBId(String fb_id) {
        // TODO Auto-generated method stub
        fbid = fb_id;

    }


    @Override
    public void setUsername(String input_username) {
        // TODO Auto-generated method stub
        username = input_username;
    }

    @Override
    public void setProfilePic(ImageView profilePic) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<EventTypes> getAllEvents() {
        return null;
    }

    @Override
    public List<EventTypes> getEventsOrganised() {
        return eventsOrganised;
    }

    @Override
    public List<EventTypes> getEventsInvited() {
        return eventsInvited;
    }

    @Override
    public List<EventTypes> getEventsRejected() {
        return eventsRejected;
    }

    @Override
    public List<EventTypes> getEventsAttending() {
        return eventsAttending;
    }

    @Override
    public void addEvent(Events eventToAdd) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<OtherUser> getMasterList() {
        // TODO Auto-generated method stub
        //    Log.i("did i get frds",friends.get(0).username);
        return friends;
    }

    @Override
    public ArrayList<OtherUser> getStarList() {
        // TODO Auto-generated method stub
        return stars;
    }
/*
    @Override
    public void getMasterListwLoc() {
        final List<String> idsOfFrds = new ArrayList<String>();
        mapOfFrds = new HashMap<String, OtherUser>();
        for (OtherUser frds : friends) {
            idsOfFrds.add(frds.fbid);
            mapOfFrds.put(frds.fbid, frds);
            //Log.i("frds.fbid",frds.fbid);
        }
        //Log.i("mapoffriends",Integer.toString(mapOfFrds.size()));
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.i("User", "getting friends' location");

                String link = "http://letshangout.netau.net/getloc.php";
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(link);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    JSONArray json_toSend = new JSONArray(idsOfFrds);

                    //send the POST out
                    //sending out the POST
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(json_toSend.toString());
                    Log.i("json", json_toSend.toString());
                    out.flush();
                    out.close();

                    // read
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    JSONArray json_fromServer= new JSONArray(sb.toString());
                    int arrSize = json_fromServer.length();
                    for (int i = 0; i < arrSize; ++i) {
                        //get event ids
                        JSONObject json_tmp = json_fromServer.getJSONObject(i);
                        String fbid_tmp = json_tmp.getString("fbid");
                        Double lat_tmp = json_tmp.getDouble("lat");
                        Double long_tmp = json_tmp.getDouble("longitude");
                        //create events
                        OtherUser frds_tmp = mapOfFrds.get(fbid_tmp);
                        frds_tmp.hasLoc = true;
                        frds_tmp.lat = lat_tmp;
                        frds_tmp.longitude = long_tmp;
                    }
                    return "ok";
                } catch (Exception ex) {
                    Log.i("getting frds' loc", "Error :" + ex.getMessage());
                    return ex.toString();
                }
            }
            @Override
            protected void onPostExecute(String result) {
                // get with db
                Log.i("getting friends Loc" ,result);
                this.handleGetFrdsLocResults(mapOfFrds);
            }

        }.execute(null,null,null);
    }

    public void handleGetFrdsLocResults(HashMap<String,OtherUser> mapOfFrds) {

    }
*/

    public List<OtherUser> getStarListwLoc() {
        return null;
    }

    @Override
    public void addFrds(Users frdsToAdd) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addFrdsToStar(final List<OtherUser> frdsToStar, final UpdateResult handler) {
        //            stars.addAll(frdsToStar);
        //update server
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Log.i("User", "adding frds to star");
                String link = "http://letshangout.netau.net/addfrds.php";
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(link);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    JSONObject json_toSend = new JSONObject();
                    json_toSend.put("uid", userId);
                    JSONArray arrayOfFrds = new JSONArray();
                    for (int i = 0; i < frdsToStar.size(); i++) {
                        arrayOfFrds.put(frdsToStar.get(i).userId);
                    }
                    json_toSend.put("frds", arrayOfFrds);

                    //send the POST out
                    //sending out the POST
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(json_toSend.toString());
                    //   Log.i("jsonfuck", json_toSend.toString());
                    out.flush();
                    out.close();

                    // read
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    Log.i("Adding Friends", sb.toString());

                    if (sb.toString().matches("OK_wahid")) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception ex) {
                    Log.i("User", "Adding Friends Error :" + ex.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean results) {
                if (results) {
                    Log.i("Adding Frds to Stars", "success!");
                    // 0 indicates success
                    handler.handleUpdateResults(0);
                } else {
                    Log.i("Adding Frds to Stars", "fucking failed!");
                    // 1 indicates failure
                    handler.handleUpdateResults(1);
                }

            }
        }.execute(null, null, null);

    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateLocation(Location currentLoc) {
        // TODO Auto-generated method stub
        final double userLat = currentLoc.getLatitude();
        final double userLong = currentLoc.getLongitude();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.i("Location", "(User) Updating location ");

                String link = "http://letshangout.netau.net/updateloc.php";
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(link);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    JSONObject json_toSend = new JSONObject();
                    json_toSend.put("userid", userId);
                    json_toSend.put("userLat", userLat);
                    json_toSend.put("userLong", userLong);

                    //send the POST out
                    //sending out the POST
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(json_toSend.toString());
                    Log.i("json", json_toSend.toString());
                    out.flush();
                    out.close();

                    // read
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception ex) {
                    Log.i("User", "Error :" + ex.getMessage());
                    return ex.toString();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // get with db
                Log.i("Location", "Updated successfully. Result is " + result);
            }

        }.execute(null, null, null);
    }


    public void updateLocation(Location currentLoc, String userId) {
        // TODO Auto-generated method stub
        final double userLat = currentLoc.getLatitude();
        final double userLong = currentLoc.getLongitude();
        final String id = userId;

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.i("Location", "(User) Updating location ");

                String link = "http://letshangout.netau.net/updateloc.php";
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(link);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    JSONObject json_toSend = new JSONObject();
                    json_toSend.put("userid", id);
                    json_toSend.put("userLat", userLat);
                    json_toSend.put("userLong", userLong);

                    //send the POST out
                    //sending out the POST
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(json_toSend.toString());
                    Log.i("json", json_toSend.toString());
                    out.flush();
                    out.close();

                    // read
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception ex) {
                    Log.i("User", "Error :" + ex.getMessage());
                    return ex.toString();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                // get with db
                Log.i("Location", "Updated successfully. Result is " + result);
            }

        }.execute(null, null, null);
    }


    @Override
    public String addUserToDB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean updateDB() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Boolean isNew() {
        // TODO Auto-generated method stub
        return newUser;
    }

    ;

    public String toString() {
        String tmp = new String();
        tmp = "\nid: " + getUserId() +
                "\nname: " + getUsername() +
                "\nAttending: " + getEventsAttending().toString() +
                "\nInvited: " + getEventsInvited().toString() +
                "\nfriends: " + getMasterList().toString();
        return tmp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.fbid,
                        this.userId,
                        this.username,
                        this.regID}
        );
        parcel.writeList(this.eventsAttending);
        parcel.writeList(this.eventsInvited);
        parcel.writeList(this.eventsOrganised);
        parcel.writeList(this.eventsRejected);
        parcel.writeList(this.friends);
        parcel.writeList(this.stars);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


