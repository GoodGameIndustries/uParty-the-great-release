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
import com.ggi.uparty.network.NewGroup;

public class NewGroupScreen implements Screen, InputProcessor {

	public uParty u;

	public TextField group;

	public TextButton create, back;

	public Rectangle groupB, createB, backB;

	public Texture background;

	private SpriteBatch pic;

	public float fade = 0;

	public String g = "";

	public GlyphLayout layout = new GlyphLayout();

	public Stage stage;

	public boolean created = false;

	private boolean goB = false;

	public NewGroupScreen(uParty u) {
		this.u = u;
	}

	@Override
	public void show() {
		pic = new SpriteBatch();

		Gdx.input.setInputProcessor(this);

		background = u.assets.get("UI/Background.png");

		backB = new Rectangle(u.w / 36, .93f * u.h, .15f * u.w, .05f * u.h);
		groupB = new Rectangle(u.w / 9, .5f * u.h, 7 * u.w / 9, u.h / 16);
		createB = new Rectangle(u.w / 4, .42f * u.h, u.w / 2, u.h / 16);

		back = new TextButton("Back", u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);

		create = new TextButton("Create Group", u.standardButtonStyle);
		create.setBounds(createB.x, createB.y, createB.width, createB.height);

		group = new TextField(g, u.textFieldStyle);
		group.setBounds(groupB.x, groupB.y, groupB.width, groupB.height);
		group.setAlignment(Align.center);
		group.setMessageText("Group Name");

		stage = new Stage();
		stage.addActor(group);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);

		back.draw(pic, fade);
		create.draw(pic, fade);
		group.draw(pic, fade);

		u.largeFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(u.largeFnt, "New Group");
		u.largeFnt.draw(pic, "New Group", u.w / 2 - layout.width / 2, .7f * u.h - layout.height / 2);

		pic.end();

		if (created) {
			u.nextScreen = new GroupSettingsScreen(u, u.myAcc.groups.get(u.myAcc.groups.size() - 1));
			created = false;
		}

		if ((u.nextScreen == null && !goB) && fade < 1f) {
			fade += (1 - fade) / 2;
		} else if ((u.nextScreen != null || goB) && fade > .1f) {
			fade += (0 - fade) / 2;
		} else if (u.nextScreen != null) {
			u.setScreen(u.nextScreen);
		} else if (goB) {
			u.goBack();
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
		if (stage.getKeyboardFocus().equals(group)) {
			if (character == '' && g.length() > 0) {
				g = g.substring(0, g.length() - 1);
			} else if ((character == '\r' || character == '\n')) {
			} else {
				g += character;
			}
		}

		g = g.replaceAll("\\p{Cntrl}", "");

		group.setText(g);

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);

		if (Intersector.overlaps(touch, backB)) {
			back.toggle();
		} else if (Intersector.overlaps(touch, createB)) {
			create.toggle();
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);

		toggleOff();

		if (Intersector.overlaps(touch, backB)) {
			goB = true;
		} else if (Intersector.overlaps(touch, createB)) {
			NewGroup g = new NewGroup();
			g.name = this.g;
			g.owner = u.myAcc.e;
			u.send(g);

		} else if (Intersector.overlaps(touch, groupB)) {
			stage.setKeyboardFocus(group);
			Gdx.input.setOnscreenKeyboardVisible(true);
		}

		return true;
	}

	private void toggleOff() {
		Gdx.input.setOnscreenKeyboardVisible(false);
		stage.setKeyboardFocus(null);
		if (back.isChecked()) {
			back.toggle();
		}
		if (create.isChecked()) {
			create.toggle();
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
