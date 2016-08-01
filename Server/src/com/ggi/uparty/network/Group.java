package com.ggi.uparty.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable{

	public ArrayList<Event> events = new ArrayList<Event>();
	public ArrayList<Member> members = new ArrayList<Member>();
	public String owner;
	public String name;
	
}
