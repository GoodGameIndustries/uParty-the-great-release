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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Confirm;
import com.ggi.uparty.network.Resend;

/**
 * @author Emmett
 *
 */
public class ConfirmationScreen implements Screen, InputProcessor{
	
	public uParty u;
	
	public SpriteBatch pic;
	
	public Texture logo,background;
	
	public TextButton resend,cont,error;
	
	public TextField codeField;
	
	public String codeInput="";
	
	public Rectangle resendB,contB,codeFieldB,errorB;
	
	public Stage stage;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public float fade = 0;
	
	public ConfirmationScreen(uParty u){
		this.u=u;
		u.nextScreen=null;
		
		
		resendB = new Rectangle(u.w/9,.39f*u.h,7*u.w/9,u.h/16);
		contB = new Rectangle(u.w/9,.31f*u.h,7*u.w/9,u.h/16);
		codeFieldB = new Rectangle(u.w/9,.47f*u.h,7*u.w/9,u.h/16);
		errorB = new Rectangle(u.w/9,.23f*u.h,7*u.w/9,u.h/16);
		
		codeField = new TextField(codeInput,u.textFieldStyle);
			codeField.setMessageText("Confirmation Code");
			codeField.setBounds(codeFieldB.x, codeFieldB.y, codeFieldB.width, codeFieldB.height);
			codeField.setAlignment(Align.center);
			
		resend = new TextButton("Resend Confirmation Email",u.standardButtonStyle);
			resend.setBounds(resendB.x, resendB.y, resendB.width, resendB.height);
		cont = new TextButton("Continue",u.standardButtonStyle);
			cont.setBounds(contB.x, contB.y, contB.width, contB.height);
		error = new TextButton(u.error,u.errorButtonStyle);
			error.setBounds(errorB.x, errorB.y, errorB.width, errorB.height);
			
		stage = new Stage();
		stage.addActor(codeField);
	}

	@Override
	public void show() {
		u.nextScreen=null;
		Gdx.input.setInputProcessor(this);
		pic = new SpriteBatch();
		
		logo = u.assets.get("Logos/1024.png");
		background = u.assets.get("UI/Background.png");
		
	}

	@Override
	public void render(float delta) {
		error.setText(u.error);
		if(u.nextScreen==null&&fade<1f){fade+=(1-fade)/2;}
		else if(u.nextScreen!=null&&fade>.1f){fade+=(0-fade)/2;}
		else if(u.nextScreen!=null){u.setScreen(u.nextScreen);}
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		cont.draw(pic, fade);
		codeField.draw(pic, fade);
		resend.draw(pic, fade);
		error.draw(pic, fade);
		layout.setText(u.largeFnt, "We sent you an email!");
		u.largeFnt.setColor(new Color(247f/255f,148f/255f,29f/255f,fade));
		u.largeFnt.draw(pic,"We sent you an email!",u.w/2-layout.width/2,.7f*u.h);
		pic.end();
		
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
		if(stage.getKeyboardFocus().equals(codeField)){if(character == ''&&codeInput.length()>0){
			codeInput=codeInput.substring(0, codeInput.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			codeInput+=character;
		}}
		
		codeInput=codeInput.replaceAll("\\p{Cntrl}","");
		codeInput=codeInput.replaceAll("\\s+","");
		
		codeField.setText(codeInput);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		if(Intersector.overlaps(touch, resendB)){resend.toggle();}
		else if(Intersector.overlaps(touch, contB)){cont.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		if(Intersector.overlaps(touch, resendB)){Resend r = new Resend();r.e=u.myAcc.e;u.send(r);u.error="Code has been resent";}
		else if(Intersector.overlaps(touch, contB)){if((""+u.myAcc.code).equals(codeInput)){
			Confirm c = new Confirm();c.e=u.myAcc.e;u.myAcc.confirmed=true;u.send(c);
		}
		else{
			u.error="Incorrect code";
		}
		}
		else if(Intersector.overlaps(touch, codeFieldB)){stage.setKeyboardFocus(codeField);}
		return true;
	}

	private void toggleOff() {
		stage.setKeyboardFocus(null);
		if(cont.isChecked()){cont.toggle();}
		if(resend.isChecked()){resend.toggle();}
		
		
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
