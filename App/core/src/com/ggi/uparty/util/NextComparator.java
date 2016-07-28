package com.ggi.uparty.util;

import java.util.Comparator;

import com.ggi.uparty.ui.EventModule;

public class NextComparator implements Comparator<EventModule>{

	@Override
	public int compare(EventModule o1, EventModule o2) {
		// TODO Auto-generated method stub
		return (int) (o1.e.start.getTime()-o2.e.start.getTime());
	}

}
