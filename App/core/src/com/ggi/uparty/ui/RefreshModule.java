package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.screens.MainScreen;

public class RefreshModule {

	public MainScreen s;
	
	public Rectangle bounds = new Rectangle();
	
	public Texture icon;
	
	public TextureRegion load;
	
	public int theta = 0;
	
	public RefreshModule(MainScreen s){
		this.s=s;
		icon=s.u.assets.get("Logos/1024.png",Texture.class);
		load=new TextureRegion(s.u.assets.get("UI/Load.png",Texture.class));
	}
	
	public void draw(SpriteBatch pic, float fade){
		theta-=2;
		pic.draw(icon,bounds.width/2-bounds.height/4,bounds.y+bounds.height/4,bounds.height/2,bounds.height/2);
		if(s.events.refresh){
			pic.draw(load, bounds.width/2-bounds.height/4, bounds.y+bounds.height/4, bounds.height/4, bounds.height/4, bounds.height/2, bounds.height/2, 1, 1, theta);
		}
	}
	
}
