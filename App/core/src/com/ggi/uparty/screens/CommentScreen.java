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
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Comment;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Refresh;

public class CommentScreen implements Screen, InputProcessor {

	public uParty u;

	public TextArea comment;

	public TextButton invite, back;

	public Rectangle commentB, inviteB, backB;

	public Texture background, tA, tAC;

	private SpriteBatch pic;

	public float fade = 0;

	public String g = "";

	public GlyphLayout layout = new GlyphLayout();

	public Stage stage;

	public boolean invited = false;

	public Event e;

	public Group group;

	private boolean goB = false;
	
	public MainScreen s;

	public CommentScreen(uParty u, Event e, Group group, MainScreen s) {
		this.u = u;
		this.e = e;
		this.group = group;
		this.s=s;
	}

	@Override
	public void show() {
		pic = new SpriteBatch();

		Gdx.input.setInputProcessor(this);

		background = u.assets.get("UI/Background.png");
		tA = u.assets.get("UI/TextArea.png");
		tAC = u.assets.get("UI/TextAreaChecked.png");

		backB = new Rectangle(u.w / 36, .93f * u.h, .15f * u.w, .05f * u.h);
		commentB = new Rectangle(u.w / 9, .5f * u.h, 7 * u.w / 9, .15f * u.h);
		inviteB = new Rectangle(u.w / 4, .4f * u.h, u.w / 2, u.h / 16);

		back = new TextButton("Back", u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);

		invite = new TextButton("Send", u.standardButtonStyle);
		invite.setBounds(inviteB.x, inviteB.y, inviteB.width, inviteB.height);

		comment = new TextArea(g, u.textAreaStyle);
		comment.setBounds(commentB.x, commentB.y, commentB.width, commentB.height);
		comment.setAlignment(Align.center);
		comment.setMessageText("Comment");

		stage = new Stage();
		stage.addActor(comment);

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
		comment.draw(pic, fade);

		pic.draw(stage.getKeyboardFocus() != null && stage.getKeyboardFocus().equals(comment) ? tAC : tA,
				commentB.x - .025f * commentB.width, commentB.y - .1f * commentB.height, 1.05f * commentB.width,
				1.2f * commentB.height);

		u.largeFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(u.largeFnt, "New Comment");
		u.largeFnt.draw(pic, "New Comment", u.w / 2 - layout.width / 2, .73f * u.h - layout.height / 2);

		pic.end();

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
		if (stage.getKeyboardFocus().equals(comment)) {
			if (character == '' && g.length() > 0) {
				g = g.substring(0, g.length() - 1);
			} else if ((character == '\r' || character == '\n' || g.length() >= 140)) {
			} else {
				g += character;
			}
		}

		g = g.replaceAll("\\p{Cntrl}", "");
		// g=g.replaceAll("\\s+","");

		comment.setText(g);

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);

		if (Intersector.overlaps(touch, backB)) {
			back.toggle();
		} else if (Intersector.overlaps(touch, inviteB)) {
			invite.toggle();
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
		} else if (Intersector.overlaps(touch, inviteB)) {
			Comment c = new Comment();
			c.ID = e.ID;
			c.c = u.myAcc.xp + ":" + g;
			c.e = u.myAcc.e;
			c.group = group == null ? ""
					: group.name.replace(" ", "") + group.owner.replace(".", "_").replace("@", "_");
			c.lat = u.controller.getLat();
			c.lng = u.controller.getLong();
			if (c.c.length() > 0) {
				u.send(c);

				e.comments.add(u.myAcc.xp + ":" + g);
			}
			u.nextScreen = new EventScreen(u, e, group,s);
		} else if (Intersector.overlaps(touch, commentB)) {
			stage.setKeyboardFocus(comment);
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
		if (invite.isChecked()) {
			invite.toggle();
		}
	}

	public void refresh() {
		Refresh r = new Refresh();
		r.e = u.myAcc.e;
		r.lat = u.controller.getLat();
		r.lng = u.controller.getLong();
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
