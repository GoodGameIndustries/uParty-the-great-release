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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ggi.uparty.uParty;

/**
 * @author Emmett
 *
 */
public class HomeScreen implements Screen, InputProcessor{

	public uParty u;
	
	public SpriteBatch pic;
	
	public Texture background,logo;
	
	public float logoMoveAnim = -1;
	public float fade=0;
	
	public TextButton login;
	public TextButton signUp;
	
	public Rectangle loginB;
	public Rectangle signUpB;

	private boolean fadeOut = false;

	
	
	public HomeScreen(uParty u){
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
		u.assets.update();
		pic = new SpriteBatch();
		
		background = u.assets.get("UI/Background.png");
		logo = u.assets.get("Logos/1024.png");
		
		loginB = new Rectangle(u.w/9,.45f*u.h,7*u.w/9,u.h/16);
		signUpB = new Rectangle(u.w/9,.45f*u.h-1.3f*u.h/16f,7*u.w/9,u.h/16);
		
		u.standardButtonStyle = new TextButtonStyle();
			u.standardButtonStyle.up=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/Filled.png",Texture.class)));
			u.standardButtonStyle.down=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/FilledChecked.png",Texture.class)));
			u.standardButtonStyle.checked=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/FilledChecked.png",Texture.class)));
			u.standardButtonStyle.font=u.mediumFnt;
			u.standardButtonStyle.fontColor=u.darkL;
		u.linkButtonStyle = new TextButtonStyle();
			u.linkButtonStyle.font = u.smallFnt;
			u.linkButtonStyle.checkedFontColor = new Color(1,.8f,.2f,1);
			u.linkButtonStyle.fontColor = u.orange;
			//u.linkButtonStyle.up=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/TextField.png", Texture.class)));
		u.errorButtonStyle = new TextButtonStyle();
			u.errorButtonStyle.font = u.smallFnt;
			//u.errorButtonStyle.checkedFontColor = new Color(1,.8f,.2f,1);
			u.errorButtonStyle.fontColor = Color.RED;
			//u.errorButtonStyle.up=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/TextField.png", Texture.class)));
		u.textFieldStyle = new TextFieldStyle();
			u.textFieldStyle.font=u.mediumFnt;
			u.textFieldStyle.fontColor=u.orange;
			u.textFieldStyle.background=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/TextField.png", Texture.class)));
			u.textFieldStyle.focusedBackground=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/TextFieldChecked.png", Texture.class)));
		u.checkStyle = new CheckBoxStyle();
			u.checkStyle.checkboxOff=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/CheckBox.png", Texture.class)));
			u.checkStyle.checkboxOn=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/CheckBoxChecked.png", Texture.class)));
			u.checkStyle.checked=new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/CheckBoxChecked.png", Texture.class)));
			u.checkStyle.font=u.smallFnt;
					
		login=new TextButton("Login",u.standardButtonStyle);
			login.setBounds(loginB.x, loginB.y, loginB.width, loginB.height);
		signUp=new TextButton("Sign Up",u.standardButtonStyle);
			signUp.setBounds(signUpB.x, signUpB.y, signUpB.width, signUpB.height);

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		if(logoMoveAnim<.55f){logoMoveAnim+=(.55-logoMoveAnim)/2;}
		if(logoMoveAnim>.4f&&fade<1&&!fadeOut){fade+=(1-fade)/2;}
		
		if(fadeOut&&fade>.01f){fade+=(0-fade)/2;}
		else if(fadeOut){u.setScreen(u.nextScreen);}
		//System.out.println(fade);
		
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		pic.draw(logo, .25f*u.w, .5f*u.h+(logoMoveAnim*u.h/8), u.w/2, u.w/2);
		login.draw(pic, fade);
		signUp.draw(pic, fade);
		pic.end();

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch =new Rectangle(screenX,screenY,1,1);
		if(Intersector.overlaps(touch, loginB)){
			login.toggle();
		}
		else if(Intersector.overlaps(touch, signUpB)){
			signUp.toggle();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY=(int) (u.h-screenY);
		Rectangle touch =new Rectangle(screenX,screenY,1,1);
		toggleOff();
		if(Intersector.overlaps(touch, loginB)){
			fadeOut=true;
			u.nextScreen=new LoginScreen(u);
		}
		else if(Intersector.overlaps(touch, signUpB)){
			fadeOut =true;
			u.nextScreen=new SignUpScreen(u);
		}
		return true;
	}

	private void toggleOff() {
		if(login.isChecked()){login.toggle();}
		if(signUp.isChecked()){signUp.toggle();}
		
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
