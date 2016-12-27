package com.ggi.uparty.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.ggi.uparty.network.Event;

public class Datapoint implements Comparable, Serializable{

	public float x=0, y=0;
	public double dist=0;
	public World w=null;
	public ArrayList<Event> events = new ArrayList<Event>();
	
	public Datapoint(){}
	
	public Datapoint(float x, float y, World world){
		this.x=x;
		this.y=y;
		this.w=world;
	}

	@Override
	public int compareTo(Object o) {
		Datapoint d = (Datapoint)(o);
		double dist = getDistance()-d.getDistance();
		if(dist>0){return 1;}
		else if(dist<0){return -1;}
		else{return 0;}
	}

	public double getDistance() {
		dist = Math.sqrt(Math.pow(x-w.refX, 2)+Math.pow(y-w.refY, 2));
		//dist = Haversine.distance(y, x, w.refY, w.refX);
		return dist;
	}
	
}
