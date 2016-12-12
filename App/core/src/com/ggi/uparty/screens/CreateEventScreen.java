package com.ggi.uparty.screens;

import java.util.Date;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.ggi.uparty.uParty;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.ui.DateModule;

public class CreateEventScreen implements Screen, InputProcessor{

	public uParty u;
	
	public SpriteBatch pic;
	
	public Texture background,tA,tAC;
	
	public TextField name;
	
	public TextArea description,location;
	
	public TextButton create,back,error;
	
	public Rectangle nameB,descriptionB,locationB,createB,backB,errorB;
	
	public float fade = 0;
	
	public String n="",d="",l="";
	
	public DateModule start,end;
	
	public Stage stage;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public Group g;
	
	public float scr = 0;

	private boolean goB = false;
	
	public CreateEventScreen(uParty u,Group g){
		this.u=u;
		this.g=g;
	}
	
	@Override
	public void show() {
		pic = new SpriteBatch();
		
		stage = new Stage();
		
		Gdx.input.setInputProcessor(this);
		
		background = u.assets.get("UI/Background.png");
		tA = u.assets.get("UI/TextArea.png");
		tAC = u.assets.get("UI/TextAreaChecked.png");
		
		nameB = new Rectangle(2*u.w/9,.75f*u.h,5*u.w/9,u.h/16);
		createB = new Rectangle(2*u.w/9,.11f*u.h,5*u.w/9,u.h/16);
		descriptionB = new Rectangle(u.w/9,.38f*u.h,7*u.w/9,u.h/8);
		locationB = new Rectangle(u.w/9,.21f*u.h,7*u.w/9,u.h/8);
		backB=new Rectangle(u.w/36,.93f*u.h,.15f*u.w,.05f*u.h);
		errorB = new Rectangle(u.w/9,.04f*u.h,7*u.w/9,u.h/16);
		
		start = new DateModule(this);
			start.bounds= new Rectangle(.15f*u.w,.645f*u.h,.7f*u.w,u.h/16);
		end = new DateModule(this);
			end.bounds= new Rectangle(.15f*u.w,.54f*u.h,.7f*u.w,u.h/16);
		
		name = new TextField(n,u.textFieldStyle);
			name.setMessageText("Event Name");
			name.setAlignment(Align.center);
			name.setBounds(nameB.x, nameB.y, nameB.width, nameB.height);
		description = new TextArea(d,u.textAreaStyle);
			description.setMessageText("Description");
			description.setAlignment(Align.center);
			description.setBounds(descriptionB.x, descriptionB.y, descriptionB.width, descriptionB.height);
			description.setMaxLength(140);
		location = new TextArea(l,u.textAreaStyle);
			location.setMessageText("Location");
			location.setAlignment(Align.center);
			location.setBounds(locationB.x, locationB.y, locationB.width, locationB.height);
			location.setMaxLength(140);
		
		create = new TextButton("Create Event",u.standardButtonStyle);
			create.setBounds(createB.x, createB.y, createB.width, createB.height);
		back = new TextButton("Back",u.standardButtonStyle);
			back.setBounds(backB.x, backB.y, backB.width, backB.height);	
		error = new TextButton(u.error,u.errorButtonStyle);
			error.setBounds(errorB.x, errorB.y, errorB.width, errorB.height);
			
		stage.addActor(name);
		stage.addActor(description);
		stage.addActor(location);
		
		stage.addActor(start.month);
		stage.addActor(start.day);
		stage.addActor(start.year);
		stage.addActor(start.hour);
		stage.addActor(start.minute);
		stage.addActor(start.half);
		
		stage.addActor(end.month);
		stage.addActor(end.day);
		stage.addActor(end.year);
		stage.addActor(end.hour);
		stage.addActor(end.minute);
		stage.addActor(end.half);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		pic.begin();
		pic.setColor(1, 1, 1, 1);
		pic.draw(background, 0, 0, u.w, u.h);
		name.draw(pic, fade);
		description.draw(pic, fade);
		location.draw(pic, fade);
		pic.setColor(1,1,1,fade);
		pic.draw(stage.getKeyboardFocus()!=null&&stage.getKeyboardFocus().equals(description)?tAC:tA,descriptionB.x-.025f*descriptionB.width,descriptionB.y-.1f*descriptionB.height,1.05f*descriptionB.width,1.2f*descriptionB.height);
		pic.draw(stage.getKeyboardFocus()!=null&&stage.getKeyboardFocus().equals(location)?tAC:tA,locationB.x-.025f*locationB.width,locationB.y-.1f*locationB.height,1.05f*locationB.width,1.2f*locationB.height);
		
		u.largeFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.largeFnt, "New Event");
		u.largeFnt.draw(pic, "New Event", u.w/2-layout.width/2, .9f*u.h-layout.height/2+scr);
		
		u.mediumFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(u.mediumFnt, "Start Date:");
		u.mediumFnt.draw(pic, "Start Date:", u.w/2-layout.width/2, .745f*u.h-layout.height/2+scr);
		
		layout.setText(u.mediumFnt, "End Date:");
		u.mediumFnt.draw(pic, "End Date:", u.w/2-layout.width/2, .64f*u.h-layout.height/2+scr);
		
		error.setText(u.error);
		
		start.draw(pic, fade);
		end.draw(pic, fade);
		create.draw(pic, fade);
		back.draw(pic, fade);
		error.draw(pic, fade);
		
		pic.end();
		
		if((u.nextScreen==null && !goB)&&fade<1f){fade+=(1-fade)/2;}
		else if((u.nextScreen!=null || goB)&&fade>.1f){fade+=(0-fade)/2;}
		else if(u.nextScreen!=null){u.setScreen(u.nextScreen);}
		else if(goB){u.goBack();}
		
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
		if(!Character.isAlphabetic(character)&&!Character.isDigit(character)&&!Character.isSpaceChar(character)&&character!=''){return true;}
		
		if(stage.getKeyboardFocus()==null){}
		
		else if(stage.getKeyboardFocus().equals(name)){if(character == ''&&n.length()>0){
			n=n.substring(0, n.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			n+=character;
		}}
		
		else if(stage.getKeyboardFocus().equals(description)){if(character == ''&&d.length()>0){
			d=d.substring(0, d.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			d+=character;
		}}
		
		else if(stage.getKeyboardFocus().equals(location)){if(character == ''&&l.length()>0){
			l=l.substring(0, l.length()-1);
		}
		else if((character == '\r' || character == '\n')){}
		else{
			l+=character;
		}}
		
		//start date
		else if(stage.getKeyboardFocus().equals(start.half)){start.isPM=!start.isPM;stage.setKeyboardFocus(null);}
		
		if(stage.getKeyboardFocus().equals(start.minute)){if(character == ''&&start.min.length()>0){
			start.min=start.min.substring(0, start.min.length()-1);
		}
		else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
		else{
			if(start.min.length()==2){}
			else{start.min+=character;}
			
		}}
		
		else if(stage.getKeyboardFocus().equals(start.hour)){if(character == ''&&start.hr.length()>0){
			start.hr=start.hr.substring(0, start.hr.length()-1);
		}
		else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
		else{
			start.hr+=character;
			if(start.hr.length()==2){start.min="";stage.setKeyboardFocus(start.minute);}
		}}
		
		else if(stage.getKeyboardFocus().equals(start.year)){if(character == ''&&start.y.length()>0){
			start.y=start.y.substring(0, start.y.length()-1);
		}
		else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
		else{
			start.y+=character;
			if(start.y.length()==4){start.hr="";stage.setKeyboardFocus(start.hour);}
		}}
		
		else if(stage.getKeyboardFocus().equals(start.day)){if(character == ''&&start.d.length()>0){
			start.d=start.d.substring(0, start.d.length()-1);
		}
		else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
		else{
			start.d+=character;
			if(start.d.length()==2){start.y="";stage.setKeyboardFocus(start.year);}
		}}
		
		else if(stage.getKeyboardFocus().equals(start.month)){if(character == ''&&start.m.length()>0){
			start.m=start.m.substring(0, start.m.length()-1);
		}
		else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
		else{
			start.m+=character;
			if(start.m.length()==2){start.d="";stage.setKeyboardFocus(start.day);}
		}}
		
		//end date
		else if(stage.getKeyboardFocus().equals(end.half)){end.isPM=!end.isPM;stage.setKeyboardFocus(null);}
				
		else if(stage.getKeyboardFocus().equals(end.minute)){if(character == ''&&end.min.length()>0){
					end.min=end.min.substring(0, end.min.length()-1);
				}
				else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
				else{
					if(end.min.length()==2){}
					else{end.min+=character;}
				}}
				
		else if(stage.getKeyboardFocus().equals(end.hour)){if(character == ''&&end.hr.length()>0){
					end.hr=end.hr.substring(0, end.hr.length()-1);
				}
				else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
				else{
					end.hr+=character;
					if(end.hr.length()==2){end.min="";stage.setKeyboardFocus(end.minute);}
				}}
				
		else if(stage.getKeyboardFocus().equals(end.year)){if(character == ''&&end.y.length()>0){
					end.y=end.y.substring(0, end.y.length()-1);
				}
				else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
				else{
					end.y+=character;
					if(end.y.length()==4){end.hr="";stage.setKeyboardFocus(end.hour);}
				}}
				
		else if(stage.getKeyboardFocus().equals(end.day)){if(character == ''&&end.d.length()>0){
					end.d=end.d.substring(0, end.d.length()-1);
				}
				else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
				else{
					end.d+=character;
					if(end.d.length()==2){end.y="";stage.setKeyboardFocus(end.year);}
				}}
				
		else if(stage.getKeyboardFocus().equals(end.month)){if(character == ''&&end.m.length()>0){
					end.m=end.m.substring(0, end.m.length()-1);
				}
				else if((character == '\r' || character == '\n' || !Character.isDigit(character))){}
				else{
					end.m+=character;
					if(end.m.length()==2){end.d="";stage.setKeyboardFocus(end.day);}
				}}
		
		name.setText(n);
		description.setText(d);
		location.setText(l);
		
		
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		
		if(Intersector.overlaps(touch, backB)){back.toggle();}
		else if(Intersector.overlaps(touch, createB)){create.toggle();}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenY = (int) (u.h-screenY);
		Rectangle touch = new Rectangle(screenX,screenY,1,1);
		toggleOff();
		
		if(Intersector.overlaps(touch, backB)){goB = true;}
		else if(Intersector.overlaps(touch, createB)){
			if(n.length()>0&&d.length()>0&&l.length()>0&&start.isFilled()&&end.isFilled()){
				if(d.length()>140){d=d.substring(0, 140);}
				if(l.length()>140){l=l.substring(0, 140);}
				
				Date start = new Date();
					start.setMonth(Integer.parseInt(this.start.m)-1);
					start.setDate(Integer.parseInt(this.start.d));
					start.setYear(Integer.parseInt(this.start.y)-1900);
					start.setHours((Integer.parseInt(this.start.hr)%12)+(this.start.isPM?12:0));
					start.setMinutes(Integer.parseInt(this.start.min));
				Date end = new Date();
					end.setMonth(Integer.parseInt(this.end.m)-1);
					end.setDate(Integer.parseInt(this.end.d));
					end.setYear(Integer.parseInt(this.end.y)-1900);
					end.setHours((Integer.parseInt(this.end.hr)%12)+(this.end.isPM?12:0));
					end.setMinutes(Integer.parseInt(this.end.min));

				Event event = new Event(u.controller.getLong(),u.controller.getLat(),n,d,l,start,end,u.myAcc.e,u.myAcc.xp);	
				if(g!=null){
					event.group=g.name.replace(" ", "")+g.owner.replace(".", "_").replace("@", "_");
				}
				u.send(event);
				
				MainScreen s = new MainScreen(u);
				s.g=g;
				
				u.nextScreen=s;
				
			}else{
				u.error="Please fill out all fields";
			}
			
		}
		else if(Intersector.overlaps(touch, nameB)){stage.setKeyboardFocus(name);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, descriptionB)){stage.setKeyboardFocus(description);Gdx.input.setOnscreenKeyboardVisible(true);scr=.07f*u.h;}
		else if(Intersector.overlaps(touch, locationB)){stage.setKeyboardFocus(location);Gdx.input.setOnscreenKeyboardVisible(true);scr=.24f*u.h;}
		
		/**start date*/
		else if(Intersector.overlaps(touch, start.monthB)){start.m="";stage.setKeyboardFocus(start.month);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, start.dayB)){start.d="";stage.setKeyboardFocus(start.day);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, start.yearB)){start.y="";stage.setKeyboardFocus(start.year);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, start.hourB)){start.hr="";stage.setKeyboardFocus(start.hour);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, start.minuteB)){start.min="";stage.setKeyboardFocus(start.minute);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, start.halfB)){start.isPM=!start.isPM;}
		
		
		/**end date*/
		else if(Intersector.overlaps(touch, end.monthB)){end.m="";stage.setKeyboardFocus(end.month);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, end.dayB)){end.d="";stage.setKeyboardFocus(end.day);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, end.yearB)){end.y="";stage.setKeyboardFocus(end.year);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, end.hourB)){end.hr="";stage.setKeyboardFocus(end.hour);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, end.minuteB)){end.min="";stage.setKeyboardFocus(end.minute);Gdx.input.setOnscreenKeyboardVisible(true);}
		else if(Intersector.overlaps(touch, end.halfB)){end.isPM=!end.isPM;}
		
		setB();
		
		return true;
	}
	
	public void toggleOff(){
		scr=0;
		Gdx.input.setOnscreenKeyboardVisible(false);
		stage.setKeyboardFocus(null);
		if(back.isChecked()){back.toggle();}
		if(create.isChecked()){create.toggle();}
		
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

	public void setB(){
		nameB = new Rectangle(2*u.w/9,.75f*u.h+scr,5*u.w/9,u.h/16);
		createB = new Rectangle(2*u.w/9,.11f*u.h+scr,5*u.w/9,u.h/16);
		descriptionB = new Rectangle(u.w/9,.38f*u.h+scr,7*u.w/9,u.h/8);
		locationB = new Rectangle(u.w/9,.21f*u.h+scr,7*u.w/9,u.h/8);
		backB=new Rectangle(u.w/36,.93f*u.h+scr,.15f*u.w,.05f*u.h);
		errorB = new Rectangle(u.w/9,.04f*u.h+scr,7*u.w/9,u.h/16);
		
			start.bounds= new Rectangle(.15f*u.w,.645f*u.h+scr,.7f*u.w,u.h/16);
	
			end.bounds= new Rectangle(.15f*u.w,.54f*u.h+scr,.7f*u.w,u.h/16);
		
			name.setBounds(nameB.x, nameB.y, nameB.width, nameB.height);
		
			description.setBounds(descriptionB.x, descriptionB.y, descriptionB.width, descriptionB.height);
			
			location.setBounds(locationB.x, locationB.y, locationB.width, locationB.height);
			
			create.setBounds(createB.x, createB.y, createB.width, createB.height);
		
			back.setBounds(backB.x, backB.y, backB.width, backB.height);	
		
			error.setBounds(errorB.x, errorB.y, errorB.width, errorB.height);
	}
}
