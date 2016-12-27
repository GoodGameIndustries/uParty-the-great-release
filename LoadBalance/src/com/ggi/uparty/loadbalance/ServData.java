package com.ggi.uparty.loadbalance;

import com.esotericsoftware.kryonet.Connection;

public class ServData implements Comparable{	
	public Connection c;
	public int cons = 0;
	
	public ServData(){}
	public ServData(Connection c){
		this.c=c;
	}
	@Override
	public int compareTo(Object o) {
		ServData s = (ServData) o;
		return cons-s.cons;
	}
}
