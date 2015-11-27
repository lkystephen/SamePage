package com.example.projecttesting;

import java.util.List;

import android.location.Location;
import android.widget.ImageView;

import org.json.JSONObject;

public interface Users {
	//I. Basic info
	public String getUserId();
	public String getFBId();
	public String getUsername();
	public ImageView getProfilePic();
	public Boolean isNew();
	
	//for first timer
	public void setFBId(String fb_id);
	public void setUsername(String username);
	public void setProfilePic(ImageView profilePic);

	//II. Event organisation
	//Getting the lists
	public List<EventTypes> getAllEvents();
	public List<EventTypes> getEventsOrganised();
	public List<EventTypes> getEventsInvited();
	public List<EventTypes> getEventsRejected();
	public List<EventTypes> getEventsAttending();
	
	//Adding an event; update DB automatically 
	public void addEvent(Events eventToAdd);
	//RSVP - 0=pending, 1=attend, 2=reject; update DB automatically

	//III. Friends management
	//Getting the lists
	public List<OtherUser> getMasterList();
	public List<OtherUser> getStarList();
  //  public void getMasterListwLoc();
  //  public List<OtherUser> getStarListwLoc();


    //Adding friends; update DB automatically
	public void addFrds(Users frdsToAdd);
	//Adding friends to Star List; update DB automatically
	public void addFrdsToStar(OtherUser frdsToStar);
	
	//IV. Location services
	public Location getLocation();
	public void updateLocation(Location currentLoc);
	
	//IV. Updating database
	//for new users, return unique userID
	public String addUserToDB();
	//backup purposes
	public boolean updateDB();
}
