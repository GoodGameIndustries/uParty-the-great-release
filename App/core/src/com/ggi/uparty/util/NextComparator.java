package com.ggi.uparty.util;

import java.util.Comparator;

import com.ggi.uparty.ui.EventModule;

public class NextComparator implements Comparator<EventModule>{

	@Override
	public int compare(EventModule o1, EventModule o2) {
		if(o1.e.start.after(o2.e.start)){
			return 1;
		}
		else{
			return -1;
		}
		//return (int) (o1.e.start.getTime()-o2.e.start.getTime());
	}

}
