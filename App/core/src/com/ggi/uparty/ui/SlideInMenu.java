package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.screens.CreateEventScreen;
import com.ggi.uparty.screens.GroupScreen;
import com.ggi.uparty.screens.GroupSettingsScreen;
import com.ggi.uparty.screens.NewGroupScreen;
import com.ggi.uparty.screens.SettingsScreen;

public class SlideInMenu {

	public uParty u;
	
	public Rectangle bounds;
	
	public float theta = 0; //0 = close 1 = open;
	
	private Texture bg, darken;
	
	public boolean lvRef;
	
	public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public RadialSprite lvBar;
	
	public float lvAngle=0;
	
	public GlyphLayout layout;
	
	public TextButton newPost, groups, newGroup, settings;
	
	public Group g;
	
	public SlideInMenu(uParty u, Group g){
		this.g=g;
		this.u=u;
		bounds = new Rectangle(-.75f*u.w,0,.75f*u.w,u.h);
		
		bg = u.assets.get("UI/Background.png");
		darken = u.assets.get("UI/Darken.png");
		
		layout = new GlyphLayout();
		
		lvBar = new RadialSprite(new TextureRegion(u.assets.get("UI/CircleLoad.png",Texture.class)));
		
		newPost = makeMenuButton(new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/newUp.png",Texture.class))),new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/newDown.png", Texture.class))),"     New Post");
		groups = makeMenuButton(new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/groupUp.png",Texture.class))),new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/groupDown.png", Texture.class)))," Groups");
		newGroup = makeMenuButton(new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/newGroupUp.png",Texture.class))),new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/newGroupDown.png", Texture.class))),"        New Group");
		settings = makeMenuButton(new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/settingsUp.png",Texture.class))),new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Menu/Buttons/settingsDown.png", Texture.class))),"    Settings");
		
		newPost.padRight(u.w/4.5f);
		groups.padRight(u.w/4.5f);
		newGroup.padRight(u.w/4.5f);
		settings.padRight(u.w/4.5f);
		
	}
	
	public void draw(SpriteBatch pic,float fade){
		
		newPost.setBounds(bounds.x+theta*.75f*u.w, bounds.y+.7975f*bounds.height, bounds.width, .0625f*bounds.height);
		groups.setBounds(bounds.x+theta*.75f*u.w, bounds.y+.735f*bounds.height, bounds.width, .0625f*bounds.height);
		newGroup.setBounds(bounds.x+theta*.75f*u.w, bounds.y+.6725f*bounds.height, bounds.width, .0625f*bounds.height);
		settings.setBounds(bounds.x+theta*.75f*u.w, bounds.y+.61f*bounds.height, bounds.width, .0625f*bounds.height);
		
		//u.myAcc.xp=8;
		if(lvRef && theta >0 ){getLv();lvRef = false;}
		if(!lvRef && theta == 0){lvRef = true;lvAngle = 0;}
		
		float ratio = -(leftOverXP/neededToLvXP)*360;
		if(theta > 0 && lvAngle!=ratio){lvAngle+=(ratio-lvAngle)/32;}
		lvBar.setAngle(lvAngle);
		
		pic.setColor(1, 1, 1, theta);
		pic.draw(darken,0,0,u.w,u.h);
		pic.setColor(1, 1, 1, fade);
		pic.draw(bg,bounds.x+theta*.75f*u.w,bounds.y,bounds.width,bounds.height);
		pic.draw(u.assets.get("UI/Menu/Banner.png", Texture.class),bounds.x+theta*.75f*u.w,bounds.y+.86f*bounds.height,bounds.width,.14f*bounds.height);
		newPost.draw(pic, fade);
		groups.draw(pic, fade);
		newGroup.draw(pic, fade);
		settings.draw(pic, fade);
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		
		layout.setText(u.largeFnt, ""+lv);
		u.largeFnt.draw(pic, ""+lv,  bounds.x+theta*.75f*u.w+.05f*bounds.width+.12f*bounds.height/2-layout.width/2, bounds.y+.87f*bounds.height+.12f*bounds.height/2+layout.height/2);
		
		lvBar.setColor(new Color(1,1,1,fade));
		if(leftOverXP!=0&&lvAngle!=0){lvBar.draw(pic, bounds.x+theta*.75f*u.w+.05f*bounds.width,bounds.y+.87f*bounds.height,.12f*bounds.height,.12f*bounds.height);}
	
		u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		
		layout.setText(u.mediumFnt, u.myAcc.u);
		u.mediumFnt.draw(pic,u.myAcc.u,bounds.x+theta*.75f*u.w+.14f*bounds.height,bounds.y+.93f*bounds.height+layout.height/2);
		
		
	}
	
	private void getLv() {
		lv = 0;
		while(factorial(lv+1)*5<=u.myAcc.xp){
			lv++;
		}
		leftOverXP=u.myAcc.xp-factorial(lv)*5;
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
	
	public TextButton makeMenuButton(TextureRegionDrawable up, TextureRegionDrawable down, String txt){
		TextButtonStyle s = new TextButtonStyle();
			s.up = up;
			s.down = down;
			s.checked = down;
			s.font  =u.smallFnt;
			s.fontColor = u.orange;
			s.checkedFontColor = new Color(1,.8f,.2f,1);
			
		TextButton result = new TextButton(txt,s);
		return result;
	}

	public void tap(Rectangle touch) {
		newPost.setChecked(false);
		groups.setChecked(false);
		newGroup.setChecked(false);
		settings.setChecked(false);
		if(Intersector.overlaps(touch, new Rectangle(newPost.getX(),newPost.getY(),newPost.getWidth(),newPost.getHeight()))){
			u.nextScreen = new CreateEventScreen(u, g);
		}
		else if(Intersector.overlaps(touch, new Rectangle(groups.getX(),groups.getY(),groups.getWidth(),groups.getHeight()))){
			u.nextScreen = new GroupScreen(u);
		}
		else if(Intersector.overlaps(touch, new Rectangle(newGroup.getX(),newGroup.getY(),newGroup.getWidth(),newGroup.getHeight()))){
			u.nextScreen = new NewGroupScreen(u);
		}
		else if(Intersector.overlaps(touch, new Rectangle(settings.getX(),settings.getY(),settings.getWidth(),settings.getHeight()))){
			u.nextScreen = new SettingsScreen(u);
		}
		
	}

	public void down(Rectangle touch) {
		if(Intersector.overlaps(touch, new Rectangle(newPost.getX(),newPost.getY(),newPost.getWidth(),newPost.getHeight()))){
			newPost.setChecked(true);
		}
		else if(Intersector.overlaps(touch, new Rectangle(groups.getX(),groups.getY(),groups.getWidth(),groups.getHeight()))){
			groups.setChecked(true);
		}
		else if(Intersector.overlaps(touch, new Rectangle(newGroup.getX(),newGroup.getY(),newGroup.getWidth(),newGroup.getHeight()))){
			newGroup.setChecked(true);
		}
		else if(Intersector.overlaps(touch, new Rectangle(settings.getX(),settings.getY(),settings.getWidth(),settings.getHeight()))){
			settings.setChecked(true);
		}
		
	}
	
}
