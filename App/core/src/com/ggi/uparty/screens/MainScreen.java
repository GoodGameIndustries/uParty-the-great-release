package com.ggi.uparty.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.ui.EventList;
import com.ggi.uparty.ui.SlideInMenu;
import com.ggi.uparty.ui.Toolbar;

public class MainScreen implements Screen, GestureListener {

	public uParty u;

	public Texture background;

	public SpriteBatch pic;

	public Toolbar toolbar;

	public SlideInMenu menu;

	public EventList events;

	public float fade = 0;

	public int lastY;

	public Group g = null;

	public Rectangle touchDown, bBounds;

	public Button b;

	public MainScreen(uParty u) {
		this.u = u;
		toolbar = new Toolbar(this);
		menu = new SlideInMenu(u, g);
		events = new EventList(this);

		bBounds = new Rectangle(u.w-.12375f*u.h, .03f*u.h, .09375f*u.h, .09375f*u.h);
		
		ButtonStyle s = new ButtonStyle();
			s.up = new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/newPostUp.png", Texture.class)));
			s.down = new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/newPostDown.png", Texture.class)));
			s.checked = new TextureRegionDrawable(new TextureRegion(u.assets.get("UI/newPostDown.png",Texture.class)));
		b = new Button(s);
		b.setBounds(bBounds.x, bBounds.y, bBounds.width, bBounds.height);
			
		events.scrolled = (int) (-.16f*u.h);

		events.refresh = true;
		events.scrolled = (int) (-.16f*u.h);
		refresh();
	}

	@Override
	public void show() {

		
		
		pic = new SpriteBatch();
		background = u.assets.get("UI/Background.png");
		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (toolbar.sortState != 0) {
			menu.theta = 0;
		}
		if (toolbar.sortState == 1 && toolbar.sideScroll <= 0) {
			menu.theta = -toolbar.sideScroll / u.w * 4;
			// System.out.println(menu.theta);
		} else if (toolbar.sortState == 0) {
			menu.theta = 1 - toolbar.sideScroll / u.w * 4;
			if (menu.theta > 1) {
				menu.theta = 1;
			}
			// System.out.println(menu.theta);
		}

		if (g != null) {
			toolbar.feed = g.name;
		} else {
			toolbar.feed = "";
		}

		if (u.needUpdate && g == null) {
			events.giveEvents(u.events);
			u.needUpdate = false;
		} else if (u.needUpdate) {
			boolean gRef = false;
			for (int i = 0; i < u.myAcc.groups.size(); i++) {
				if (g.name.equals(u.myAcc.groups.get(i).name) && g.owner.equals(u.myAcc.groups.get(i).owner)) {
					g = u.myAcc.groups.get(i);
					gRef = true;
					System.out.println("update");
					events.giveEvents(g.events);
				}
			}
			if (!gRef) {
				g = null;
			}
			u.needUpdate = false;
		}

		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		events.draw(pic, fade);
		toolbar.draw(pic, fade);
		b.draw(pic, fade);
		menu.draw(pic, fade);
		pic.end();

		if (u.nextScreen == null && fade < 1f) {
			fade += (1 - fade) / 2;
		} else if (u.nextScreen != null && fade > .1f) {
			/* menu.open=false; */fade += (0 - fade) / 2;
		} else if (u.nextScreen != null) {
			u.setScreen(u.nextScreen);
			
		}

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

	public void refresh() {
		//u.events.clear();
		//events.refresh = true;
		//events.scrolled = (int) (-.16f*u.h);
		Refresh r = new Refresh();
		r.e = u.myAcc.e;
		r.lat = u.controller.getLat();
		r.lng = u.controller.getLong();
		u.send(r);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		y = u.h - y;
		Rectangle touch = new Rectangle(x, y, 1, 1);
		if (1 - menu.theta < .05) {
			// System.out.println("Menu touched");
			menu.down(touch);
		}
		else{
			if(Intersector.overlaps(touch, bBounds)){
				b.setChecked(true);
			}
		}
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		b.setChecked(false);
		y = u.h - y;
		Rectangle touch = new Rectangle(x, y, 1, 1);
		if (1 - menu.theta < .05) {
			menu.tap(touch);
		} else {
			events.touch(touch, touch);
			toolbar.touchUp(touch);
			toolbar.touchDown(touch);
			if(Intersector.overlaps(touch, bBounds)){
				u.nextScreen = new CreateEventScreen(u,g);
			}
		}
		// menu.touchDown(touch);
		// menu.touchUp(touch);
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		velocityY = u.h - velocityY;
		if (Math.abs(velocityY) > Math.abs(velocityX)) {
			events.velocity = (int) velocityY;
		} else if (Math.abs(velocityY) < Math.abs(velocityX) && Math.abs(velocityX) > .1f*u.h) {
			if(velocityX > 0){
				if(toolbar.sortState > 0){toolbar.sortState--;}
			}
			else if(velocityX < 0){
				if(toolbar.sortState < 3){toolbar.sortState++;}
			}
			u.needUpdate=true;
		}
		
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (Math.abs(deltaY) > Math.abs(deltaX)) {
			events.isPan = true;
			events.scrolled -= (int) deltaY;
		} else if (Math.abs(deltaY) < Math.abs(deltaX)) {
		}
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		events.velocity = 0;
		toolbar.panStop();
		events.isPan = false;
		return true;
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
