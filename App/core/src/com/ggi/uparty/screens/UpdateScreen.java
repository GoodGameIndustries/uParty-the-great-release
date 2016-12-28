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

public class UpdateScreen implements Screen, InputProcessor {

	public uParty u;

	public SpriteBatch pic;

	public Texture bg;

	public GlyphLayout layout;

	public Rectangle bounds;

	public TextButton update;

	public UpdateScreen(uParty u) {
		this.u = u;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);

		pic = new SpriteBatch();
		bg = u.assets.get("UI/Background.png");
		layout = new GlyphLayout();
		bounds = new Rectangle(.25f * u.w, .45f * u.h, .5f * u.w, u.h / 16);
		update = new TextButton("Update", u.standardButtonStyle);
		update.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		pic.begin();

		pic.draw(bg, 0, 0, u.w, u.h);

		layout.setText(u.largeFnt, "Please update uParty.");
		u.largeFnt.setColor(u.orange);
		u.largeFnt.draw(pic, "Please update uParty.", .5f * u.w - layout.width / 2, .6f * u.h + layout.height / 2);
		update.draw(pic, 1);

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);
		if (Intersector.overlaps(touch, bounds)) {
			update.toggle();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);
		if (update.isChecked()) {
			update.toggle();
		}
		if (Intersector.overlaps(touch, bounds)) {
			Gdx.net.openURI("www.upartyapp.com/redirect");
		}

		return true;
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
