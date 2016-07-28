package com.ggi.uparty.util;

import java.util.Comparator;

import com.ggi.uparty.ui.EventModule;

public class HotComparator implements Comparator<EventModule>{

	@Override
	public int compare(EventModule o1, EventModule o2) {
		int o1C=o1.e.upVote.size()-o1.e.downVote.size();
		int o2C=o2.e.upVote.size()-o2.e.downVote.size();
		return o2C-o1C;
	}

}
