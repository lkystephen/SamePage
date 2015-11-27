package com.example.projecttesting;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by edmundlee on 6/13/15.
 */
public class User extends AsyncTask<Void,Void,Boolean> implements Users, Parcelable {
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
        private String regID;
        public ArrayList<OtherUser> friends;
        HashMap<String, OtherUser>  mapOfFrds;

    //  private List<String> idsOfFriends;


    //backdoor constructor
    public User (String password) {
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
            OtherUser json_tmp = new OtherUser("fbid",null,"username");
            friends.add(json_tmp);
        }
    }
    // constructor
        public User (String userid, MainAct mainAct){
            fbid = userid;
            //username = username_input;

            //initialise
            eventsAttending = new ArrayList<EventTypes>();
            eventsInvited = new ArrayList<EventTypes>();
            eventsOrganised = new ArrayList<EventTypes>();
            eventsRejected = new ArrayList<EventTypes>();
        }
        //constructor for parcel
        public User (Parcel in) {
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
            in.readList(eventsAttending, EventType.class.getClassLoader());
            in.readList(eventsInvited,EventType.class.getClassLoader());
            in.readList(eventsOrganised,EventType.class.getClassLoader());
            in.readList(eventsRejected,EventType.class.getClassLoader());
            in.readList(friends,OtherUser.class.getClassLoader());
            //public Boolean newUser;
        }

        // for testing
        public User (String uid, String inputUsername, String regId, MainAct mainAct){
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
            //get friends - testing

            Bundle param = new Bundle();
            param.putString("fields", "id,name");
            param.putInt("limit", 3000);

            // var to store friends
            friends = new ArrayList<OtherUser>();
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

                            String link = "http://letshangout.netau.net/getloc.php";
                            HttpURLConnection urlConnection = null;

                            try {
                                URL url = new URL(link);
                                urlConnection = (HttpURLConnection) url.openConnection();
                                urlConnection.setDoOutput(true);
                                urlConnection.setRequestMethod("POST");
                                urlConnection.setRequestProperty("Content-Type", "application/json");
                                urlConnection.setRequestProperty("Accept", "application/json");

                                JSONArray json_toSend = new JSONArray(fbid_tmp);

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
                                JSONObject json_fromServer= new JSONObject(sb.toString());
                                double lat_tmp = json_fromServer.getDouble("lat");
                                double long_tmp = json_fromServer.getDouble("longitude");
                                double lat_o_tmp = json_fromServer.getDouble("lat_old");
                                double long_o_tmp = json_fromServer.getDouble("long_old");
                                String uid_tmp = json_fromServer.getString("userid");
                                friends.add(new OtherUser(fbid_tmp,uid_tmp,username_tmp, lat_tmp,long_tmp,lat_o_tmp,long_o_tmp));
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
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me/friends",param, HttpMethod.GET, graphCallback).executeAndWait();
            for (int i=0; i < friends.size(); ++i)
            {
                Log.i ("Get Friends", friends.get(i).toString());

            }

            //	String toOutput;
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
                BufferedReader reader = new BufferedReader (new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                Log.i("jsonFrmServer", sb.toString());

                JSONObject json_fromServer= new JSONObject(sb.toString());
                userId = json_fromServer.getString("userid");
                String isNewUser = json_fromServer.getString("isNew");
                // for first time user
                if (isNewUser.equals("true")) {
                    newUser = true;
                }
                // for exisiting users
                else {
                    newUser = false;
                    //get events
                    //events pending
                    if (json_fromServer.isNull("0")) {
                        Log.i("json","no events pending");
                    } else {
                        JSONArray json_events_invited = json_fromServer.getJSONArray("0");
                        // convert events to arraylist
                        int arrSize = json_events_invited.length();
                        for (int i = 0; i < arrSize; ++i) {
                            //get event ids
                            JSONObject json_event_tmp = json_events_invited.getJSONObject(i);
                            //create events
                            EventType event_tmp = new EventType (json_event_tmp);
                            //to set parameters for event
                            // To do..
                            eventsInvited.add(event_tmp);
                        }
                        //sort eventsInvited
                        Collections.sort(eventsInvited);

                        int arr_test = eventsInvited.size();
                        for (int j = 0; j < arr_test; ++j){
                            Log.i("events test in User", eventsInvited.get(j).toString());
                        }
                    }

                    //events organised
                    if (json_fromServer.isNull("3")) {
                        Log.i("json","no events organised");
                    } else {
                        JSONArray json_events_organised = json_fromServer.getJSONArray("3");
                        int arrSize_org = json_events_organised.length();
                        for (int i = 0; i < arrSize_org; ++i) {
                            //get event ids
                            JSONObject json_event_tmp = json_events_organised.getJSONObject(i);
                            //create events
                            EventType event_tmp = new EventType (json_event_tmp);
                            //to set parameters for event
                            eventsOrganised.add(event_tmp);
                        }
                        //sort
                        Collections.sort(eventsOrganised);
                    }
                    //for events attending
                    if (json_fromServer.isNull("1")) {
                        Log.i("json","no events attending");
                    } else {
                        JSONArray json_events_attending = json_fromServer.getJSONArray("1");
                        int arrSize_a = json_events_attending.length();
                        for (int i = 0; i < arrSize_a; ++i) {
                            //get event ids
                            JSONObject json_event_tmp = json_events_attending.getJSONObject(i);
                            //create events
                            EventType event_tmp = new EventType (json_event_tmp);
                            //to set parameters for event
                            eventsAttending.add(event_tmp);
                        }
                        //sort
                        Collections.sort(eventsAttending);
                    }

                    //events rejected
                    if (json_fromServer.isNull("2")) {
                        Log.i("json","no events rejceted");
                    } else {
                        JSONArray json_events_rejected = json_fromServer.getJSONArray("2");
                        int arrSize_r = json_events_rejected.length();
                        for (int i = 0; i < arrSize_r; ++i) {
                            //get event ids
                            JSONObject json_event_tmp = json_events_rejected.getJSONObject(i);
                            //create events
                            EventType event_tmp = new EventType (json_event_tmp);
                            //to set parameters for event
                            eventsRejected.add(event_tmp);
                        }
                        //sort
                        Collections.sort(eventsRejected);
                    }
                }
                Log.i(TAG, "step 2");
            }catch (Exception e){
                Log.i(TAG, e.toString());
            }finally {
                urlConnection.disconnect();
            }
            return true;
        };

        protected void onPostExecute(Boolean yesorno){
            if (yesorno) {
               /* HashMap eventsMap = new HashMap();
                eventsMap.put("Attend",eventsAttending);
                eventsMap.put("Rejected",eventsRejected);
                eventsMap.put("Invited",eventsInvited);
                eventsMap.put("Organised",eventsOrganised); */
              //  Log.i(TAG, "into onpostexecute");
                userHandler.handleLoginResults(newUser, null);
                return;
            }
            else {
                //do sth
            }
        }


        public String sendResult() {
            if (toOutput != null) {
                Log.i(TAG, "ok");

            }else{
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
            return friends;
        }

        @Override
        public List<OtherUser> getStarList() {
            // TODO Auto-generated method stub
            return null;
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
        public void addFrdsToStar(OtherUser frdsToStar) {
            // TODO Auto-generated method stub

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
                    Log.i("User", "updating user location");

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
                    Log.i("updating Loc" ,result);
                }

            }.execute(null,null,null);
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
        };

    public String toString() {
        String tmp = new String();
        tmp = "\nid: "+ getUserId() +
                "\nname: "+getUsername() +
                "\nAttending: "+getEventsAttending().toString()+
                "\nInvited: "+getEventsInvited().toString()+
                "\nfriends: "+getMasterList().toString();
        return tmp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {  this.fbid,
                                                this.userId,
                                                this.username,
                                                this.regID}
        );
        parcel.writeList(this.eventsAttending);
        parcel.writeList(this.eventsInvited);
        parcel.writeList(this.eventsOrganised);
        parcel.writeList(this.eventsRejected);
        parcel.writeList(this.friends);
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


