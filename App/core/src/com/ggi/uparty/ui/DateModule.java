package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.screens.CreateEventScreen;

public class DateModule {

	public CreateEventScreen s;
	
	public Rectangle bounds=new Rectangle(),monthB=new Rectangle(),dayB=new Rectangle(),
			yearB=new Rectangle(),hourB=new Rectangle(), minuteB=new Rectangle(),halfB=new Rectangle();
	
	public String m="",d="",y="",hr="",min="";
	
	public TextField month,day,year,hour,minute,half;
	
	public Texture background;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public float totalWidth=0,slashWidth=0,colonWidth=0,spaceWidth=0;
	
	
	public boolean isPM=false;
	
	public DateModule(CreateEventScreen s){
		this.s=s;
		
		background = s.u.assets.get("UI/TextField.png");
		
		month = new TextField(m,s.u.plainTextStyle);
			month.setMessageText("MM");
			month.setAlignment(Align.center);
		day = new TextField(d,s.u.plainTextStyle);
			day.setMessageText("DD");
			day.setAlignment(Align.center);
		year = new TextField(y,s.u.plainTextStyle);
			year.setMessageText("YYYY");
			year.setAlignment(Align.center);
		hour = new TextField(hr,s.u.plainTextStyle);
			hour.setMessageText("hh");
			hour.setAlignment(Align.center);
		minute = new TextField(min,s.u.plainTextStyle);
			minute.setMessageText("mm");
			minute.setAlignment(Align.center);
		half = new TextField("AM",s.u.plainTextStyle);
			half.setAlignment(Align.center);
		
	}
	
	public void draw(SpriteBatch pic, float fade){
		half.setText(isPM?"PM":"AM");
		
		
		
		
		layout.setText(s.u.mediumFnt, (m.length()>0?m:"MM")+"/"+(d.length()>0?d:"DD")+"/"+(y.length()>0?y:"YYYY")+"   "+(hr.length()>0?hr:"hh")+":"+(min.length()>0?min:"mm")+(isPM?" PM":" AM"));
			totalWidth = layout.width;
		layout.setText(s.u.mediumFnt, "/");
			slashWidth = layout.width;
		layout.setText(s.u.mediumFnt, ":");
			colonWidth = layout.width;
		layout.setText(s.u.mediumFnt, " ");
			spaceWidth = layout.width;
		layout.setText(s.u.mediumFnt, m.length()>0?m:"MM");
		monthB=new Rectangle(s.u.w/2-totalWidth/2,bounds.y,layout.width,bounds.height);
		layout.setText(s.u.mediumFnt, d.length()>0?d:"DD");
		dayB=new Rectangle(monthB.x+slashWidth+monthB.width,bounds.y,layout.width,bounds.height);
		layout.setText(s.u.mediumFnt, y.length()>0?y:"YYYY");
		yearB=new Rectangle(dayB.x+slashWidth+dayB.width,bounds.y,layout.width,bounds.height);
		layout.setText(s.u.mediumFnt, hr.length()>0?hr:"hh");
		hourB=new Rectangle(yearB.x+3*spaceWidth+yearB.width,bounds.y,layout.width,bounds.height);
		layout.setText(s.u.mediumFnt, min.length()>0?min:"mm");
		minuteB=new Rectangle(hourB.x+colonWidth+hourB.width,bounds.y,layout.width,bounds.height);
		layout.setText(s.u.mediumFnt, (isPM?"PM":"AM"));
		halfB=new Rectangle(minuteB.x+spaceWidth+minuteB.width,bounds.y,layout.width,bounds.height);
		
		month.setBounds(monthB.x, monthB.y, monthB.width, monthB.height);
		day.setBounds(dayB.x, dayB.y, dayB.width, dayB.height);
		year.setBounds(yearB.x, yearB.y, yearB.width, yearB.height);
		hour.setBounds(hourB.x, hourB.y, hourB.width, hourB.height);
		minute.setBounds(minuteB.x, minuteB.y, minuteB.width, minuteB.height);
		half.setBounds(halfB.x, halfB.y, halfB.width, halfB.height);
		
		pic.setColor(1,1,1,fade);
		pic.draw(background,bounds.x,bounds.y,bounds.width,bounds.height);
		
		month.setText(m.length()>0?m:"MM");
		day.setText(d.length()>0?d:"DD");
		year.setText(y.length()>0?y:"YYYY");
		hour.setText(hr.length()>0?hr:"hh");
		minute.setText(min.length()>0?min:"mm");
		month.draw(pic, fade);
		day.draw(pic, fade);
		year.draw(pic, fade);
		hour.draw(pic, fade);
		minute.draw(pic, fade);
		half.draw(pic, fade);
		
		s.u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(s.u.mediumFnt, "/");
		s.u.mediumFnt.draw(pic, "/", monthB.x+monthB.width, bounds.y+bounds.height/2+layout.height/2);
		s.u.mediumFnt.draw(pic, "/", dayB.x+dayB.width, bounds.y+bounds.height/2+layout.height/2);
		layout.setText(s.u.mediumFnt, ":");
		s.u.mediumFnt.draw(pic, ":", hourB.x+hourB.width, bounds.y+bounds.height/2+layout.height/2);
	}

	public boolean isFilled() {
		return (m.length()>0&&d.length()>0&&y.length()>0&&hr.length()>0&&min.length()>0);
	}
	
	
}
