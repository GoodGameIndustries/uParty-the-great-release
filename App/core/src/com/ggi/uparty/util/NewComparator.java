package com.ggi.uparty.util;

import java.util.Comparator;

import com.ggi.uparty.ui.EventModule;

public class NewComparator implements Comparator<EventModule>{

	@Override
	public int compare(EventModule o1, EventModule o2) {
		// TODO Auto-generated method stub
		return (int) (o2.e.posted.getTime()-o1.e.posted.getTime());
	}

}
