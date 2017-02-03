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
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Report;

public class ConfirmReportScreen implements Screen, InputProcessor {

	public uParty u;

	public Group g;

	public float fade = 0;

	public SpriteBatch pic;

	public Texture background;

	public GlyphLayout layout = new GlyphLayout();

	public Rectangle yesB, noB;

	public TextButton yes, no;
	
	public Event e;

	public ConfirmReportScreen(uParty u, Group g, Event e) {
		this.u = u;
		this.g = g;
		this.e=e;
	}

	@Override
	public void show() {
		pic = new SpriteBatch();

		Gdx.input.setInputProcessor(this);

		background = u.assets.get("UI/Background.png");

		yesB = new Rectangle(u.w / 4, .42f * u.h, u.w / 8, u.h / 16);
		noB = new Rectangle(5 * u.w / 8, .42f * u.h, u.w / 8, u.h / 16);

		yes = new TextButton("Yes", u.standardButtonStyle);
		yes.setBounds(yesB.x, yesB.y, yesB.width, yesB.height);
		no = new TextButton("No", u.standardButtonStyle);
		no.setBounds(noB.x, noB.y, noB.width, noB.height);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);

		u.mediumFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(u.mediumFnt, "Are you sure you want to report this event?");
		u.mediumFnt.draw(pic, "Are you sure you want to report this event?", u.w / 2 - layout.width / 2,
				.55f * u.h - layout.height / 2);

		yes.draw(pic, fade);
		no.draw(pic, fade);

		pic.end();

		if (u.nextScreen == null && fade < 1f) {
			fade += (1 - fade) / 2;
		} else if (u.nextScreen != null && fade > .1f) {
			fade += (0 - fade) / 2;
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
		if (Intersector.overlaps(touch, yesB)) {
			yes.toggle();
		} else if (Intersector.overlaps(touch, noB)) {
			no.toggle();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);
		toggleOff();
		if (Intersector.overlaps(touch, yesB)) {
			Report r = new Report();
			r.ID = e.ID;
			r.lat = e.lat;
			r.lng = e.lng;
			r.e = u.myAcc.e;
			r.group = e.group;
			u.send(r);
			System.out.println("Report sent");
			MainScreen s = new MainScreen(u);
			s.g = g;

			u.nextScreen = s;
		} else if (Intersector.overlaps(touch, noB)) {
			u.goBack();
			//u.nextScreen = new GroupSettingsScreen(u, g);
		}
		return true;
	}

	private void toggleOff() {
		if (yes.isChecked()) {
			yes.toggle();
		}
		if (no.isChecked()) {
			no.toggle();
		}

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
