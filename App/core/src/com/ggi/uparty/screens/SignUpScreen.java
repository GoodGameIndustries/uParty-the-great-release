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
import com.ggi.uparty.network.SignUp;

/**
 * @author Emmett
 *
 */
public class SignUpScreen implements Screen, InputProcessor{

	public uParty u;
	
	public SpriteBatch pic;
	
	public Texture logo,background;
	
	public float logoMoveAnim=0,fade=0;
	
	public Stage stage = new Stage();
	
	public TextField user,email,pass,cpass;
	
	public TextButton tos,signUp,back,error;
	
	public Rectangle userB,emailB,passB,cpassB,tosB,tosBC,signUpB,backB,errorB;
	
	public String uN="",e="",p="",cp="";
	
	public CheckBox tosC;
	
	public GlyphLayout layout = new GlyphLayout();

	
	
	public SignUpScreen(uParty u){
		this.u=u;
		u.nextScreen=null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		u.nextScreen=null;
		
		//DIALOG EXAMPLE
		//MyTextInputListener listener = new MyTextInputListener();
		//Gdx.input.getTextInput(listener, "Dialog Title", "Initial Textfield Value", "");
		
		pic = new SpriteBatch();
		
		logo = u.assets.get("Logos/1024.png");
		background = u.assets.get("UI/Background.png");
		
		userB=new Rectangle(2*u.w/9,.55f*u.h,5*u.w/9,u.h/16);
		emailB=new Rectangle(u.w/9,.47f*u.h,7*u.w/9,u.h/16);
		passB=new Rectangle(u.w/9,.39f*u.h,7*u.w/9,u.h/16);
		cpassB=new Rectangle(u.w/9,.31f*u.h,7*u.w/9,u.h/16);
		tosB=new Rectangle(.45f*u.w,.267f*u.h,u.w/4,u.h/32);
		tosBC=new Rectangle(.71f*u.w,.276f*u.h,u.h/64,u.h/64);
		signUpB = new Rectangle(u.w/9,.195f*u.h,7*u.w/9,u.h/16);
		backB = new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		errorB = new Rectangle(u.w/9,.125f*u.h,7*u.w/9,u.h/16);
		
		user = new TextField(uN,u.textFieldStyle);
			user.setMessageText("Username");
			user.setBounds(userB.x, userB.y, userB.width, userB.height);
			user.setAlignment(Align.center);
		email = new TextField(e,u.textFieldStyle);
			email.setMessageText("Email (must be.edu)");
			email.setBounds(emailB.x, emailB.y, emailB.width, emailB.height);
			email.setAlignment(Align.center);
		pass = new TextField(p,u.textFieldStyle);
			pass.setMessageText("Password");
			pass.setBounds(passB.x,passB.y,passB.width,passB.height);
			pass.setAlignment(Align.center);
			pass.setPasswordMode(true);
			pass.setPasswordCharacter('*');
		cpass = new TextField(cp,u.textFieldStyle);
			cpass.setMessageText("Confirm Password");
			cpass.setBounds(cpassB.x, cpassB.y, cpassB.width, cpassB.height);
			cpass.setAlignment(Align.center);
			cpass.setPasswordMode(true);
			cpass.setPasswordCharacter('*');
			
		tosC = new CheckBox("",u.checkStyle);
			tosC.setBounds(tosBC.x, tosBC.y, tosBC.width, tosBC.height);
		
		tos = new TextButton("terms of service",u.linkButtonStyle);
			tos.setBounds(tosB.x, tosB.y, tosB.width, tosB.height);
		signUp = new TextButton("Sign Up",u.standardButtonStyle);
			signUp.setBounds(signUpB.x, signUpB.y, signUpB.width, signUpB.height);
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);
		error = new TextButton(u.error,u.errorButtonStyle);
			error.setBounds(errorB.x, errorB.y, errorB.width, errorB.height);
			
			stage.addActor(user);
			stage.addActor(email);
			stage.addActor(pass);
			stage.addActor(cpass);
			
			Gdx.input.setInputProcessor(this);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		error.setText(u.error);
		if(u.myAcc!=null){
			if(!u.myAcc.confirmed){u.nextScreen=new ConfirmationScreen(u);}
		}
		
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		pic.draw(logo, .25f*u.w, .569f*u.h+(logoMoveAnim*u.h/16), u.w/2, u.w/2);
		
		user.draw(pic, fade);
		email.draw(pic, fade);
		pass.draw(pic, fade);
		cpass.draw(pic, fade);
		tos.draw(pic, fade);
		tosC.draw(pic, fade);
		signUp.draw(pic, fade);
		back.draw(pic, fade);
		error.draw(pic, fade);
		
		pic.setColor(1, 1, 1, fade);
		layout.setText(u.smallFnt, "I agree to the terms of service      ");
		u.smallFnt.setColor(new Color(.694f,.694f,.694f,fade));
		u.smallFnt.draw(pic, "I agree to the ", u.w/2-layout.width/2, .29f*u.h);
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
		if(stage.getKeyboardFocus().equals(user)){if(character == ''&&uN.length()>0){
			uN=uN.substring(0, uN.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			uN+=character;
		}}
		
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
		
		if(stage.getKeyboardFocus().equals(cpass)){if(character == ''&&cp.length()>0){
			cp=cp.substring(0, cp.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			cp+=character;
		}}
		
		uN=uN.replaceAll("\\p{Cntrl}","");
		uN=uN.replaceAll("\\s+","");
		e=e.replaceAll("\\p{Cntrl}","");
		e=e.replaceAll("\\s+","");
		p=p.replaceAll("\\p{Cntrl}","");
		p=p.replaceAll("\\s+","");
		cp=cp.replaceAll("\\p{Cntrl}","");
		cp=cp.replaceAll("\\s+","");
		
		user.setText(uN);
		email.setText(e);
		pass.setText(p);
		cpass.setText(cp);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		if(Intersector.overlaps(touch, signUpB)){signUp.toggle();}
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		if(Intersector.overlaps(touch, tosB)){tos.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		
		if(Intersector.overlaps(touch, signUpB)){
			if(e.length()>4&&uN.length()>0&&p.length()>0&&p.equals(cp)&&tosC.isChecked()&&e.endsWith(".edu")){
			SignUp s = new SignUp();
			s.e=e;
			s.u=uN;
			s.p=p;
			u.send(s);
			}
			else{
				u.error="Please make sure all fields are \nfilled out and your passwords match";
			}
		}
		else if(Intersector.overlaps(touch, tosBC)){tosC.toggle();}
		else if(Intersector.overlaps(touch, backB)){u.nextScreen = new HomeScreen(u);}
		else if(Intersector.overlaps(touch, userB)){stage.setKeyboardFocus(user);}
		else if(Intersector.overlaps(touch, emailB)){stage.setKeyboardFocus(email);}
		else if(Intersector.overlaps(touch, passB)){stage.setKeyboardFocus(pass);}
		else if(Intersector.overlaps(touch, cpassB)){stage.setKeyboardFocus(cpass);}
		else if(Intersector.overlaps(touch, tosB)){}
		
		return true;
	}

	private void toggleOff() {
		stage.setKeyboardFocus(null);
		if(signUp.isChecked()){signUp.toggle();}
		if(back.isChecked()){back.toggle();}
		if(tos.isChecked()){tos.toggle();}
		
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
