package com.ggi.uparty.screens;

import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.UpVote;
import com.ggi.uparty.ui.CommentModule;
import com.ggi.uparty.ui.EventBar;
import com.ggi.uparty.ui.List;
import com.ggi.uparty.ui.Module;
import com.ggi.uparty.ui.RadialSprite;

public class EventScreen implements Screen, GestureListener {

	public uParty u;

	public Event e;

	public Texture background, up, upC, down, downC;;

	public SpriteBatch pic;

	public TextArea description, location, time;

	public float fade = 0;

	public TextButton back, report, reply;

	public Rectangle descriptionB, locationB, timeB, backB, upB, downB, reportB, replyB;

	public GlyphLayout layout = new GlyphLayout();

	public float iHeight = 0;

	public long lv = 0;

	public float leftOverXP = 1, neededToLvXP = 1;

	public float veloc = 0;

	public RadialSprite lvBar;

	public float lvAngle = 0;

	public Group g;

	public List list;

	public ArrayList<Module> modules = new ArrayList<Module>();

	private boolean initTouch;

	public float scrolled = 0;

	public float velocity = 0;

	public EventBar bar;

	public boolean isPan = false;

	private boolean goB = false;
	
	private MainScreen bScreen;

	public EventScreen(uParty u, Event e, Group g, MainScreen s) {
		this.u = u;
		this.e = e;
		this.g = g;
		this.bScreen = s;

		list = new List(u);

		bar = new EventBar(u, this);

		genModules();
	}

	private void genModules() {
		for (int i = 0; i < e.comments.size(); i++) {
			modules.add(new CommentModule(list, e.comments.get(i)));
		}

	}

	@Override
	public void show() {
		pic = new SpriteBatch();

		GestureDetector gd = new GestureDetector(this);
		Gdx.input.setInputProcessor(gd);
		

		getLv();

		descriptionB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h, .55f * u.w, .124f * u.h);
		locationB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h - .189375f * u.h, .55f * u.w,
				.124f * u.h);
		timeB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h - .378750f * u.h, .55f * u.w,
				.124f * u.h);
		backB = new Rectangle(u.w / 36, .93f * u.h, .2f * u.w, .05f * u.h);
		upB = new Rectangle(.9f * u.w - .048f * u.w, .7584375f * u.h + .05f * u.h - .012f * u.h, .096f * u.w,
				.024f * u.h);
		downB = new Rectangle(.9f * u.w - .048f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h, .096f * u.w,
				.024f * u.h);
		reportB = new Rectangle(u.w - .2f * u.w - u.w / 36, .93f * u.h, .2f * u.w, .05f * u.h);
		replyB = new Rectangle(0, 0, u.w, u.h / 16);

		background = u.assets.get("UI/EventScreenBG.png");
		up = u.assets.get("UI/SlideUp.png");
		upC = u.assets.get("UI/SlideUpChecked.png");
		down = u.assets.get("UI/SlideDown.png");
		downC = u.assets.get("UI/SlideDownChecked.png");

		back = new TextButton("Back", u.standardButtonStyle);
		back.setBounds(backB.x, backB.y, backB.width, backB.height);
		report = new TextButton(u.myAcc.e.equals(e.owner)?"Delete":"Report", u.redButtonStyle);
		report.setBounds(reportB.x, reportB.y, reportB.width, reportB.height);
		reply = new TextButton("Tap to Reply", u.standardButtonStyle);
		reply.setBounds(replyB.x, replyB.y, replyB.width, replyB.height);

		description = new TextArea(e.description, u.viewAreaStyle);
		description.setBounds(descriptionB.x, descriptionB.y, descriptionB.width, descriptionB.height);
		location = new TextArea(e.location, u.viewAreaStyle);
		location.setBounds(locationB.x, locationB.y, locationB.width, locationB.height);
		time = new TextArea("Start: " + dateToString(e.start) + "\nEnd: " + dateToString(e.end), u.viewAreaStyle);
		time.setBounds(timeB.x, timeB.y, timeB.width, timeB.height);

		lvBar = new RadialSprite(new TextureRegion(u.assets.get("UI/CircleLoad.png", Texture.class)));

		list.bounds = new Rectangle(0, .0625f * u.h, u.w, .6f * u.h);
		list.modHeight = .1f;

		getLv();
	}

	@Override
	public void render(float delta) {
		
		reply.setText((bar.select==1?"Open in Maps":"Tap to Reply"));

		list.move(veloc);

		if (scrolled < 0) {
			scrolled = 0;
		}
		if (scrolled > .378750f * u.h) {
			scrolled = .378750f * u.h;
		}
		if (scrolled - bar.select * (.189375f * u.h) != 0) {
			scrolled += (bar.select * (.189375f * u.h) - scrolled) / 3;
		}

		descriptionB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h + scrolled, .55f * u.w,
				.124f * u.h);
		locationB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h - .189375f * u.h + scrolled,
				.55f * u.w, .124f * u.h);
		timeB = new Rectangle(.2f * u.w, .7584375f * u.h - .05f * u.h - .012f * u.h - .378750f * u.h + scrolled,
				.55f * u.w, .124f * u.h);
		description.setBounds(descriptionB.x, descriptionB.y, descriptionB.width, descriptionB.height);
		location.setBounds(locationB.x, locationB.y, locationB.width, locationB.height);
		time.setBounds(timeB.x, timeB.y, timeB.width, timeB.height);

		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		list.give(modules);

		pic.begin();

		pic.setColor(1, 1, 1, 1);
		pic.draw(u.assets.get("UI/EventModule.png", Texture.class), .13f * u.w, .66375f * u.h, .87f * u.w,
				.189375f * u.h);
		pic.draw(background, 0, 0, u.w, u.h);

		description.draw(pic, fade);
		location.draw(pic, fade);
		time.draw(pic, fade);
		
		//leftOverXP = 2000;
		
		float ratio = -(leftOverXP / neededToLvXP) * 360;
		if (lvAngle != ratio) {
			lvAngle += (ratio - lvAngle) / 32;
		}
		lvBar.setAngle(lvAngle);

		lvBar.setColor(new Color(1, 1, 1, fade));
		if (leftOverXP != 0 && lvAngle != 0) {
			lvBar.draw(pic, .2f * u.w, .7584375f * u.h - .05f * u.h - .025f * u.h - .378750f * u.h + scrolled,
					.075f * u.h, .075f * u.h);
		}

		layout.setText(u.supersmallFnt, "" + lv);
		u.supersmallFnt.setColor(1, 1, 1, fade);
		u.supersmallFnt.draw(pic, "" + lv, .2f * u.w + .075f *u.h/2-layout.width/2,
				 .7584375f * u.h - .05f * u.h - .025f * u.h - .378750f * u.h + scrolled + .075f*u.h/2 + layout.height / 2);
		
		pic.draw(u.assets.get("UI/EventScreenBG.png", Texture.class), 0, 0, u.w, u.h);
		pic.draw(u.assets.get("Logos/1024.png", Texture.class), u.w / 2 - .025f * u.h, .93f * u.h, .05f * u.h,
				.05f * u.h);

		bar.draw(pic, fade);
		list.draw(pic, fade);

		back.draw(pic, fade);
		// info.draw(pic, fade);
		reply.draw(pic, fade);
		report.draw(pic, fade);

		pic.setColor(1, 1, 1, fade);
		pic.draw(e.upVote.contains(u.myAcc.e) ? upC : up, upB.x, upB.y, upB.width, upB.height);
		pic.draw(e.downVote.contains(u.myAcc.e) ? downC : down, downB.x, downB.y, downB.width, downB.height);

		u.mediumFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(u.mediumFnt, e.name);
		u.mediumFnt.draw(pic, e.name, u.w / 2 - layout.width / 2, .89f * u.h + layout.height / 2);
		layout.setText(u.mediumFnt, "" + (e.upVote.size() - e.downVote.size()));
		u.mediumFnt.draw(pic, "" + (e.upVote.size() - e.downVote.size()), .9f * u.w - layout.width / 2,
				.7584375f * u.h + layout.height / 2);

		pic.end();

		if ((u.nextScreen == null && !goB) && fade < 1f) {
			fade += (1 - fade) / 2;
		} else if ((u.nextScreen != null || goB) && fade > .1f) {
			fade += (0 - fade) / 2;
		} else if (u.nextScreen != null) {
			u.setScreen(u.nextScreen);
		} else if (goB) {
			u.setScreen(bScreen);
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

	private String dateToString(Date d) {
		String result = d.getMonth() + 1 + "/" + d.getDate() + "/" + (d.getYear() + 1900);
		int hour = -1;
		boolean isPm = false;
		if (d.getHours() >= 12) {
			isPm = true;
		}
		hour = d.getHours() % 12;
		if (hour == 0) {
			hour = 12;
		}
		result += "   " + hour + ":" + String.format("%02d", d.getMinutes()) + (isPm ? " PM" : " AM");

		return result;
	}

	private void getLv() {
		lv = 0;
		while (factorial(lv + 1) * 5 <= e.ownerXp) {
			lv++;
		}
		leftOverXP = e.ownerXp - factorial(lv) * 5;
		neededToLvXP = (factorial(lv + 1) - factorial(lv)) * 5;

	}

	public long factorial(long number) {
		if (number == 0) {
			return 0;
		}
		long factorial = 1;
		for (long i = 1; i <= number; ++i) {
			factorial *= i;
		}
		return factorial;
	}

	private void toggleOff() {
		if (report.isChecked()) {
			report.toggle();
		}
		if (back.isChecked()) {
			back.toggle();
		}
		if (reply.isChecked()) {
			reply.toggle();
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		initTouch = false;
		y = (int) (u.h - y);
		Rectangle touch = new Rectangle(x, y, 1, 1);
		if (Intersector.overlaps(touch, backB)) {
			back.toggle();
		} else if (Intersector.overlaps(touch, reportB)) {
			report.toggle();
		} else if (Intersector.overlaps(touch, replyB)) {
			reply.toggle();
		}
		if (Intersector.overlaps(touch, list.bounds)) {
			list.touchDown(touch);
			initTouch = true;
		}
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		y = (int) (u.h - y);
		Rectangle touch = new Rectangle(x, y, 1, 1);
		toggleOff();
		bar.touchUp(touch);
		if (Intersector.overlaps(touch, backB)) {
			goB = true;
		} else if (Intersector.overlaps(touch, reportB)) {
			u.nextScreen = (u.myAcc.e.equals(e.owner)?new ConfirmDeleteEventScreen(u,g,e):new ConfirmReportScreen(u,g,e));

		} else if (Intersector.overlaps(touch, upB)) {
			if (e.downVote.contains(u.myAcc.e)) {
				e.downVote.remove(u.myAcc.e);
			}
			if (!e.upVote.contains(u.myAcc.e)) {
				e.upVote.add(u.myAcc.e);
			}
			UpVote o = new UpVote();
			o.e = u.myAcc.e;
			o.ID = e.ID;
			o.lng = e.lng;
			o.lat = e.lat;
			u.send(o);
		} else if (Intersector.overlaps(touch, downB)) {
			if (!e.downVote.contains(u.myAcc.e)) {
				e.downVote.add(u.myAcc.e);
			}
			if (e.upVote.contains(u.myAcc.e)) {
				e.upVote.remove(u.myAcc.e);
			}
			DownVote o = new DownVote();
			o.e = u.myAcc.e;
			o.ID = e.ID;
			o.lng = e.lng;
			o.lat = e.lat;
			u.send(o);
		} else if (Intersector.overlaps(touch, replyB)) {
			if(bar.select==1){
				String toSearch = location.getText().replace(" ", "+");
			Gdx.net.openURI("http://maps.google.com/?q="+toSearch);	
			}
			else{
			u.nextScreen = new CommentScreen(u, e, g, bScreen);
			}
		}

		if (Intersector.overlaps(touch, list.bounds)) {
			list.touchUp(touch);
		}
		initTouch = false;
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
		if (Math.abs(velocityY) > Math.abs(velocityX) && Math.abs(velocityY) > .01f * u.h) {
			if (initTouch) {
			} else {
				if (velocityY > 0) {
					bar.select++;
				} else if (velocityY < 0) {
					bar.select--;
				}
				if (bar.select < 0) {
					bar.select = 0;
				}
				if (bar.select > 2) {
					bar.select = 2;
				}
			}
		} else if (Math.abs(velocityY) < Math.abs(velocityX)) {
			// toolbar.fling(velocityX);
		}
		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (Math.abs(deltaY) > Math.abs(deltaX)) {
			if (initTouch) {
				list.move(-deltaY);
			}
		} else if (Math.abs(deltaY) < Math.abs(deltaX)) {
			// toolbar.pan(deltaX);
		}
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		velocity = 0;
		isPan = false;
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
