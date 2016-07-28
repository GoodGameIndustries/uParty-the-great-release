package com.ggi.uparty.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.screens.MainScreen;

public class Toolbar {

	public MainScreen s;
	
	public Rectangle bounds;
	
	public int sortState=1;//0=hot 1=next 2=new
	
	public Texture bg,logo;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public TextButton hot,next,nw;
	
	public Rectangle hotB,nextB,nwB;
	
	public String feed = "";
	
	
	public Toolbar(MainScreen s){
		this.s=s;
		bounds = new Rectangle(0,.916f*s.u.h,s.u.w,.094f*s.u.h);
		bg = s.u.assets.get("UI/Toolbar.png");
		logo = s.u.assets.get("Logos/512.png");
		
		hotB=new Rectangle(bounds.x,bounds.y,bounds.width/3f,bounds.height/2);
		nextB=new Rectangle(bounds.x+bounds.width/3f,bounds.y,bounds.width/3f,bounds.height/2);
		nwB=new Rectangle(bounds.x+2*bounds.width/3f,bounds.y,bounds.width/3f,bounds.height/2);
		
		hot = new TextButton("Hot",s.u.sortButtonStyle);
			hot.setBounds(hotB.x, hotB.y, hotB.width, hotB.height);
		next = new TextButton("Next",s.u.sortButtonStyle);
			next.setBounds(nextB.x,nextB.y,nextB.width,nextB.height);
		nw = new TextButton("New",s.u.sortButtonStyle);
			nw.setBounds(nwB.x, nwB.y, nwB.width, nwB.height);
	}
	
	public void draw(SpriteBatch pic, float fade){
		toggle();
		pic.setColor(1, 1, 1, fade);
		pic.draw(bg,bounds.x,bounds.y,bounds.width,bounds.height);
		s.u.mediumFnt.setColor(new Color(0f,0f,0f,fade));
		layout.setText(s.u.mediumFnt, "uParty");
		s.u.mediumFnt.draw(pic, "uParty", .02f*bounds.width, .87f*bounds.height-layout.height/2+bounds.y);
		layout.setText(s.u.mediumFnt, s.u.myAcc.u);
		s.u.mediumFnt.draw(pic, s.u.myAcc.u, .98f*bounds.width-layout.width, .87f*bounds.height-layout.height/2+bounds.y);
		if(!(feed.length()>0)){
		pic.setColor(1, 1, 1, fade);
		pic.draw(logo,bounds.x+.5f*bounds.width-.15f*bounds.height,bounds.y+.55f*bounds.height,.3f*bounds.height,.3f*bounds.height);
		}
		hot.draw(pic, fade);
		next.draw(pic, fade);
		nw.draw(pic, fade);
	}

	private void toggle() {
		if(hot.isChecked()){hot.toggle();}
		if(next.isChecked()){next.toggle();}
		if(nw.isChecked()){nw.toggle();}
		
		switch(sortState){
		case 0:hot.toggle();
			break;
		case 1:next.toggle();
			break;
		case 2:nw.toggle();
			break;
		}
		
	}

	public void touchDown(Rectangle touch) {
		
		
	}

	public void touchUp(Rectangle touch) {
		if(Intersector.overlaps(touch, hotB)){sortState=0;}
		else if(Intersector.overlaps(touch, nextB)){sortState=1;}
		else if(Intersector.overlaps(touch, nwB)){sortState=2;}
		s.events.sort();
		
	}
	
}
