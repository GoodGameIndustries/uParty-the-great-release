package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.screens.MainScreen;

public class Toolbar {

	public MainScreen s;

	public Rectangle bounds;

	public int sortState = 1;// 0=menu 1=next 2=hot 3=new

	public Texture bg, bg2;

	public GlyphLayout layout = new GlyphLayout();

	public String feed = "";

	public float sideScroll = 0;

	private float velocity = 0;

	public boolean isPan = false;

	public Toolbar(MainScreen s) {
		this.s = s;
		bounds = new Rectangle(0, .875f * s.u.h, s.u.w, .125f * s.u.h);
		bg = s.u.assets.get("UI/Filled.png");
		bg2 = s.u.assets.get("UI/EventModule.png");

	}

	public void draw(SpriteBatch pic, float fade) {

		if (sideScroll - sortState * (.25f * s.u.w) != 0) {
			sideScroll += (sortState * (.25f * s.u.w) - sideScroll) / 3;
		}

		pic.setColor(1, 1, 1, fade);
		pic.draw(bg, bounds.x, bounds.y + 0.5f * bounds.height, bounds.width, .5f * bounds.height);
		pic.draw(bg2, bounds.x, bounds.y, bounds.width, 0.5f * bounds.height);

		pic.draw(
				sortState == 0 ? s.u.assets.get("UI/Toolbar/MenuC.png", Texture.class)
						: s.u.assets.get("UI/Toolbar/Menu.png", Texture.class),
				bounds.x + .125f * bounds.width - .125f * bounds.height, bounds.y + .125f * bounds.height,
				.25f * bounds.height, .25f * bounds.height);
		pic.draw(
				sortState == 1 ? s.u.assets.get("UI/Toolbar/NextC.png", Texture.class)
						: s.u.assets.get("UI/Toolbar/Next.png", Texture.class),
				bounds.x + .375f * bounds.width - .125f * bounds.height, bounds.y + .125f * bounds.height,
				.25f * bounds.height, .25f * bounds.height);
		pic.draw(
				sortState == 2 ? s.u.assets.get("UI/Toolbar/HotC.png", Texture.class)
						: s.u.assets.get("UI/Toolbar/Hot.png", Texture.class),
				bounds.x + .625f * bounds.width - .125f * bounds.height, bounds.y + .125f * bounds.height,
				.25f * bounds.height, .25f * bounds.height);
		pic.draw(
				sortState == 3 ? s.u.assets.get("UI/Toolbar/RecentC.png", Texture.class)
						: s.u.assets.get("UI/Toolbar/Recent.png", Texture.class),
				bounds.x + .875f * bounds.width - .125f * bounds.height, bounds.y + .125f * bounds.height,
				.25f * bounds.height, .25f * bounds.height);

		pic.draw(bg, bounds.x + sideScroll, bounds.y, .25f * bounds.width, .05f * bounds.height);

		s.u.mediumFnt.setColor(new Color(0f, 0f, 0f, fade));
		layout.setText(s.u.mediumFnt, feed.length() == 0 ? "Around You" : feed);
		s.u.mediumFnt.draw(pic, feed.length() == 0 ? "Around You" : feed,
				bounds.x + bounds.width / 2 - layout.width / 2, bounds.y + .75f * bounds.height + layout.height / 2);

	}

	public void touchDown(Rectangle touch) {

	}

	public void touchUp(Rectangle touch) {
		if (Intersector.overlaps(touch, new Rectangle(bounds.x + 0 * bounds.width ,
				bounds.y, .25f * bounds.width, bounds.height))) {
			sortState = 0;
		} else if (Intersector.overlaps(touch, new Rectangle(bounds.x + .25f * bounds.width,
				bounds.y, .25f * bounds.width, bounds.height))) {
			sortState = 1;
		} else if (Intersector.overlaps(touch, new Rectangle(bounds.x + .5f * bounds.width,
				bounds.y, .25f * bounds.width, bounds.height))) {
			sortState = 2;
		} else if (Intersector.overlaps(touch, new Rectangle(bounds.x + .75f * bounds.width,
				bounds.y, .25f * bounds.width, bounds.height))) {
			sortState = 3;
		}
		s.events.sort();

	}

	public void panStop() {
		isPan = false;
	}

}
