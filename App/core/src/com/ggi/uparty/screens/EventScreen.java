package com.ggi.uparty.screens;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Report;
import com.ggi.uparty.network.UpVote;
import com.ggi.uparty.ui.RadialSprite;

public class EventScreen implements Screen, InputProcessor{

	public uParty u;
	
	public Event e;
	
	public Texture background,up,upC,down,downC;;
	
	public SpriteBatch pic;
	
	public float fade = 0;
	
	public TextArea info;
	
	public TextButton back,report;
	
	public String i = "";
	
	public Rectangle infoB,backB,upB,downB,reportB;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public float iHeight = 0;
	
    public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public RadialSprite lvBar;
	
	public float lvAngle=0;
	
	public Group g;
	
	public EventScreen(uParty u, Event e, Group g){
		this.u=u;
		this.e=e;
		this.g=g;
		
	
	}
	
	@Override
	public void show() {
		pic = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		i="Start: "+dateToString(e.start)+"\nEnd: "+dateToString(e.end)+"\n\nDescription: "+e.description+"\n\nLocation: "+e.location;
		layout.setText(u.smallFnt, i);
		iHeight=layout.height;
		backB = new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		infoB = new Rectangle(.05f*u.w,.45f*u.h,.6f*u.w,.4f*u.h);
		upB=new Rectangle(.85f*u.w-.048f*u.w,.85f*u.h-iHeight/2+.05f*u.h-.012f*u.h,.096f*u.w,.024f*u.h);
		downB=new Rectangle(.85f*u.w-.048f*u.w,.85f*u.h-iHeight/2-.05f*u.h-.012f*u.h,.096f*u.w,.024f*u.h);
		reportB = new Rectangle(2*u.w/9,.11f*u.h,5*u.w/9,u.h/16);
		
		background = u.assets.get("UI/Background.png");
		up=u.assets.get("UI/SlideUp.png");
		upC=u.assets.get("UI/SlideUpChecked.png");
		down=u.assets.get("UI/SlideDown.png");
		downC=u.assets.get("UI/SlideDownChecked.png");
		
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		report = new TextButton("Report",u.redButtonStyle);
			report.setBounds(reportB.x, reportB.y, reportB.width, reportB.height);
		info = new TextArea(i,u.textAreaStyle);
			info.setAlignment(Align.left);
			info.setBounds(infoB.x, infoB.y, infoB.width, infoB.height);
		
		lvBar = new RadialSprite(new TextureRegion(u.assets.get("UI/CircleLoad.png",Texture.class)));	
		
		getLv();
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		
		back.draw(pic, fade);
		info.draw(pic, fade);
		
		pic.setColor(1, 1, 1, fade);
		pic.draw(e.upVote.contains(u.myAcc.e)?upC:up,upB.x,upB.y,upB.width,upB.height);
		pic.draw(e.downVote.contains(u.myAcc.e)?downC:down,downB.x,downB.y,downB.width,downB.height);
		
		u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.mediumFnt,e.name);
		u.mediumFnt.draw(pic,e.name,.05f*u.w,.87f*u.h+layout.height/2);
		layout.setText(u.mediumFnt,""+(e.upVote.size()-e.downVote.size()));
		u.mediumFnt.draw(pic,""+(e.upVote.size()-e.downVote.size()),.85f*u.w-layout.width/2,.85f*u.h-iHeight/2+layout.height/2);
		
		u.smallFnt.setColor(1,1,1,fade);

		layout.setText(u.smallFnt,"Total Votes: "+(e.upVote.size()+e.downVote.size()));
		u.smallFnt.draw(pic,"Total Votes: "+(e.upVote.size()+e.downVote.size()), u.w/2-layout.width/2, .42f*u.h+layout.height/2);
		layout.setText(u.smallFnt,"Posted by:");
		u.smallFnt.draw(pic,"Posted by:", u.w/2-layout.width/2, .415f*u.h-layout.height/2);
		
		getLv();
		float ratio = -(leftOverXP/neededToLvXP)*360;
		if(lvAngle!=ratio){lvAngle+=(ratio-lvAngle)/32;}
		lvBar.setAngle(lvAngle);
		
		lvBar.setColor(new Color(1,1,1,fade));
		if(leftOverXP!=0&&lvAngle!=0){lvBar.draw(pic, u.w/2-u.w/8, .3f*u.h-u.w/8,u.w/4,u.w/4);}
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.largeFnt, ""+lv);
		u.largeFnt.draw(pic, ""+lv, u.w/2-layout.width/2, .3f*u.h+layout.height/2);
		
		report.draw(pic, 1);
		
		pic.end();
		
		if(u.nextScreen==null&&fade<1f){fade+=(1-fade)/2;}
		else if(u.nextScreen!=null&&fade>.1f){fade+=(0-fade)/2;}
		else if(u.nextScreen!=null){u.setScreen(u.nextScreen);}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private String dateToString(Date d) {
		String result = d.getMonth()+1+"/"+d.getDate()+"/"+(d.getYear()+1900);
		int hour = -1;
		boolean isPm=false;
		if(d.getHours()>=12){isPm=true;}
		hour=d.getHours()%12;
		if(hour==0){hour=12;}
		result+="   "+hour+":"+String.format("%02d", d.getMinutes())+(isPm?" PM":" AM");
		
		return result;
	}
	
	private void getLv() {
		lv = 0;
		while(factorial(lv+1)*5<=e.ownerXp){
			lv++;
		}
		leftOverXP=e.ownerXp-factorial(lv)*5;
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		else if(Intersector.overlaps(touch, reportB)){report.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		if(Intersector.overlaps(touch, backB)){u.nextScreen=new MainScreen(u);}
		else if(Intersector.overlaps(touch, reportB)){
			Report r = new Report();
			r.ID=e.ID;
			r.lat=e.lat;
			r.e=u.myAcc.e;
			r.group=e.group;
			u.send(r);
			MainScreen s = new MainScreen(u);
			s.g=g;
			
			u.nextScreen=s;
			
		}
		else if(Intersector.overlaps(touch, upB)){
			if(e.downVote.contains(u.myAcc.e)){e.downVote.remove(u.myAcc.e);}
			if(!e.upVote.contains(u.myAcc.e)){e.upVote.add(u.myAcc.e);}
			UpVote o= new UpVote();
			o.e=u.myAcc.e;
			o.ID=e.ID;
			o.lng=e.lng;o.lat=e.lat;
			u.send(o);
		}
		else if(Intersector.overlaps(touch, downB)){
			if(!e.downVote.contains(u.myAcc.e)){e.downVote.add(u.myAcc.e);}
			if(e.upVote.contains(u.myAcc.e)){e.upVote.remove(u.myAcc.e);}
			DownVote o= new DownVote();
			o.e=u.myAcc.e;
			o.ID=e.ID;
			o.lng=e.lng;o.lat=e.lat;
			u.send(o);
		}
		return true;
	}
	
	private void toggleOff(){
		if(report.isChecked()){report.toggle();}
		if(back.isChecked()){back.toggle();}
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
