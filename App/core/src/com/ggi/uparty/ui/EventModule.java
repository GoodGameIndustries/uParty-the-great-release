package com.ggi.uparty.ui;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.UpVote;
import com.ggi.uparty.screens.EventScreen;

public class EventModule {

	public Rectangle bounds = new Rectangle(), upB = new Rectangle(), downB = new Rectangle();

	public Event e;

	public EventList l;

	public TextArea info;

	public Texture background, up, upC, down, downC;

	public String i = "";

	public GlyphLayout layout = new GlyphLayout();

	public EventModule(Event e, EventList l) {
		this.e = e;
		this.l = l;

		background = l.s.u.assets.get("UI/EventModule.png");
		up = l.s.u.assets.get("UI/SlideUp.png");
		upC = l.s.u.assets.get("UI/SlideUpChecked.png");
		down = l.s.u.assets.get("UI/SlideDown.png");
		downC = l.s.u.assets.get("UI/SlideDownChecked.png");

		// i="Start: "+dateToString(e.start)+"\nEnd:
		// "+dateToString(e.end)+"\n\nDescription: "+e.description;
		i = e.description;

		info = new TextArea(i, l.s.u.viewAreaStyle);
		info.setAlignment(Align.left);

		bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), .19f * Gdx.graphics.getHeight());

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

	public void draw(SpriteBatch pic, float fade) {

		upB = new Rectangle(bounds.x + .9f * bounds.width - .048f * l.s.u.w,
				bounds.y + .75f * bounds.height - .012f * l.s.u.h, .096f * l.s.u.w, .024f * l.s.u.h);
		downB = new Rectangle(bounds.x + .9f * bounds.width - .048f * l.s.u.w,
				bounds.y + .25f * bounds.height - .012f * l.s.u.h, .096f * l.s.u.w, .024f * l.s.u.h);

		info.setBounds(bounds.x + .05f * bounds.width, bounds.y + .25f * bounds.height, .7f * bounds.width,
				.5f * bounds.height);
		pic.setColor(1, 1, 1, fade);
		pic.draw(background, bounds.x, bounds.y, bounds.width, bounds.height);

		// Rectangle upBB = new
		// Rectangle(bounds.x+.9f*bounds.width-.05f*l.s.u.w,bounds.y+.75f*bounds.height-.05f*l.s.u.h,.1f*l.s.u.w,.1f*l.s.u.h);
		// Rectangle downBB = new
		// Rectangle(bounds.x+.9f*bounds.width-.05f*l.s.u.w,bounds.y+.25f*bounds.height-.05f*l.s.u.h,.1f*l.s.u.w,.1f*l.s.u.h);

		pic.draw(e.upVote.contains(l.s.u.myAcc.e) ? upC : up, upB.x, upB.y, upB.width, upB.height);
		pic.draw(e.downVote.contains(l.s.u.myAcc.e) ? downC : down, downB.x, downB.y, downB.width, downB.height);

		info.draw(pic, fade);

		l.s.u.mediumFnt.setColor(247f / 255f, 148f / 255f, 29f / 255f, fade);
		layout.setText(l.s.u.mediumFnt, e.name);
		l.s.u.mediumFnt.draw(pic, e.name, bounds.x + .05f * bounds.width,
				bounds.y + .85f * bounds.height + layout.height / 2);
		layout.setText(l.s.u.mediumFnt, "" + (e.upVote.size() - e.downVote.size()));
		l.s.u.mediumFnt.draw(pic, "" + (e.upVote.size() - e.downVote.size()),
				bounds.x + .9f * bounds.width - layout.width / 2, bounds.y + .5f * bounds.height + layout.height / 2);
		layout.setText(l.s.u.supersmallFnt, "Comments: "+e.comments.size());
		l.s.u.supersmallFnt.setColor(.5f, .5f, .5f, fade);
		l.s.u.supersmallFnt.draw(pic, "Comments: "+e.comments.size(), bounds.x+bounds.width/2 - layout.width/2,bounds.y+.1f*bounds.height+layout.height/2);

	}

	public void touch(Rectangle touchDown, Rectangle touchUp) {
		Rectangle upBB = new Rectangle(bounds.x + .9f * bounds.width - .05f * l.s.u.w,
				bounds.y + .75f * bounds.height - .05f * l.s.u.h, .1f * l.s.u.w, .1f * l.s.u.h);
		Rectangle downBB = new Rectangle(bounds.x + .9f * bounds.width - .05f * l.s.u.w,
				bounds.y + .25f * bounds.height - .05f * l.s.u.h, .1f * l.s.u.w, .1f * l.s.u.h);

		if (Math.abs(touchDown.x - touchUp.x) < 10 && Math.abs(touchDown.y - touchUp.y) < 10) {
			if (Intersector.overlaps(touchDown, upBB) && Intersector.overlaps(touchUp, upBB)) {
				if (e.downVote.contains(l.s.u.myAcc.e)) {
					e.downVote.remove(l.s.u.myAcc.e);
				}
				if (!e.upVote.contains(l.s.u.myAcc.e)) {
					e.upVote.add(l.s.u.myAcc.e);
				}
				UpVote o = new UpVote();
				o.e = l.s.u.myAcc.e;
				o.ID = e.ID;
				o.lng = e.lng;
				o.lat = e.lat;
				o.group = e.group;
				l.s.u.send(o);
			} else if (Intersector.overlaps(touchDown, downBB) && Intersector.overlaps(touchUp, downBB)) {
				if (!e.downVote.contains(l.s.u.myAcc.e)) {
					e.downVote.add(l.s.u.myAcc.e);
				}
				if (e.upVote.contains(l.s.u.myAcc.e)) {
					e.upVote.remove(l.s.u.myAcc.e);
				}
				DownVote o = new DownVote();
				o.e = l.s.u.myAcc.e;
				o.ID = e.ID;
				o.lng = e.lng;
				o.lat = e.lat;
				o.group = e.group;
				l.s.u.send(o);
			} else {
				l.s.u.nextScreen = new EventScreen(l.s.u, e, l.s.g,l.s);
			}
		}
	}

}
