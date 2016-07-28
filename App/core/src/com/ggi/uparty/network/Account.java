package com.ggi.uparty.network;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable{
public String u="",e="",p="";
public long xp=5;
public int code = 0;
public boolean confirmed = false;
public ArrayList<Friend> friends  = new ArrayList<Friend>();
public ArrayList<Group> groups  = new ArrayList<Group>();
}
