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
import com.ggi.uparty.network.InviteNew;
import com.ggi.uparty.network.Refresh;

public class InviteNewScreen implements Screen, InputProcessor{

	public uParty u;
	
	public TextField user;
	
	public TextButton invite,back;
	
	public Rectangle userB,inviteB,backB;
	
	public Texture background;
	
	private SpriteBatch pic;
	
	public float fade = 0;
	
	public String g = "";
	
	public GlyphLayout layout = new GlyphLayout();
	
	public Stage stage;

	public boolean invited = false;

	private boolean goB = false;
	
	
	public InviteNewScreen(uParty u){
		this.u=u;
	}
	
	@Override
	public void show() {
		pic = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		background = u.assets.get("UI/Background.png");
		
		backB = new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		userB = new Rectangle(u.w/9,.5f*u.h,7*u.w/9,u.h/16);
		inviteB = new Rectangle(u.w/4,.42f*u.h,u.w/2,u.h/16);
		
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		
		invite = new TextButton("Invite",u.standardButtonStyle);
			invite.setBounds(inviteB.x, inviteB.y, inviteB.width, inviteB.height);
		
		user = new TextField(g,u.textFieldStyle);
			user.setBounds(userB.x, userB.y, userB.width, userB.height);
			user.setAlignment(Align.center);
			user.setMessageText("Email to Invite");
		
		stage = new Stage();
		stage.addActor(user);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		
		back.draw(pic, fade);
		invite.draw(pic, fade);
		user.draw(pic, fade);
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.largeFnt, "Invite Someone to uParty");
		u.largeFnt.draw(pic, "Invite Someone to uParty", u.w/2-layout.width/2, .7f*u.h-layout.height/2);
		
		
		pic.end();
		
		
		
		if((u.nextScreen==null && !goB)&&fade<1f){fade+=(1-fade)/2;}
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
		if(stage.getKeyboardFocus().equals(user)){if(character == ''&&g.length()>0){
			g=g.substring(0, g.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			g+=character;
		}}
		
		g=g.replaceAll("\\p{Cntrl}","");
		g=g.replaceAll("\\s+","");
		
		user.setText(g);
		
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		else if(Intersector.overlaps(touch, inviteB)){invite.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){goB = true;}
		else if(Intersector.overlaps(touch, inviteB)){
			InviteNew i = new InviteNew();
			i.e=g;
			
			u.send(i);
			u.nextScreen = new SettingsScreen(u);
		}
		else if(Intersector.overlaps(touch, userB)){stage.setKeyboardFocus(user);Gdx.input.setOnscreenKeyboardVisible(true);}
		
		return true;
	}
	
	private void toggleOff(){
		Gdx.input.setOnscreenKeyboardVisible(false);
		stage.setKeyboardFocus(null);
		if(back.isChecked()){back.toggle();}
		if(invite.isChecked()){invite.toggle();}
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
