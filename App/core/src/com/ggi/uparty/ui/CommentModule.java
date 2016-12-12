package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;

public class CommentModule extends Module{

	public List l;
	public long xp;
	public String com;
	
	public Texture bg;
	
	public TextArea a;
	
	public RadialSprite lvBar;
	
	public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public float lvAngle=0;
	
	public GlyphLayout layout;
	
	public CommentModule(List l, String c){
		super();
		this.l=l;
		this.xp=Long.parseLong(c.split(":")[0]);
		this.com=c.split(":")[1];
		bg=l.u.assets.get("UI/EventModule.png", Texture.class);
		
		a = new TextArea(com,l.u.viewAreaStyle);
		a.setAlignment(Align.left);
		
		lvBar = new RadialSprite(new TextureRegion(l.u.assets.get("UI/CircleLoad.png",Texture.class)));
		
		getLv();
		
		layout = new GlyphLayout();
		layout.setText(l.u.supersmallFnt, ""+lv);
	}
	
	public void draw(SpriteBatch pic, float fade){
		pic.setColor(1, 1, 1, fade);
		pic.draw(bg,bounds.x,bounds.y,bounds.width,bounds.height);
		a.setBounds(.05f*bounds.width+bounds.height, bounds.y+.1f*bounds.height, .9f*bounds.width-bounds.height, .8f*bounds.height);
		a.draw(pic, fade);
		
		float ratio = -(leftOverXP/neededToLvXP)*360;
		if(lvAngle!=ratio){lvAngle+=(ratio-lvAngle)/32;}
		lvBar.setAngle(lvAngle);
		
		lvBar.setColor(new Color(1,1,1,fade));
		if(leftOverXP!=0&&lvAngle!=0){lvBar.draw(pic, .05f*bounds.width,bounds.y+.05f*bounds.height,.9f*bounds.height,.9f*bounds.height);}
		
		l.u.supersmallFnt.setColor(1, 1, 1, fade);
		l.u.supersmallFnt.draw(pic, ""+lv,.05f*bounds.width+.9f*bounds.height/2-layout.width/2,bounds.y+bounds.height/2+layout.height/2);
	}
	
	private void getLv() {
		lv = 0;
		while(factorial(lv+1)*5<=xp){
			lv++;
		}
		leftOverXP=xp-factorial(lv)*5;
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
}
