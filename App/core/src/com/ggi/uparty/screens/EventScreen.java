package com.ggi.uparty.screens;

import java.util.ArrayList;
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
import com.ggi.uparty.ui.CommentModule;
import com.ggi.uparty.ui.List;
import com.ggi.uparty.ui.Module;
import com.ggi.uparty.ui.RadialSprite;

public class EventScreen implements Screen, InputProcessor{

	public uParty u;
	
	public Event e;
	
	public Texture background,up,upC,down,downC;;
	
	public SpriteBatch pic;
	
	public float fade = 0;
	
	public TextArea info;
	
	public TextButton back,report,reply;
	
	public String i = "";
	
	public Rectangle infoB,backB,upB,downB,reportB,replyB;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public float iHeight = 0;
	
    public long lv=0;
	
	public float leftOverXP=1,neededToLvXP=1;
	
	public RadialSprite lvBar;
	
	public float lvAngle=0;
	
	public Group g;
	
	public List list;

	public ArrayList<Module> modules = new ArrayList<Module>();

	private boolean initTouch;
	
	public EventScreen(uParty u, Event e, Group g){
		this.u=u;
		this.e=e;
		this.g=g;
		
		list = new List(u);
		
		genModules();
	}
	
	private void genModules() {
		for(int i = 0; i < e.comments.size(); i++){
			modules.add(new CommentModule(list,e.comments.get(i)));
		}
		
	}

	@Override
	public void show() {
		pic = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		getLv();
		
		i="> Start: "+dateToString(e.start)+"\n> End: "+dateToString(e.end)+"\n> Description: "+e.description+"\n> Location: "+e.location + "\n> Total Votes: "+(e.upVote.size()+e.downVote.size())+"\n> Posted by: lv " + lv;
		layout.setText(u.supersmallFnt, i);
		iHeight=layout.height;
		backB = new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		infoB = new Rectangle(.05f*u.w,.89f*u.h-2f*iHeight,.6f*u.w,2f*iHeight);
		upB=new Rectangle(.9f*u.w-.048f*u.w,.85f*u.h-.8f*iHeight/2+.05f*u.h-.012f*u.h,.096f*u.w,.024f*u.h);
		downB=new Rectangle(.9f*u.w-.048f*u.w,.85f*u.h-.8f*iHeight/2-.05f*u.h-.012f*u.h,.096f*u.w,.024f*u.h);
		reportB = new Rectangle(u.w-.15f*u.w-u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		replyB = new Rectangle(0, 0, u.w, u.h/16);
		
		background = u.assets.get("UI/Background.png");
		up=u.assets.get("UI/SlideUp.png");
		upC=u.assets.get("UI/SlideUpChecked.png");
		down=u.assets.get("UI/SlideDown.png");
		downC=u.assets.get("UI/SlideDownChecked.png");
		
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		report = new TextButton("Report",u.redButtonStyle);
			report.setBounds(reportB.x, reportB.y, reportB.width, reportB.height);
		reply = new TextButton("Tap to Reply",u.standardButtonStyle);
			reply.setBounds(replyB.x, replyB.y, replyB.width, replyB.height);
		info = new TextArea(i,u.viewAreaStyle);
			info.setAlignment(Align.left);
			info.setBounds(infoB.x, infoB.y, infoB.width, infoB.height);
			
		
		lvBar = new RadialSprite(new TextureRegion(u.assets.get("UI/CircleLoad.png",Texture.class)));	
		
		list.bounds=new Rectangle(0,.0625f*u.h,u.w,.89f*u.h-2.5f*iHeight-.0625f*u.h);
		
		getLv();
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		list.give(modules);
		
		pic.begin();
		
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		list.draw(pic, fade);
		pic.draw(u.assets.get("UI/EventModule.png",Texture.class),0,.89f*u.h-2.5f*iHeight,u.w,u.h-.89f*u.h+2.5f*iHeight);
		
		back.draw(pic, fade);
		info.draw(pic, fade);
		reply.draw(pic, fade);
		
		
		pic.setColor(1, 1, 1, fade);
		pic.draw(e.upVote.contains(u.myAcc.e)?upC:up,upB.x,upB.y,upB.width,upB.height);
		pic.draw(e.downVote.contains(u.myAcc.e)?downC:down,downB.x,downB.y,downB.width,downB.height);
		
		u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.mediumFnt,e.name);
		u.mediumFnt.draw(pic,e.name,.05f*u.w,.91f*u.h+layout.height/2);
		layout.setText(u.mediumFnt,""+(e.upVote.size()-e.downVote.size()));
		u.mediumFnt.draw(pic,""+(e.upVote.size()-e.downVote.size()),.9f*u.w-layout.width/2,.85f*u.h-.8f*iHeight/2+layout.height/2);
		
		/*
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
		*/
		report.draw(pic, fade);
		
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
		else if(Intersector.overlaps(touch, replyB)){reply.toggle();}
		if(Intersector.overlaps(touch, list.bounds)){list.touchDown(touch);initTouch=true;}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		if(Intersector.overlaps(touch, backB)){
			MainScreen s = new MainScreen(u);
			s.g=g;
			
			u.nextScreen=s;
		}
		else if(Intersector.overlaps(touch, reportB)){
			Report r = new Report();
			r.ID=e.ID;
			r.lat=e.lat;
			r.lng=e.lng;
			r.e=u.myAcc.e;
			r.group=e.group;
			u.send(r);
			System.out.println("Report sent");
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
		else if(Intersector.overlaps(touch, replyB)){u.nextScreen=new CommentScreen(u,e,g);}
		
		if(Intersector.overlaps(touch, list.bounds)){list.touchUp(touch);}
		initTouch=false;
		return true;
	}
	
	private void toggleOff(){
		if(report.isChecked()){report.toggle();}
		if(back.isChecked()){back.toggle();}
		if(reply.isChecked()){reply.toggle();}
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenY = (int) (u.h-screenY);
		if(initTouch){
			list.move(screenY-list.lastY);
		}
		return true;
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
