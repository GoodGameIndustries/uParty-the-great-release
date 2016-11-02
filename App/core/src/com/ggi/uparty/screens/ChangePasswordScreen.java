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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.ChangePass;
import com.ggi.uparty.network.Refresh;

public class ChangePasswordScreen implements Screen, InputProcessor{

	public uParty u;
	
	public TextField pass,npass,cnpass;
	
	public TextButton change,back;
	
	public Rectangle passB,npassB,cnpassB,changeB,backB;
	
	public Texture background;
	
	private SpriteBatch pic;
	
	public float fade = 0;
	
	public String g = "",n="",cn="";
	
	public GlyphLayout layout = new GlyphLayout();
	
	public Stage stage;

	public boolean changed = false;
	
	
	
	public ChangePasswordScreen(uParty u){
		this.u=u;
	}
	
	@Override
	public void show() {
		pic = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		background = u.assets.get("UI/Background.png");
		
		backB = new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		passB = new Rectangle(u.w/9,.5f*u.h,7*u.w/9,u.h/16);
		npassB = new Rectangle(u.w/9,.42f*u.h,7*u.w/9,u.h/16);
		cnpassB = new Rectangle(u.w/9,.34f*u.h,7*u.w/9,u.h/16);
		changeB = new Rectangle(u.w/4,.26f*u.h,u.w/2,u.h/16);
		
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		
		change = new TextButton("Change Password",u.standardButtonStyle);
			change.setBounds(changeB.x, changeB.y, changeB.width, changeB.height);
		
		pass = new TextField(g,u.textFieldStyle);
			pass.setBounds(passB.x, passB.y, passB.width, passB.height);
			pass.setAlignment(Align.center);
			pass.setPasswordMode(true);
			pass.setPasswordCharacter('*');
			pass.setMessageText("Old Password");
			
		npass = new TextField(g,u.textFieldStyle);
			npass.setBounds(npassB.x, npassB.y, npassB.width, npassB.height);
			npass.setAlignment(Align.center);
			npass.setPasswordMode(true);
			npass.setPasswordCharacter('*');
			npass.setMessageText("New Password");
			
		cnpass = new TextField(g,u.textFieldStyle);
			cnpass.setBounds(cnpassB.x, cnpassB.y, cnpassB.width, cnpassB.height);
			cnpass.setAlignment(Align.center);
			cnpass.setPasswordMode(true);
			cnpass.setPasswordCharacter('*');
			cnpass.setMessageText("Confirm New Password");
		
		stage = new Stage();
		stage.addActor(pass);
		stage.addActor(npass);
		stage.addActor(cnpass);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		
		back.draw(pic, fade);
		change.draw(pic, fade);
		pass.draw(pic, fade);
		npass.draw(pic, fade);
		cnpass.draw(pic, fade);
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.largeFnt, "Change Your Password");
		u.largeFnt.draw(pic, "Change Your Password", u.w/2-layout.width/2, .7f*u.h-layout.height/2);
		
		u.smallFnt.setColor(1,0,0,fade);
		layout.setText(u.smallFnt, u.error);
		u.smallFnt.draw(pic, u.error, u.w/2-layout.width/2, .2f*u.h-layout.height/2);
		
		
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
		if(stage.getKeyboardFocus().equals(pass)){if(character == ''&&g.length()>0){
			g=g.substring(0, g.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			g+=character;
		}}
		
		else if(stage.getKeyboardFocus().equals(npass)){if(character == ''&&n.length()>0){
			n=n.substring(0, n.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			n+=character;
		}}
		
		else if(stage.getKeyboardFocus().equals(cnpass)){if(character == ''&&cn.length()>0){
			cn=cn.substring(0, cn.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			cn+=character;
		}}
		
		g=g.replaceAll("\\p{Cntrl}","");
		g=g.replaceAll("\\s+","");
		
		n=n.replaceAll("\\p{Cntrl}","");
		n=n.replaceAll("\\s+","");
		
		cn=cn.replaceAll("\\p{Cntrl}","");
		cn=cn.replaceAll("\\s+","");
		
		pass.setText(g);
		npass.setText(n);
		cnpass.setText(cn);
		
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		else if(Intersector.overlaps(touch, changeB)){change.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){u.nextScreen =(new SettingsScreen(u));}
		else if(Intersector.overlaps(touch, changeB)){
			if(!n.equals(cn)){
				u.error="New passwords do not match.";
			}
			else if(!g.equals(u.myAcc.p)){
				u.error="Your old password was not correct.";
			}
			else{
				ChangePass ch = new ChangePass();
				ch.e=u.myAcc.e;
				ch.p=n;
				
				u.send(ch);
				u.setScreen(new SettingsScreen(u));
			}
			
		}
		else if(Intersector.overlaps(touch, passB)){stage.setKeyboardFocus(pass);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, npassB)){stage.setKeyboardFocus(npass);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, cnpassB)){stage.setKeyboardFocus(cnpass);Gdx.input.setOnscreenKeyboardVisible(true);}
		
		return true;
	}
	
	private void toggleOff(){
		Gdx.input.setOnscreenKeyboardVisible(false);
		stage.setKeyboardFocus(null);
		if(back.isChecked()){back.toggle();}
		if(change.isChecked()){change.toggle();}
	}
	
	public void refresh(){
		Refresh r = new Refresh();
		r.e=u.myAcc.e;
		r.lat=u.controller.getLat();r.lng=u.controller.getLong();
		u.send(r);
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

