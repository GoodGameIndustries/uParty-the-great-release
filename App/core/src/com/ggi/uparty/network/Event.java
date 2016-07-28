package com.ggi.uparty.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable{
	public String name="", description = "", location = "";
	public float lng=0,lat=0;
	public ArrayList<String> upVote = new ArrayList<String>();
	public ArrayList<String> downVote = new ArrayList<String>();
	public ArrayList<String> comments = new ArrayList<String>();
	public String ID="";
	public Date start,end,posted;
	public Account owner;
	public Event(){}
	public Event(float lng,float lat,String name,String description,String location,Date start, Date end,Account owner){
		this.lng=lng;this.lat=lat;this.name=name;this.description=description;this.location=location;
		ID=name+description+location+lng+""+lat;
		this.start=start;
		this.end=end;
		this.owner=owner;
		posted = new Date();
	}
	
}
