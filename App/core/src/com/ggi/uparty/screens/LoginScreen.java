/**
 * 
 */
package com.ggi.uparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Login;

/**
 * @author Emmett
 *
 */
public class LoginScreen implements Screen, InputProcessor {

	public uParty u;
	
	public SpriteBatch pic;
	
	public float logoMoveAnim = 0,fade = 0;
	
	public Texture logo,background;
	
	public TextField email,pass;
	
	public String e="",p="";
	
	public TextButton login,back,forgot,error;
	
	public CheckBox remember;
	
	public Rectangle emailB,passB,loginB,backB,forgotB,rememberB,errorB;

	private GlyphLayout layout = new GlyphLayout();
	
	public Stage stage = new Stage();
	
	
	public LoginScreen(uParty u){
		this.u=u;
		u.nextScreen=null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		u.nextScreen=null;
		pic = new SpriteBatch();

		logo = u.assets.get("Logos/1024.png");
		background = u.assets.get("UI/Background.png");
		
		emailB=new Rectangle(u.w/9,.5f*u.h,7*u.w/9,u.h/16);
		passB=new Rectangle(u.w/9,.42f*u.h,7*u.w/9,u.h/16);
		loginB=new Rectangle(u.w/9,.31f*u.h,7*u.w/9,u.h/16);
		backB=new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		forgotB=new Rectangle(.35f*u.w,.27f*u.h,.3f*u.w,u.h/60);
		rememberB=new Rectangle(.6f*u.w,.386f*u.h,u.h/64,u.h/64);
		errorB = new Rectangle(u.w/9,.2f*u.h,7*u.w/9,u.h/16);
		
		email = new TextField(e, u.textFieldStyle);
			email.setMessageText("Email");
			email.setAlignment(Align.center);
			email.setBounds(emailB.x, emailB.y, emailB.width, emailB.height);
		pass = new TextField(p, u.textFieldStyle);
			pass.setMessageText("Password");
			pass.setAlignment(Align.center);
			pass.setPasswordMode(true);
			pass.setPasswordCharacter('*');
			pass.setBounds(passB.x, passB.y, passB.width, passB.height);
		
		login = new TextButton("Login",u.standardButtonStyle);
			login.setBounds(loginB.x, loginB.y, loginB.width, loginB.height);
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		forgot = new TextButton("Forgot Password?",u.linkButtonStyle);
			forgot.setBounds(forgotB.x, forgotB.y, forgotB.width, forgotB.height);
		error = new TextButton(u.error,u.errorButtonStyle);
			error.setBounds(errorB.x, errorB.y, errorB.width, errorB.height);
			
		remember = new CheckBox("",u.checkStyle);
			remember.setBounds(rememberB.x, rememberB.y, rememberB.width, rememberB.height);
			
		stage.addActor(email);
		stage.addActor(pass);
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		error.setText(u.error);
		
		if(u.myAcc!=null){
			if(!u.myAcc.confirmed){u.nextScreen=new ConfirmationScreen(u);}
			else{u.nextScreen=new MainScreen(u);}
		}
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		pic.setColor(1, 1, 1, ((u.nextScreen!=null&&u.nextScreen instanceof HomeScreen)||u.nextScreen==null?1f:fade));
		pic.draw(logo, .25f*u.w, .569f*u.h+(logoMoveAnim*u.h/16), u.w/2, u.w/2);
		
		email.draw(pic, fade);
		pass.draw(pic, fade);
		login.draw(pic, fade);
		back.draw(pic, fade);
		forgot.draw(pic, fade);
		remember.draw(pic, fade);
		error.draw(pic, fade);
		
		pic.setColor(1, 1, 1, fade);
		layout .setText(u.smallFnt, "Remember me?      ");
		u.smallFnt.setColor(new Color(.694f,.694f,.694f,fade));
		u.smallFnt.draw(pic, "Remember me? ", u.w/2-layout.width/2, .4f*u.h);
		
		pic.end();
		
		if(u.nextScreen!=null){
			//System.out.println(u.nextScreen);
			if(u.nextScreen instanceof HomeScreen){
			HomeScreen hs = (HomeScreen)u.nextScreen;
			if(fade>.1f){fade+=(0-fade)/2;}
			if(fade<.5f&&logoMoveAnim>.1f){logoMoveAnim+=(0-logoMoveAnim)/2;}
			else if(logoMoveAnim<.1f){hs.logoMoveAnim=.55f;u.setScreen(hs);}}
			else{
				if(fade>.1f){fade+=(0-fade)/2;}
				else{u.setScreen(u.nextScreen);}
			}
		}
		else{
		if(logoMoveAnim<1f){logoMoveAnim+=(1-logoMoveAnim)/2;}
		if(logoMoveAnim>.5f&&fade<1){fade+=(1-fade)/2;}}

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
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
		if(stage.getKeyboardFocus().equals(email)){if(character == ''&&e.length()>0){
			e=e.substring(0, e.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			e+=character;
		}}
		
		if(stage.getKeyboardFocus().equals(pass)){if(character == ''&&p.length()>0){
			p=p.substring(0, p.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			p+=character;
		}}
		
		e=e.replaceAll("\\p{Cntrl}","");
		e=e.replaceAll("\\s+","");
		p=p.replaceAll("\\p{Cntrl}","");
		p=p.replaceAll("\\s+","");
		
		email.setText(e);
		pass.setText(p);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		else if(Intersector.overlaps(touch, forgotB)){forgot.toggle();}
		else if(Intersector.overlaps(touch, loginB)){login.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){u.nextScreen = new HomeScreen(u);}
		else if(Intersector.overlaps(touch, forgotB)){
			u.nextScreen=new ForgotScreen(u,null);
		}
		else if(Intersector.overlaps(touch, loginB)){
			if(e.length()>0&&p.length()>0){
				Login l = new Login();
					l.e=e;
					l.p=p;
					
					if(remember.isChecked()){
						u.prefs.putString("email", e);
						u.prefs.putString("password", p);
						u.prefs.flush();
					}
					
				u.send(l);}
			else{u.error="Invalid email or password";}}
		else if(Intersector.overlaps(touch, emailB)){stage.setKeyboardFocus(email);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, passB)){stage.setKeyboardFocus(pass);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, rememberB)){remember.toggle();}
		
		
		return true;
	}

	private void toggleOff() {
		Gdx.input.setOnscreenKeyboardVisible(false);
		stage.setKeyboardFocus(null);
		if(back.isChecked()){back.toggle();}
		if(login.isChecked()){login.toggle();}
		if(forgot.isChecked()){forgot.toggle();}
		
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
