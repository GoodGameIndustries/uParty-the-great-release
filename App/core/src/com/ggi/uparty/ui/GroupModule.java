package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.screens.GroupSettingsScreen;
import com.ggi.uparty.screens.MainScreen;

public class GroupModule extends Module{

	public Group group;
	
	public List l;
	
	public Texture background;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public TextButton settings;
	
	public Rectangle settingsB = new Rectangle();
	
	public GroupModule(List l,Group group) {
		this.l=l;
		this.group=group;
		background = l.u.assets.get("UI/EventModule.png");
		
		settings = new TextButton("Settings",l.u.standardButtonStyle);
			settings.setBounds(settingsB.x, settingsB.y, settingsB.width, settingsB.height);
	}
	
	public void draw(SpriteBatch pic, float fade){
		settingsB = new Rectangle(bounds.x+.75f*bounds.width,bounds.y,.25f*bounds.width,bounds.height);
		settings.setBounds(settingsB.x, settingsB.y, settingsB.width, settingsB.height);
		pic.setColor(1, 1, 1, fade);
		pic.draw(background,bounds.x,bounds.y,bounds.width,bounds.height);
		
		l.u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(l.u.mediumFnt, group!=null?group.name:"Public");
		l.u.mediumFnt.draw(pic, group!=null?group.name:"Public", bounds.x+.05f*bounds.width, bounds.y+bounds.height/2+layout.height/2);
		
		if(group!=null){settings.draw(pic, fade);}
		
	}
	
	public void toggleOff(){
		if(settings.isChecked()){settings.toggle();}
	}
	
	public void touchDown(Rectangle touch){
		if(group!=null&&Intersector.overlaps(touch, settingsB)){settings.toggle();}
	}
	
	public void touchUp(Rectangle touch){
		if(group!=null&&Intersector.overlaps(touch, settingsB)){l.u.nextScreen=new GroupSettingsScreen(l.u,group);}
		else if(Intersector.overlaps(touch, bounds)){
			MainScreen s = new MainScreen(l.u);
			if(group!=null){s.g=group;}
			l.u.nextScreen=s;}
		
	}

}
