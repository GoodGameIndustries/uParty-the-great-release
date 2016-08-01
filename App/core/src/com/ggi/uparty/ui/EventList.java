package com.ggi.uparty.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.screens.MainScreen;
import com.ggi.uparty.util.HotComparator;
import com.ggi.uparty.util.NewComparator;
import com.ggi.uparty.util.NextComparator;

public class EventList {

	public MainScreen s;
	
	public ArrayList<EventModule> modules = new ArrayList<EventModule>();
	public ArrayList<Event> evs = new ArrayList<Event>();
	
	public int scrolled = 0;
	
	public int focus = 0;

	public int momentum=0;
	
	public boolean refresh=false;
	
	public RefreshModule rM;
	
	public EventList(MainScreen s){
		this.s=s;
		rM = new RefreshModule(s);
	}
	
	public void giveEvents(ArrayList<Event> evs){
		modules.clear();
		this.evs.clear();
		this.evs.addAll(evs);
		System.out.println(this.evs.size());
		focus=0;
		
		for(Event e:evs){
			modules.add(new EventModule(e,this));
		}
		
		sort();
		
		refresh=false;
	}
	
	public void sort() {
		switch(s.toolbar.sortState){
		case 0:modules.sort(new HotComparator());
			break;
		case 1:modules.sort(new NextComparator());
			break;
		case 2:modules.sort(new NewComparator());
			break;
		}
		
	}

	public void draw(SpriteBatch pic, float fade){
		if(s.lastY==0){
		scrolled+=momentum;
		momentum/=1.1;}
		if(scrolled>.19f*s.u.h){scrolled=0;focus++;}
		if(scrolled<-.19f*s.u.h){scrolled=0;focus--;}
		if(focus>=modules.size()-1&&modules.size()>0){focus=modules.size()-1;if(scrolled>0){scrolled=0;}}
		if(focus<0&&!refresh){refresh=true;s.refresh();}
		
		if(refresh){focus=0;scrolled=(int) (-.095f*s.u.h);}
		
		float alpha = scrolled/(.19f*s.u.h);
		if(s.lastY==0&&momentum == 0&&!refresh){scrolled+=(0-scrolled)/3;}
		
		if(!refresh&&focus==0){rM.bounds=new Rectangle(0,s.toolbar.bounds.y+alpha*.34f*s.u.h,s.u.w,.17f*s.u.h);}
		else if(refresh){rM.bounds=new Rectangle(0,s.toolbar.bounds.y-.17f*s.u.h,s.u.w,.17f*s.u.h);}
		
		rM.draw(pic, fade);
		//System.out.println(scrolled+":"+.19f*s.u.h+":"+scrolled/.19f*s.u.h);
		
		for(int i = 0;i < modules.size(); i++){
		if(focus-i==2){modules.get(i).bounds=new Rectangle((.14f+alpha*.045f)*s.u.w,(.8f+alpha*.06f)*s.u.h,(.72f-alpha*.09f)*s.u.w,(.13f-alpha*.02f)*s.u.h);
		modules.get(i).Draw(pic, fade/4-alpha*fade/4);
		}
		else if(focus-i==1){modules.get(i).bounds=new Rectangle((.095f+alpha*.045f)*s.u.w,(.74f+alpha*.06f)*s.u.h,(.81f-alpha*.09f)*s.u.w,(.15f-alpha*.02f)*s.u.h);
		modules.get(i).Draw(pic, 3*fade/4-alpha*fade/4);
		}
		else if(focus-i==-4){modules.get(i).bounds=new Rectangle((.095f-alpha*.045f)*s.u.w,(.05f+alpha*.06f)*s.u.h,(.81f+alpha*.09f)*s.u.w,(.15f+alpha*.02f)*s.u.h);
		modules.get(i).Draw(pic, 3*fade/4+alpha*fade/4);
		}
		else if(i==focus&&scrolled>0){
			modules.get(i).bounds=new Rectangle((.05f+alpha*.045f)*s.u.w,(.68f+.19f*(focus-i)+alpha*.06f)*s.u.h,(.9f-alpha*.09f)*s.u.w,(.17f-alpha*.02f)*s.u.h);
			modules.get(i).Draw(pic, fade-alpha*fade/4);
		}
		else if(focus-i==-5){modules.get(i).bounds=new Rectangle((.14f-alpha*.045f)*s.u.w,(-.01f+alpha*.06f)*s.u.h,(.72f+alpha*.09f)*s.u.w,(.13f+alpha*.02f)*s.u.h);
		modules.get(i).Draw(pic, fade/4+alpha*fade/4);
		}
		}
		
		for(int i = 0;i < modules.size(); i++){
		if(i==focus+3&&scrolled<0){
			modules.get(i).bounds=new Rectangle((.05f-alpha*.045f)*s.u.w,(.68f+.19f*(focus-i)+alpha*.06f)*s.u.h,(.9f+alpha*.09f)*s.u.w,(.17f+alpha*.02f)*s.u.h);
			modules.get(i).Draw(pic, fade+alpha*fade/4);
		}
		}
		
		for(int i = 0;i < modules.size(); i++){
		
		if(i>=focus&&i<=focus+3){
			if(i==focus&&scrolled>0){}
			else if(i==focus+3&&scrolled<0){}
			else{
			modules.get(i).bounds=new Rectangle(.05f*s.u.w,(.68f+.19f*(focus-i))*s.u.h+scrolled,.9f*s.u.w,.17f*s.u.h);
			modules.get(i).Draw(pic, fade);
			}
		}
		
			
		}
	}

	public void touch(Rectangle touchDown, Rectangle touchUp) {
		for(int i = 0; i < modules.size(); i++){
			if(Intersector.overlaps(touchDown, modules.get(i).bounds)&&Intersector.overlaps(touchUp, modules.get(i).bounds)){modules.get(i).touch(touchDown,touchUp);}
		}
	}
	
}
