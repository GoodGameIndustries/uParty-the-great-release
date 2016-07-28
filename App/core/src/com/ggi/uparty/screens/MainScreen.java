package com.ggi.uparty.screens;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.ui.EventList;
import com.ggi.uparty.ui.PopUpMenu;
import com.ggi.uparty.ui.Toolbar;

public class MainScreen implements Screen, InputProcessor{

	public uParty u;
	
	public Texture background;
	
	public SpriteBatch pic;
	
	public Toolbar toolbar;
	
	public PopUpMenu menu;
	
	public EventList events;
	
	public float fade=0;
	
	public int lastY;
	
	public Rectangle touchDown;
	
	public MainScreen(uParty u){
		this.u=u;
		toolbar = new Toolbar(this);
		menu = new PopUpMenu(this);
		events = new EventList(this);
	}
	
	@Override
	public void show() {
		
		pic = new SpriteBatch();
		background = u.assets.get("UI/Background.png");
		Gdx.input.setInputProcessor(this);
		
		events.refresh=true;refresh();
		/**
		ArrayList<Event> tests = new ArrayList<Event>();
			Event e = new Event(u.controller.getLong(),u.controller.getLat(),"Test","As far as I am concerned, any 3rd party candidate running under the “Tea Party” label is a wacko or Dem stalking horse.","As far as I am concerned, any 3rd party candidate running under the “Tea Party” label is a wacko or Dem stalking horse.",new Date(),new Date(),u.myAcc);
			e.upVote.add(u.myAcc);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			tests.add(e);
			//tests.add(e);
			events.giveEvents(tests);**/
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(u.needUpdate){
			events.giveEvents(u.events);
			u.needUpdate=false;
		}
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		events.draw(pic, fade);
		toolbar.draw(pic, fade);
		menu.draw(pic, fade);	
		pic.end();
		
		if(u.nextScreen==null&&fade<1f){fade+=(1-fade)/2;}
		else if(u.nextScreen!=null&&fade>.1f){menu.open=false;fade+=(0-fade)/2;}
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
		screenY=(int) (u.h-screenY);
		lastY=screenY;
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		touchDown=touch;
		if(touch.overlaps(menu.bounds)){menu.touchDown(touch);}
		else if(touch.overlaps(toolbar.bounds)){toolbar.touchDown(touch);}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		lastY=0;
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		events.touch(touchDown,touch);
		
		if(touch.overlaps(menu.bounds)){menu.touchUp(touch);}
		else if(!touch.overlaps(menu.bounds)&&menu.open){menu.open=!menu.open;}
		else if(touch.overlaps(toolbar.bounds)){toolbar.touchUp(touch);}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenY=(int) (u.h-screenY);
		events.scrolled+=screenY-lastY;
		if(Math.abs(screenY-lastY)>50){events.momentum =screenY-lastY;}else{events.momentum=0;}
		lastY=screenY;
		if(events.scrolled>.095f*u.h){events.scrolled=(int) (-.095f*u.h);events.focus++;}
		if(events.scrolled<-.095f*u.h){events.scrolled=(int) (.095f*u.h);events.focus--;}
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

	
	public void refresh(){
		Refresh r = new Refresh();
		r.e=u.myAcc.e;
		r.lat=u.controller.getLat();r.lng=u.controller.getLong();
		u.send(r);
	}
}
