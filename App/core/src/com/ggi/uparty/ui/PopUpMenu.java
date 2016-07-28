package com.ggi.uparty.ui;

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

public class PopUpMenu {

	public MainScreen s;
	
	public float slide=0;//-.7525f*s.u.h = closed
	
	public boolean open = false;
	
	public Texture background,darken;
	
	public Button slideButton;
	
	public TextButton post,logout;
	
	public Rectangle bounds,slideB,postB,logoutB;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public RadialSprite lvBar;
	
	public float lvAngle=0;
	
	public PopUpMenu(MainScreen s){
		this.s=s;
		background = s.u.assets.get("UI/PopUpMenu.png");
		darken = s.u.assets.get("UI/Darken.png");
		slide=-.7525f*s.u.h;
		bounds = new Rectangle(0,slide,s.u.w,.8f*s.u.h);
		slideB = new Rectangle(bounds.width/2-.048f*s.u.w,.97f*bounds.height+bounds.y-.012f*s.u.h,.096f*s.u.w,.024f*s.u.h);
		slideButton = new Button(s.u.slideStyle);
		slideButton.setBounds(slideB.x, slideB.y, slideB.width, slideB.height);
		lvBar = new RadialSprite(new TextureRegion(s.u.assets.get("UI/CircleLoad.png",Texture.class)));
		
		postB = new Rectangle(bounds.width/4,.52f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		logoutB = new Rectangle(bounds.width/4,.44f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		
		post = new TextButton("Post an Event",s.u.standardButtonStyle);
			post.setBounds(postB.x, postB.y, postB.width, postB.height);
		logout = new TextButton("Logout",s.u.redButtonStyle);
			logout.setBounds(logoutB.x, logoutB.y, logoutB.width, logoutB.height);
		
		
		s.u.myAcc.xp=9;
	}
	
	public void draw(SpriteBatch pic, float fade){
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
		pic.draw(background,bounds.x,bounds.y,bounds.width,bounds.height);
		slideButton.draw(pic, fade);
		
		s.u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(s.u.largeFnt, s.u.myAcc.u);
		s.u.largeFnt.draw(pic, s.u.myAcc.u, bounds.width/2-layout.width/2, .93f*bounds.height+bounds.y-layout.height/2);
		
		layout.setText(s.u.largeFnt, ""+lv);
		s.u.largeFnt.draw(pic, ""+lv, bounds.width/2-layout.width/2, .75f*bounds.height+bounds.y-layout.height/2);
		
		
		lvBar.setColor(new Color(1,1,1,fade));
		if(leftOverXP!=0&&lvAngle!=0){lvBar.draw(pic, bounds.width/2-s.u.w/8, .75f*bounds.height+bounds.y-layout.height-s.u.w/8,s.u.w/4,s.u.w/4);}
		
		postB = new Rectangle(bounds.width/4,.52f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		logoutB = new Rectangle(bounds.width/4,.44f*bounds.height+bounds.y,bounds.width/2,s.u.h/20);
		post.setBounds(postB.x, postB.y, postB.width, postB.height);
		logout.setBounds(logoutB.x, logoutB.y, logoutB.width, logoutB.height);
		
		post.draw(pic, fade);
		logout.draw(pic, fade);
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
		else if(Intersector.overlaps(touch, logoutB)){logout.toggle();}
		
	}

	public void touchUp(Rectangle touch) {
		toggleOff();
		if(Intersector.overlaps(touch, postB)){s.u.nextScreen=new CreateEventScreen(s.u);}
		else if(Intersector.overlaps(touch, logoutB)){s.u.myAcc=null;s.u.setScreen(new LoadScreen(s.u));}
		else if(!open){open=!open;}
		
	}

	private void toggleOff() {
		if(post.isChecked()){post.toggle();}
		if(logout.isChecked()){logout.toggle();}
		
	}

	
	
	
}
