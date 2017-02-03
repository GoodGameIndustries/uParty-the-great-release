package com.ggi.uparty.loadbalance;

import java.util.ArrayList;
import java.util.Date;

public class Tracker implements Runnable{

	int users = 0;
	boolean added = false;
	public ArrayList<TrackData> report = new ArrayList<TrackData>();
	
	@Override
	public void run() {
		while (true){
			Date d = new Date();
			if(d.getMinutes()==0 && !added){
				added = true;
				TrackData t = new TrackData();
				t.d=d;
				t.c=users;
				report.add(t);
				users = 0;
				System.out.println("[TRACK]-hour tracked");
				if(d.getHours()==23){
					String msg = "";
					for(int i = 0; i < report.size(); i++){
						msg += report.get(i).toString() + "\n";
					}
					report.clear();
					try {
						new SendMailSSL().send("goodgameindustries@gmail.com", "Report", msg);
						System.out.println("[TRACK]-daily report sent");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(d.getMinutes()==1){
				added  =false;
			}
		}
		
	}

	public void newClient() {
		users++;
		System.out.println("[TRACK]-tracking new connection");
		
	}

}
