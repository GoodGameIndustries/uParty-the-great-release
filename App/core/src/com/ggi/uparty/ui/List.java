package com.ggi.uparty.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Group;

public class List {

	public Rectangle bounds = new Rectangle();
	
	public ArrayList<Module> modules = new ArrayList<Module>();
	
	public uParty u;
	
	public float modHeight = .05f;
	
	public boolean scrolled = false;
	
	public float lastY;
	
	public float scr=0;
	
	public Group g = null;
	
	public List(uParty u){
		this.u=u;
	}
	
	public void give(ArrayList<Module> modules){
		this.modules=modules;
		
		for(int i = 0; i < this.modules.size(); i++){
			Module m = this.modules.get(i);
			m.bounds=new Rectangle(0,bounds.y+bounds.height-((modHeight + .015f)*(i+1))*u.h+scr,u.w,modHeight*u.h);
		}
		
	}
	
	public void move(float move){
		//System.out.println(move);
		if(Math.abs(move)>3){
		scrolled=true;}
		toggleOff();
		
			scr+=move;lastY+=move;
		
		
		if(modules.size()*((modHeight + .015f)*u.h) < bounds.height-(.02f*u.h) + scr){scr = (modules.size()*((modHeight + .015f)*u.h))-(bounds.height-(.02f*u.h));}
		
		if(scr<0){scr=0;}
		if(scr>(modules.size()-1)*(modHeight + .015f)*u.h){scr=(modules.size()-1)*(modHeight + .015f)*u.h;}
		//System.out.println(scr);
		//if(scr>modules.size()-1*.06f*u.h){scr=modules.size()-1*.06f*u.h;}
	}
	
	private void toggleOff() {
		for(int i = 0; i < this.modules.size(); i++){
			Module m = this.modules.get(i);
			m.toggleOff();
		}
		
	}

	public void draw(SpriteBatch pic, float fade){
		for(int i = 0; i < modules.size(); i++){
			if(Intersector.overlaps(bounds, modules.get(i).bounds)){
				if(bounds.height - modules.get(i).bounds.y <= (modHeight-.0475f)*u.h){
					float fde = (bounds.height-modules.get(i).bounds.y)/((modHeight-.0475f)*u.h);
					if(fde > 0){
					System.out.println(fde);
					if(modules.get(i).bounds.y >= bounds.y+bounds.height-((modHeight + .015f))*u.h ){
					modules.get(i).bounds=new Rectangle(0,bounds.y+bounds.height-((modHeight + .015f))*u.h ,u.w,modHeight*u.h);}
					modules.get(i).draw(pic,fde);}
				}
				else{
					modules.get(i).draw(pic,fade);
				}
			}
		}
	}

	public void touchDown(Rectangle touch) {
		lastY=touch.y;
		for(int i = 0; i < this.modules.size(); i++){
			Module m = this.modules.get(i);
			m.touchDown(touch);
		}
	}
	
	public void touchUp(Rectangle touch) {
		toggleOff();
		if(scrolled){scrolled=false;}
		else{
			for(int i = 0; i < this.modules.size(); i++){
				Module m = this.modules.get(i);
				m.touchUp(touch);
			}
		}
		
	}
	
}
