package com.ggi.uparty.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.uParty;
import com.ggi.uparty.ui.GroupModule;
import com.ggi.uparty.ui.List;
import com.ggi.uparty.ui.Module;

public class GroupScreen implements Screen, GestureListener{

	public uParty u;
	
	public List list;
	
	public Texture background;
	
	public SpriteBatch pic;

	private Rectangle backB;

	private TextButton back;
	
	private float fade = 0;

	private boolean goB = false;
	
	public ArrayList<Module> modules = new ArrayList<Module>();

	private GlyphLayout layout = new GlyphLayout();
	
	public GroupScreen(uParty u){
		this.u = u;
		background = u.assets.get("UI/Background.png");
		
		
		backB = new Rectangle(u.w/36,.93f*u.h,.2f*u.w,.05f*u.h);
		back = new TextButton("Back",u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);
		
		list = new List(u);
		list.modHeight = .05f;
		list.bounds = new Rectangle(0,0,u.w,.91f*u.h);
		genModules();
		list.give(modules);
		
	}
	
	private void genModules() {
		modules.add(new GroupModule(list,null));
		for(int i = 0; i < u.myAcc.groups.size(); i++){
			GroupModule gm = new GroupModule(list, u.myAcc.groups.get(i));
			modules.add(gm);
		}
		
	}

	@Override
	public void show() {
		pic = new SpriteBatch();
		
		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		pic.begin();
		pic.draw(background,0,0,u.w,u.h);
		back.draw(pic, fade);
		list.draw(pic, fade);
		
		u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout .setText(u.mediumFnt,"Groups");
		u.mediumFnt.draw(pic,"Groups",u.w/2 - layout.width/2,.955f*u.h+layout.height/2);
		
		pic.end();
		
		
		if((u.nextScreen==null && !goB )&&fade<1f){fade+=(1-fade)/2;}
		else if((u.nextScreen!=null || goB)&&fade>.1f){fade+=(0-fade)/2;}
		else if(u.nextScreen!=null){u.setScreen(u.nextScreen);}
		else if(goB){u.goBack();}
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
	public boolean touchDown(float x, float y, int pointer, int button) {
		y = u.h-y;
		Rectangle touch = new Rectangle(x,y,1,1);
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		for(int i = 0; i < modules.size(); i++){
			GroupModule gm = (GroupModule) modules.get(i);
			if(Intersector.overlaps(touch, gm.settingsB)){
				gm.settings.toggle();
			}
		}
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		y = u.h-y;
		Rectangle touch = new Rectangle(x,y,1,1);
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){goB=true;}
		for(int i = 0; i < modules.size(); i++){
			GroupModule gm = (GroupModule) modules.get(i);
			if(Intersector.overlaps(touch, gm.settingsB)){
				u.nextScreen = new GroupSettingsScreen(u,gm.group);
			}
			else if(Intersector.overlaps(touch, gm.bounds)){
				MainScreen s = new MainScreen(u);
				s.g=gm.group;
				u.nextScreen = s;
			}
		}
		
		return true;
	}

	private void toggleOff() {
		if(back.isChecked()){back.toggle();}
		
		for(int i = 0; i < modules.size(); i++){
			GroupModule gm = (GroupModule) modules.get(i);
			if(gm.settings.isChecked()){
				gm.settings.toggle();
			}
		}
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (Math.abs(deltaY) > Math.abs(deltaX)) {
				list.move(-deltaY);
		} else if (Math.abs(deltaY) < Math.abs(deltaX)) {
			//toolbar.pan(deltaX);
		}
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}

}
