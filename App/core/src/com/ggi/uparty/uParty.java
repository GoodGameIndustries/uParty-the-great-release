package com.ggi.uparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.ggi.uparty.network.Account;
import com.ggi.uparty.network.Comment;
import com.ggi.uparty.network.DownVote;
import com.ggi.uparty.network.ErrorMessage;
import com.ggi.uparty.network.Event;
import com.ggi.uparty.network.Friend;
import com.ggi.uparty.network.Group;
import com.ggi.uparty.network.Member;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.network.Refresh;
import com.ggi.uparty.network.Report;
import com.ggi.uparty.network.UpVote;
import com.ggi.uparty.screens.LoadScreen;
import com.ggi.uparty.screens.NewGroupScreen;
import com.ggi.uparty.screens.SignUpScreen;

public class uParty extends Game {
	
	public AssetManager assets = new AssetManager();
	
	public float w,h;
	public BitmapFont supersmallFnt,smallFnt,mediumFnt,largeFnt;

	public TextButtonStyle standardButtonStyle;
	public TextButtonStyle linkButtonStyle;
	public TextButtonStyle errorButtonStyle;
	public TextButtonStyle sortButtonStyle;
	public TextButtonStyle redButtonStyle;
	
	public ButtonStyle slideStyle;
	
	public CheckBoxStyle checkStyle;
	
	public Client client;
	
	public String error="";
	
	public boolean debug = true;
	
	public Screen nextScreen;
	
	public Account myAcc;
	
	public Preferences prefs;
	
	/**Colors*/
	public Color orange = new Color(247f/255f,148f/255f,29f/255f,1f);
	public Color dark = new Color(.05f,.05f,.05f,1);
	public Color darkL = new Color(.1f,.1f,.1f,1);

	public TextFieldStyle textFieldStyle;
	public TextFieldStyle textAreaStyle;
	public TextFieldStyle viewAreaStyle;
	public TextFieldStyle plainTextStyle;
	
	public NativeController controller;
	
	public boolean refreshing = false;
	
	public ArrayList<Event> events = new ArrayList<Event>();

	public boolean needUpdate = false;
	
	public boolean accRefresh = false;

	public boolean logout=false;
	
	public boolean groupRefresh=false;
	
	public uParty(NativeController controller){
		this.controller=controller;
	}
	
	@Override
	public void create () {
		load();
		prefs = Gdx.app.getPreferences("My Preferences");
		createClient();
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		setScreen(new LoadScreen(this));
		
		
	}

	private void load() {
		assets.load("Logos/Social.png",Texture.class);
		assets.load("UI/Background.png", Texture.class);
		assets.load("Logos/1024.png", Texture.class);
		assets.load("UI/CircleLoad.png", Texture.class);
		assets.load("UI/Filled.png", Texture.class);
		assets.load("UI/FilledChecked.png", Texture.class);
		assets.load("UI/TextField.png", Texture.class);
		assets.load("UI/TextFieldChecked.png", Texture.class);
		assets.load("UI/CheckBox.png", Texture.class);
		assets.load("UI/CheckBoxChecked.png", Texture.class);
		assets.load("UI/Toolbar.png", Texture.class);
		assets.load("Logos/512.png", Texture.class);
		assets.load("UI/PopUpMenu.png", Texture.class);
		assets.load("UI/PopUpMenuBottom.png", Texture.class);
		assets.load("UI/SlideUp.png",Texture.class);
		assets.load("UI/SlideDown.png", Texture.class);
		assets.load("UI/SlideUpChecked.png",Texture.class);
		assets.load("UI/SlideDownChecked.png", Texture.class);
		assets.load("UI/Darken.png", Texture.class);
		assets.load("UI/FilledRed.png", Texture.class);
		assets.load("UI/FilledRedChecked.png", Texture.class);
		assets.load("UI/TextArea.png", Texture.class);
		assets.load("UI/TextAreaChecked.png", Texture.class);
		assets.load("UI/EventModule.png", Texture.class);
		assets.load("UI/Load.png", Texture.class);
		
		
		assets.update();
		
		
		
	}

	public void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("calibri.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (h/80);
		supersmallFnt = generator.generateFont(parameter);
		
		parameter.size = (int) (h/50);
		smallFnt = generator.generateFont(parameter); 
		
		parameter.size = (int) (h/40);
		mediumFnt = generator.generateFont(parameter); 
		
		parameter.size = (int) (h/25);
		largeFnt = generator.generateFont(parameter); 
		generator.dispose();
		
		
		
	}
	
	public void createClient(){
		client= new Client();
		client.addListener(new ThreadedListener(new Listener(){
			 

			

			public void received (Connection connection, Object object) {
				if(object instanceof ErrorMessage){
					ErrorMessage o = (ErrorMessage)object;
					error=o.error;
					
				}
				
				if(object instanceof Account){
					Account o = (Account)object;
					myAcc=o;
					accRefresh=true;
				}
				
				if(object instanceof Member){
					Member o = (Member)object;
					myAcc.groups.get(myAcc.groups.size()-1).members.add(o);
				}
				
				if(object instanceof Friend){
					Friend o = (Friend)object;
					myAcc.friends.add(o);
				}
				
				if(object instanceof Group){
					Group o = (Group)object;
					myAcc.groups.add(o);
					if(getScreen() instanceof NewGroupScreen){
						NewGroupScreen s = (NewGroupScreen)getScreen();
						s.created=true;
					}
					groupRefresh=true;
				}
				
				if(object instanceof Event){
					System.out.println("Event Recieved");
					if(!refreshing){refreshing = true;groupRefresh=false;events.clear();System.out.println(events.size());}
					Date d = new Date();
					Event o = (Event)object;
					if(o.end.after(d)){
						//System.out.println(o.end.getTime()-d.getTime());
						System.out.println("Group: "+o.group);
						if(o.group.length()>0){
							myAcc.groups.get(myAcc.groups.size()-1).events.add(o);
						}else{
						events.add(o);}
					}
				}
				
				if(object instanceof Comment){
					System.out.println("Recieved Comment");
					Comment o = (Comment)object;
					if(o.group.length()>0){
						Event e = myAcc.groups.get(myAcc.groups.size()-1).events.get(myAcc.groups.get(myAcc.groups.size()-1).events.size()-1);
						if(e.ID.equals(o.ID)){
							e.comments.add(o.c);
						}
					}
					else{
					if(events.size()>0){
						Event e = events.get(events.size()-1);
						if(e.ID.equals(o.ID)){
							e.comments.add(o.c);
						}
					}
					}
				}
				
				if(object instanceof UpVote){
					System.out.println("Recieved upVote");
					UpVote o = (UpVote)object;
					if(o.group.length()>0){
						Event e = myAcc.groups.get(myAcc.groups.size()-1).events.get(myAcc.groups.get(myAcc.groups.size()-1).events.size()-1);
						if(e.ID.equals(o.ID)){
							e.upVote.add(o.e);
						}
					}
					else{
					if(events.size()>0){
						Event e = events.get(events.size()-1);
						if(e.ID.equals(o.ID)){
						e.upVote.add(o.e);System.out.println("Added");
						}
					}
					}
				}
				
				if(object instanceof DownVote){
					System.out.println("Recieved downVote");
					DownVote o = (DownVote)object;
					
					if(o.group.length()>0){
						Event e = myAcc.groups.get(myAcc.groups.size()-1).events.get(myAcc.groups.get(myAcc.groups.size()-1).events.size()-1);
						if(e.ID.equals(o.ID)){
							e.downVote.add(o.e);
						}
					}
					else{
					if(events.size()>0){
						Event e = events.get(events.size()-1);
						if(e.ID.equals(o.ID)){
						e.downVote.add(o.e);System.out.println("Added");
						}
					}
					}
				}
				
				if(object instanceof Report){
					System.out.println("Recieved Report");
					Report o = (Report)object;
					
					if(o.group.length()>0){
						Event e = myAcc.groups.get(myAcc.groups.size()-1).events.get(myAcc.groups.get(myAcc.groups.size()-1).events.size()-1);
						if(e.ID.equals(o.ID)){
							if(o.e.equals(myAcc.e)){
							myAcc.groups.get(myAcc.groups.size()-1).events.remove(e);
							}
							}
					}
					else{
					if(events.size()>0){
						Event e = events.get(events.size()-1);
						if(e.ID.equals(o.ID)){
						if(o.e.equals(myAcc.e)){
							events.remove(events.size()-1);
						}
						}
					}
					}
				}
				
				if(object instanceof Refresh){
					refreshing=false;
					needUpdate =true;
					System.out.println(events.size());
				}
			}

			private Event searchId(String iD) {
				Event result = null;
				
				for(int i = 0; i < events.size(); i++){
					if(events.get(i).ID.equals(iD)){result = events.get(i);}
					break;
				}
				
				return result;
			}
			
		}));
	}

	
		
	

	public void connect(){
		if(!client.isConnected()){
			try {
				
				client.start();
				client.connect(5000, debug ?"localhost":"52.89.96.208", 36695);
				Network.register(client);
			} catch (IOException e) {
				error="Cannot connect to server.\nMake sure your app is up to date\nand try again soon.";
				
				e.printStackTrace();
			}
		}
	}
	
	public void send(Object o){
		connect();
		int t=0;
		boolean noSend = true;
		while(noSend&&t<=3){
			try{
				t++;
				client.sendTCP(o);
				noSend=false;
			}catch (Exception e){
				connect();
			}
		}
	}
	
	public void setScreen(Screen s){
		error="";
		nextScreen=null;
		super.setScreen(s);
	}
	
}
