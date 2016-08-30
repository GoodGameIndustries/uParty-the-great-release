package com.ggi.uparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.DeleteGroup;
import com.ggi.uparty.network.Group;

public class SettingsScreen implements Screen,InputProcessor {

	public uParty u;
	
	public Group g;
	
	public float fade = 0;
	
	public SpriteBatch pic;
	
	public Texture background;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public Rectangle changeUB,changePB,inviteB,reviewB,backB,logoutB;
	
	public TextButton changeU,changeP,invite,review,back,logout;
	
	public SettingsScreen(uParty u){
		this.u=u;
	}

	@Override
	public void show() {
		pic = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		background = u.assets.get("UI/Background.png");
		
		backB=new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		changeUB=new Rectangle(u.w/9,.6f*u.h,7*u.w/9,u.h/16);
		changePB=new Rectangle(u.w/9,.52f*u.h,7*u.w/9,u.h/16);
		reviewB=new Rectangle(u.w/9,.44f*u.h,7*u.w/9,u.h/16);
		inviteB=new Rectangle(u.w/9,.36f*u.h,7*u.w/9,u.h/16);
		logoutB =new Rectangle(u.w/9,.28f*u.h,7*u.w/9,u.h/16);
		
		back = new TextButton("Back",u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);
		
		changeU = new TextButton("Change Username",u.standardButtonStyle);
		changeU.setBounds(changeUB.x, changeUB.y, changeUB.width, changeUB.height);
		
		changeP = new TextButton("Change Password",u.standardButtonStyle);
		changeP.setBounds(changePB.x, changePB.y, changePB.width, changePB.height);
		
		review = new TextButton("Rate Us",u.standardButtonStyle);
		review.setBounds(reviewB.x, reviewB.y, reviewB.width, reviewB.height);
		
		invite = new TextButton("Invite Someone",u.standardButtonStyle);
		invite.setBounds(inviteB.x, inviteB.y, inviteB.width, inviteB.height);
		
		logout = new TextButton("Logout",u.redButtonStyle);
		logout.setBounds(logoutB.x, logoutB.y, logoutB.width, logoutB.height);
		
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.largeFnt, "Settings");
		u.largeFnt.draw(pic, "Settings", u.w/2-layout.width/2, .85f*u.h-layout.height/2);
		
		back.draw(pic, fade);
		changeU.draw(pic, fade);
		changeP.draw(pic, fade);
		review.draw(pic, fade);
		invite.draw(pic, fade);
		logout.draw(pic, fade);
		
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
		else if(Intersector.overlaps(touch, changeUB)){changeU.toggle();}
		else if(Intersector.overlaps(touch, changePB)){changeP.toggle();}
		else if(Intersector.overlaps(touch, inviteB)){invite.toggle();}
		else if(Intersector.overlaps(touch, reviewB)){review.toggle();}
		else if(Intersector.overlaps(touch, logoutB)){logout.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){u.setScreen(new MainScreen(u));}
		else if(Intersector.overlaps(touch, changeUB)){}
		else if(Intersector.overlaps(touch, changePB)){}
		else if(Intersector.overlaps(touch, inviteB)){}
		else if(Intersector.overlaps(touch, reviewB)){Gdx.net.openURI("www.uaprtyapp.com/redirect");}
		else if(Intersector.overlaps(touch, logoutB)){u.myAcc=null;u.logout=true;u.setScreen(new LoadScreen(u));}
		
		return true;
	}

	private void toggleOff() {
		if(back.isChecked()){back.toggle();}
		if(changeU.isChecked()){changeU.toggle();}
		if(changeP.isChecked()){changeP.toggle();}
		if(invite.isChecked()){invite.toggle();}
		if(review.isChecked()){review.toggle();}
		if(logout.isChecked()){logout.toggle();}
		
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

