package com.ggi.uparty.util;

import java.util.Comparator;

import com.ggi.uparty.network.Member;

public class RankComparator implements Comparator<Member>{

	@Override
	public int compare(Member arg0, Member arg1) {
		// TODO Auto-generated method stub
		return arg1.rank-arg0.rank;
	}

}
