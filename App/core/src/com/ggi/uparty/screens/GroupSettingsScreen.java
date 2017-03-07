package com.ggi.uparty.screens;

import java.util.ArrayList;
import java.util.Collections;

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
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.LeaveGroup;
import com.ggi.uparty.network.Member;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.ui.List;
import com.ggi.uparty.ui.MemberModule;
import com.ggi.uparty.ui.Module;
import com.ggi.uparty.util.RankComparator;

public class GroupSettingsScreen implements Screen, InputProcessor {

	public uParty u;

	public Group g;

	public Texture background;

	public float fade = 0;

	public SpriteBatch pic;

	public Rectangle backB, inviteB, deleteB;

	public TextButton back, invite, delete;

	public GlyphLayout layout = new GlyphLayout();

	public ArrayList<Module> modules = new ArrayList<Module>();

	public List list;

	public boolean initTouch = false;

	private boolean override = true;

	private boolean goB = false;

	public GroupSettingsScreen(uParty u, Group g) {
		this.u = u;
		this.g = g;
		list = new List(u);
	}

	@Override
	public void show() {
		override = true;
		background = u.assets.get("UI/Background.png");

		Gdx.input.setInputProcessor(this);

		pic = new SpriteBatch();

		backB = new Rectangle(u.w / 36, .93f * u.h, .15f * u.w, .05f * u.h);
		inviteB = new Rectangle(0, .7625f * u.h, u.w, .0625f * u.h);
		deleteB = new Rectangle(0, 0, u.w, .0625f * u.h);

		back = new TextButton("Back", u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);
		invite = new TextButton(g.owner.equals(u.myAcc.e) ? "Invite New Members" : "", u.standardButtonStyle);
		invite.setBounds(inviteB.x, inviteB.y, inviteB.width, inviteB.height);
		delete = new TextButton(g.owner.equals(u.myAcc.e) ? "Delete Group" : "Leave Group", u.redButtonStyle);
		delete.setBounds(deleteB.x, deleteB.y, deleteB.width, deleteB.height);

		Collections.sort(g.members, new RankComparator());

		// System.out.println(g.members.size());

		for (Member m : g.members) {
			modules.add(new MemberModule(list, m));
		}
		/*
		 * Member m = new Member(); m.u="Test";m.e="Test@test.edu";
		 * 
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m)); modules.add(new
		 * MemberModule(list,m)); modules.add(new MemberModule(list,m));
		 * modules.add(new MemberModule(list,m));
		 */

		list.give(modules);
		list.bounds = new Rectangle(0, .0625f * u.h, u.w, .7f * u.h);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (override) {
			refresh();
			override = false;
		}

		refreshModules();

		list.g = g;
		list.give(modules);

		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);

		list.draw(pic, fade);

		back.draw(pic, fade);
		delete.draw(pic, fade);
		invite.draw(pic, fade);

		u.largeFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(u.largeFnt, g.name);
		u.largeFnt.draw(pic, g.name, u.w / 2 - layout.width / 2, .9f * u.h - layout.height / 2);

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

	private void refreshModules() {
		for (int i = 0; i < u.myAcc.groups.size(); i++) {
			if (u.myAcc.groups.get(i).name.equals(g.name) && u.myAcc.groups.get(i).owner.equals(g.owner)) {
				g.members.clear();
				g.members.addAll(u.myAcc.groups.get(i).members);
			}
		}

		modules.clear();
		Collections.sort(g.members, new RankComparator());

		// System.out.println(g.members.size());

		for (Member m : g.members) {
			modules.add(new MemberModule(list, m));
		}

		list.give(modules);
		list.bounds = new Rectangle(0, .0625f * u.h, u.w, .7f * u.h);

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
		if (Intersector.overlaps(touch, backB)) {
			back.toggle();
		}
		if (Intersector.overlaps(touch, inviteB) && g.owner.equals(u.myAcc.e)) {
			invite.toggle();
		}
		if (Intersector.overlaps(touch, deleteB)) {
			delete.toggle();
		}
		if (Intersector.overlaps(touch, list.bounds)) {
			list.touchDown(touch);
			initTouch = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h - screenY);
		Rectangle touch = new Rectangle(screenX, screenY, 1, 1);
		toggleOff();
		if (!initTouch) {
			if (Intersector.overlaps(touch, backB)) {
				u.nextScreen = new MainScreen(u);
			}
			if (Intersector.overlaps(touch, inviteB) && g.owner.equals(u.myAcc.e)) {
				u.nextScreen = new InviteScreen(u, g);
			}
			if (Intersector.overlaps(touch, deleteB) && g.owner.equals(u.myAcc.e)) {
				u.nextScreen = new ConfirmDeleteScreen(u, g);
			} else if (Intersector.overlaps(touch, deleteB)) {
				LeaveGroup l = new LeaveGroup();
				l.e = u.myAcc.e;
				l.group = g.name.replace(" ", "") + g.owner.replace(".", "_").replace("@", "_");
				u.send(l);
				u.nextScreen = new MainScreen(u);
			}
		}
		if (Intersector.overlaps(touch, list.bounds)) {
			list.touchUp(touch);
		}
		initTouch = false;
		return true;
	}

	public void toggleOff() {
		if (back.isChecked()) {
			back.toggle();
		}
		if (invite.isChecked()) {
			invite.toggle();
		}
		if (delete.isChecked()) {
			delete.toggle();
		}
		refresh();
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
		screenY = (int) (u.h - screenY);
		if (initTouch) {
			list.move(screenY - list.lastY);
		}
		return true;
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
