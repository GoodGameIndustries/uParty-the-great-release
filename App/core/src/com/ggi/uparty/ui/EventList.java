package com.ggi.uparty.ui;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.screens.MainScreen;
import com.ggi.uparty.util.HotComparator;
import com.ggi.uparty.util.NewComparator;
import com.ggi.uparty.util.NextComparator;

public class EventList {

	public MainScreen s;

	public ArrayList<EventModule> modules = new ArrayList<EventModule>();
	public ArrayList<Event> evs = new ArrayList<Event>();

	public int scrolled = 0;

	public int focus = 0;

	public float velocity = 0;

	public boolean refresh = false;

	public boolean sentRef = false;

	public boolean isPan = false;

	public RefreshModule rM;

	public EventList(MainScreen s) {
		this.s = s;
		rM = new RefreshModule(s);
		rM.bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), .14f * Gdx.graphics.getHeight());
		scrolled = (int) (-.16f * s.u.h);
	}

	public void giveEvents(ArrayList<Event> evs) {
		// System.out.println("Refresh End");
		modules.clear();
		this.evs.clear();
		this.evs.addAll(evs);
		System.out.println(this.evs.size());
		focus = 0;

		for (Event e : evs) {
			modules.add(new EventModule(e, this));
		}

		sort();

		refresh = false;
	}

	public void sort() {
		switch (s.toolbar.sortState) {
		case 2:
			Collections.sort(modules, new HotComparator());
			break;
		case 1:
			Collections.sort(modules, new NextComparator());
			break;
		case 3:
			Collections.sort(modules, new NewComparator());
			break;
		}

	}

	public void draw(SpriteBatch pic, float fade) {
		if (!refresh) {
			sentRef = false;
		}
		if (!isPan && scrolled < 0 && !refresh) {
			scrolled /= 2;
		}

		if (scrolled < -.15f * s.u.h) {
			scrolled = (int) (-.15f * s.u.h);
			refresh = true;
			if (!sentRef) {
				s.refresh();
				sentRef = true;
			}
		}

		if (modules.size() * .2f * s.u.h + scrolled > .15f * s.u.h && modules.size() * .2f * s.u.h > .675f * s.u.h) {
			scrolled = (int) (.15f * s.u.h - modules.size() * .2f * s.u.h);
		} else if (scrolled > 0) {
			scrolled = 0;
		}

		rM.bounds.y = .675f * s.u.h + .2f * s.u.h + scrolled;
		rM.draw(pic, fade);

		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).bounds.y = .675f * s.u.h - i * .2f * s.u.h + scrolled;
			modules.get(i).draw(pic, fade);
		}
	}

	public void touch(Rectangle touchDown, Rectangle touchUp) {
		for (int i = 0; i < modules.size(); i++) {
			if (Intersector.overlaps(touchDown, modules.get(i).bounds)
					&& Intersector.overlaps(touchUp, modules.get(i).bounds)) {
				modules.get(i).touch(touchDown, touchUp);
			}
		}
	}

}
