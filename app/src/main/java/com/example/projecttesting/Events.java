package com.example.projecttesting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.location.Location;


public interface Events {
	//variables
		
	//I. Modification
	// creating an event without date option
	public void createEvent();
	// creating an event with date option
	public void createEventWithOpt();
		
	//For existing events, set event ID 
	public void setEventId(String eventId);
	
	//updating a particular aspect of event
	public void setEventType(String type);
	public void setEventName(String name);
	public void setEventDetails(String details);
	//depending on organiser's pref, it can be set to one date time or three
	public void setEventDateTime(Calendar dateTime);
	public void setEventDateTimeOptions(List<Calendar> dateOptions);
	public void setEventVenue(String venue, Location location);
	//invite frds by entering their user ID
	public void inviteFriends(List<String> listOfFrds);
	
	//delete an event
	public void deleteEvent();
	
	//II. Connecting with DataBase
	
	//create a new event entry at DB and return a unique eventID
	public String createEventAtServer();
	//update database for updating/deleting, return true if success
	public boolean updateDB();
	
	//III. get Event details
	//return event details
	public Boolean isOptional();
	public String getEventid();
	public String getEventType();
	public String getEventName();
	public String getEventDetails();
	public List<Calendar> getEventDateTimeOptions();
	public Calendar getEventDateTime();
	public String getEventVenue();
	public Location getEventLoc();
	
	//return user ID of those who were invited
	public List<String> getEventInvitees();	
	//return user ID of those who agree to attend
	public List<String> getAttendees();
	//return user ID of those who declines to attend
	public List<String> getRejects();
	//return user ID of those whose replies are pending
	public List<String> getPending();
	
	//IV. Inform invitees of event
	//0=new event invitation, 1=event details update, 2=event cancelled
	public void sendNotifications(int typeOfNotification);
	void setOrganiser(String organiser);
}
