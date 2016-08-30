package com.ggi.uparty.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.screens.CreateEventScreen;
import com.ggi.uparty.screens.LoadScreen;
import com.ggi.uparty.screens.MainScreen;
import com.ggi.uparty.screens.NewGroupScreen;
import com.ggi.uparty.screens.SettingsScreen;

public class PopUpMenu {

	public MainScreen s;
	
	public float slide=0;//-.7525f*s.u.h = closed
	
	public boolean open = false;
	
	public Texture background,bottom,darken;
	
	public Button slideButton;
	
	public TextButton post,settings,group;
	
	public Rectangle bounds,slideB,postB,settingsB,groupB;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public RadialSprite lvBar;
	
	public float lvAngle=0;
	
	public List l;
	
	public ArrayList<Module> modules = new ArrayList<Module>();

	public boolean initTouch;
	
	public PopUpMenu(MainScreen s){
		this.s=s;
		l=new List(s.u);
		
		modules.add(new GroupModule(l,null));
		for(int i = 0; i < s.u.myAcc.groups.size();i++){
		modules.add(new GroupModule(l,s.u.myAcc.groups.get(i)));
		}
		
		background = s.u.assets.get("UI/PopUpMenu.png");
		bottom = s.u.assets.get("UI/PopUpMenuBottom.png");
		darken = s.u.assets.get("UI/Darken.png");
		slide=-.7525f*s.u.h;
		bounds = new Rectangle(0,slide,s.u.w,.8f*s.u.h);
		slideB = new Rectangle(bounds.width/2-.048f*s.u.w,.97f*bounds.height+bounds.y-.012f*s.u.h,.096f*s.u.w,.024f*s.u.h);
		slideButton = new Button(s.u.slideStyle);
		slideButton.setBounds(slideB.x, slideB.y, slideB.width, slideB.height);
		lvBar = new RadialSprite(new TextureRegion(s.u.assets.get("UI/CircleLoad.png",Texture.class)));
		
		postB = new Rectangle(bounds.width/4,.52f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		settingsB = new Rectangle(bounds.width/4,.36f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		groupB = new Rectangle(bounds.width/4,.44f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		
		post = new TextButton("Post an Event",s.u.standardButtonStyle);
			post.setBounds(postB.x, postB.y, postB.width, postB.height);
		group = new TextButton("New Group",s.u.standardButtonStyle);
			group.setBounds(groupB.x, groupB.y, groupB.width, groupB.height);
		settings = new TextButton("Settings",s.u.standardButtonStyle);
			settings.setBounds(settingsB.x, settingsB.y, settingsB.width, settingsB.height);
		
		
		s.u.myAcc.xp=9;
	}
	
	public void draw(SpriteBatch pic, float fade){
		if(s.u.accRefresh){
			modules.clear();
			l=new List(s.u);
			
			modules.add(new GroupModule(l,null));
		for(int i = 0; i < s.u.myAcc.groups.size();i++){
			modules.add(new GroupModule(l,s.u.myAcc.groups.get(i)));
		}	
		s.u.accRefresh = false;
		}
		l.bounds=new Rectangle(bounds.x,bounds.y,bounds.width,.3f*bounds.height);
		l.give(modules);
		
		if(open&&slide!=0){slide+=(0-slide)/3;}
		else if(!open&&slide!=-.7525f*s.u.h){slide+=(-.7525f*s.u.h-slide)/3;}
		float ratio = -(leftOverXP/neededToLvXP)*360;
		if(slide>-.1f*s.u.h&&lvAngle!=ratio){lvAngle+=(ratio-lvAngle)/32;}
		lvBar.setAngle(lvAngle);
		
		bounds = new Rectangle(0,slide,s.u.w,.8f*s.u.h);
		slideB = new Rectangle(bounds.width/2-.048f*s.u.w,.97f*bounds.height+bounds.y-.012f*s.u.h,.096f*s.u.w,.024f*s.u.h);
		slideButton.setBounds(slideB.x, slideB.y, slideB.width, slideB.height);
		if(slideButton.isChecked()!=open){slideButton.toggle();lvAngle=0;getLv();}
		
		pic.setColor(1, 1, 1, fade);
		if(open&&slide>-.05f*s.u.h){pic.draw(darken,0,0,s.u.w,s.u.h);}
		pic.draw(bottom,bounds.x,bounds.y,bounds.width,bounds.height);
		
		postB = new Rectangle(bounds.width/4,.52f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		settingsB = new Rectangle(bounds.width/4,.36f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		groupB = new Rectangle(bounds.width/4,.44f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		post.setBounds(postB.x, postB.y, postB.width, postB.height);
		settings.setBounds(settingsB.x, settingsB.y, settingsB.width, settingsB.height);
		group.setBounds(groupB.x, groupB.y, groupB.width, groupB.height);
		
		l.draw(pic, fade);
		
		pic.draw(background,bounds.x,bounds.y,bounds.width,bounds.height);
		slideButton.draw(pic, fade);
		post.draw(pic, fade);
		settings.draw(pic, fade);
		group.draw(pic, fade);
		
		s.u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(s.u.largeFnt, s.u.myAcc.u);
		s.u.largeFnt.draw(pic, s.u.myAcc.u, bounds.width/2-layout.width/2, .93f*bounds.height+bounds.y-layout.height/2);
		
		layout.setText(s.u.largeFnt, ""+lv);
		s.u.largeFnt.draw(pic, ""+lv, bounds.width/2-layout.width/2, .75f*bounds.height+bounds.y-layout.height/2);
		
		lvBar.setColor(new Color(1,1,1,fade));
		if(leftOverXP!=0&&lvAngle!=0){lvBar.draw(pic, bounds.width/2-s.u.w/8, .75f*bounds.height+bounds.y-layout.height-s.u.w/8,s.u.w/4,s.u.w/4);}
		
		
		s.u.smallFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(s.u.smallFnt, "("+s.u.myAcc.e+")");
		s.u.smallFnt.draw(pic, "("+s.u.myAcc.e+")", bounds.width/2-layout.width/2, .87f*bounds.height+bounds.y-layout.height/2);
		
		s.u.smallFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(s.u.smallFnt, s.u.controller.getLat()+":"+s.u.controller.getLong());
		//s.u.smallFnt.draw(pic, s.u.controller.getLat()+":"+s.u.controller.getLong(), bounds.width/2-layout.width/2, .87f*bounds.height+bounds.y-layout.height/2);
		
		
		layout.setText(s.u.smallFnt, "Groups:");
		s.u.smallFnt.draw(pic, "Groups:", bounds.width/2-layout.width/2, .34f*bounds.height+bounds.y-layout.height/2);
		
		
		
		
	}

	private void getLv() {
		lv = 0;
		while(factorial(lv+1)*5<=s.u.myAcc.xp){
			lv++;
		}
		leftOverXP=s.u.myAcc.xp-factorial(lv)*5;
		neededToLvXP=(factorial(lv+1)-factorial(lv))*5;
		
	}
	
	public long factorial(long number) {
		if(number==0){return 0;}
	    long factorial = 1;
	    for (long i = 1; i <= number; ++i) {
	        factorial *= i;
	    }
	    return factorial;
	}

	public void touchDown(Rectangle touch) {
		if(Intersector.overlaps(touch, postB)){post.toggle();}
		else if(Intersector.overlaps(touch, settingsB)){settings.toggle();}
		else if(Intersector.overlaps(touch, groupB)){group.toggle();}
		else if(Intersector.overlaps(touch, l.bounds)){l.touchDown(touch);initTouch=true;}
	}

	public void touchUp(Rectangle touch) {
		toggleOff();
		
		if(!initTouch){
		if(Intersector.overlaps(touch, postB)){s.u.nextScreen=new CreateEventScreen(s.u,s.g);}
		//else if(Intersector.overlaps(touch, settingsB)){s.u.myAcc=null;s.u.settings=true;s.u.setScreen(new LoadScreen(s.u));}
		else if(Intersector.overlaps(touch, settingsB)){s.u.setScreen(new SettingsScreen(s.u));}
		else if(Intersector.overlaps(touch, groupB)){s.u.nextScreen=new NewGroupScreen(s.u);}
		else if(!open){open=!open;}
		}
		if(Intersector.overlaps(touch, l.bounds)){l.touchUp(touch);}
		initTouch=false;
		
	}

	private void toggleOff() {
		if(post.isChecked()){post.toggle();}
		if(settings.isChecked()){settings.toggle();}
		if(group.isChecked()){group.toggle();}
		
	}

	public void touchDragged(int screenY) {
		if(initTouch){
			l.move(screenY-l.lastY);
		}
		
	}

	
	
	
}
