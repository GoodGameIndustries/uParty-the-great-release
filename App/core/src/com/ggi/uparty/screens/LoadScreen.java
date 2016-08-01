/**
 * 
 */
package com.ggi.uparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Login;
import com.ggi.uparty.ui.RadialSprite;

/**
 * @author Emmett
 *
 */
public class LoadScreen implements Screen {

	public uParty u;
	
	public SpriteBatch pic;
	
	private Texture background;
	private Texture logo;
	
	private RadialSprite loadingBar;
	
	private int i = 0;
	
	float angle=0;
	
	public LoadScreen(uParty u){
		this.u=u;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		pic = new SpriteBatch();
		u.loadFonts();
		
		if(!u.logout){
		Login l = new Login();
		l.e=u.prefs.getString("email", "");
		l.p=u.prefs.getString("password","");
		if(l.e.length()>0&&l.p.length()>0){
			u.send(l);
		}
		}
		//System.out.println(l.e+":"+l.p);
		

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		u.assets.update();
		
		
		
		i++;
		angle = (u.assets.getLoadedAssets()/(float)(u.assets.getLoadedAssets()+u.assets.getQueuedAssets()))*360f;
		if(angle>i/50*360){angle=-i/50f*360f;}
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//System.out.println(u.assets.getQueuedAssets());
		if(logo!=null&&background!=null){
			loadingBar.setAngle(angle);
		pic.begin();
		pic.draw(background, 0, 0, u.w, u.h);
		pic.draw(logo, .25f*u.w, .5f*u.h-u.w/4, u.w/2, u.w/2);
		loadingBar.draw(pic,.25f*u.w, .5f*u.h-u.w/4, u.w/2, u.w/2);
		pic.end();}
		else if(u.assets.isLoaded("UI/Background.png")&&u.assets.isLoaded("Logos/1024.png")&&u.assets.isLoaded("UI/CircleLoad.png")){
			background = u.assets.get("UI/Background.png");
			logo = u.assets.get("Logos/1024.png");
			loadingBar = new RadialSprite(new TextureRegion(u.assets.get("UI/CircleLoad.png",Texture.class)));
			
		}

		if(i>50&&u.assets.update()){
			
			u.setScreen(new HomeScreen(u));
		}
		
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

}
