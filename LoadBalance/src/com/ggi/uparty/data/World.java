package com.ggi.uparty.data;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.ggi.uparty.network.Event;

/**
 * 
 */

/**
 * @author Emmett
 *
 */
public class World implements Comparator<Datapoint>, Serializable{

	public ArrayList<Datapoint> points = new ArrayList<Datapoint>();
	public ArrayList<Event> reported = new ArrayList<Event>();
	public float radius=25f;
	public float refX=0,refY=0;
	public int eventsInStorage = 0;
	
	public World(){
		
		
	}
	
	public void init(){
		try {
			List<String> lines = Files.readAllLines(Paths.get("5mrad.txt"), Charset.defaultCharset());
			for(String s:lines){
				String[] breakdown = s.split(" ");
				addPoint(Float.parseFloat(breakdown[0]),Float.parseFloat(breakdown[1]));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addPoint(float x, float y){
		points.add(new Datapoint(x,y,this));
	}
	
	public ArrayList<Datapoint> getAround(float x, float y){
		ArrayList<Datapoint> result = new ArrayList<Datapoint>();
		refX=x; refY=y;
		points.sort(this);
		for(Datapoint p: points){
			if(result.size()<8){
				result.add(p);
			}
		}
		return result;
	}
	
	public void addToClosest(Event e){
		refX=e.lat; refY=e.lng;
		points.sort(this);
		points.get(0).events.add(e);
		eventsInStorage++;
	}

	@Override
	public int compare(Datapoint o1, Datapoint o2) {
		return o1.compareTo(o2);
	}
	
}
