package com.ggi.uparty.loadbalance;

import java.util.Date;

public class TrackData {
	public Date d;
	public int c;
	
	public String toString(){
		Date d2 = d;
		d2.setHours(d.getHours()-1);
		return d2.toString()+" - "+d.toString()+" users connected: " + c;
		
	}
}
