package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.uParty;
import com.ggi.uparty.screens.EventScreen;

public class EventBar {

	public EventScreen s;
	public uParty u;
	
	public int select = 0; // 0 = description 1 = location 2 = time
	
	public Texture desc,descC,loc,locC,time,timeC;
	
	Rectangle bounds;
	
	public EventBar(uParty u, EventScreen s){
		this.u=u;
		this.s=s;
		
		desc = u.assets.get("UI/Toolbar/Desc.png");
		descC = u.assets.get("UI/Toolbar/DescC.png");
		loc = u.assets.get("UI/Toolbar/Loc.png");
		locC = u.assets.get("UI/Toolbar/LocC.png");
		time = u.assets.get("UI/Toolbar/Recent.png");
		timeC = u.assets.get("UI/Toolbar/RecentC.png");
		
		bounds = new Rectangle(0,.66375f*u.h,.11f*u.w,.189375f*u.h);
	}
	
	public void draw(SpriteBatch pic, float fade){
		if(s.scrolled < 0){s.scrolled = 0;}
		if(s.scrolled > .378750f*u.h) {s.scrolled = .378750f*u.h;}
		
		
		
		//select = Math.round(s.scrolled/(.189375f*u.h));
		
		//System.out.println(select);
		
		pic.setColor(1, 1, 1, fade);
		pic.draw(u.assets.get("UI/EventModule.png",Texture.class),bounds.x,bounds.y,bounds.width,bounds.height);
		
		pic.draw(select == 0?descC:desc,bounds.x+bounds.width/2-.03f*u.w,bounds.y+(5f/6f)*bounds.height-.03f*u.w,.06f*u.w,.06f*u.w);
		pic.draw(select == 1?locC:loc,bounds.x+bounds.width/2-.03f*u.w,bounds.y+(3f/6f)*bounds.height-.03f*u.w,.06f*u.w,.06f*u.w);
		pic.draw(select == 2?timeC:time,bounds.x+bounds.width/2-.03f*u.w,bounds.y+(1f/6f)*bounds.height-.03f*u.w,.06f*u.w,.06f*u.w);
		
		pic.draw(u.assets.get("UI/Filled.png",Texture.class),.10f*u.w,bounds.y+bounds.height-.189375f*u.h/3f - s.scrolled/3,.01f*u.w,.189375f*u.h/3f);
	}
	
}
