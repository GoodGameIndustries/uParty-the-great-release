package com.ggi.uparty;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.ggi.uparty.network.Account;
import com.ggi.uparty.network.ErrorMessage;
import com.ggi.uparty.network.Network;
import com.ggi.uparty.screens.ConfirmationScreen;
import com.ggi.uparty.screens.LoadScreen;

public class uParty extends Game {
	
	public AssetManager assets = new AssetManager();
	
	public float w,h;
	public BitmapFont smallFnt,mediumFnt,largeFnt;

	public TextButtonStyle standardButtonStyle;
	public TextButtonStyle linkButtonStyle;
	public TextButtonStyle errorButtonStyle;
	
	public CheckBoxStyle checkStyle;
	
	public Client client;
	
	public String error="";
	
	public boolean debug = true;
	
	public Screen nextScreen;
	
	public Account myAcc;
	
	/**Colors*/
	public Color orange = new Color(247f/255f,148f/255f,29f/255f,1f);
	public Color dark = new Color(.05f,.05f,.05f,1);
	public Color darkL = new Color(.1f,.1f,.1f,1);

	public TextFieldStyle textFieldStyle;;
	
	@Override
	public void create () {
		load();
		createClient();
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		setScreen(new LoadScreen(this));
		
	}

	private void load() {
		assets.load("UI/Background.png", Texture.class);
		assets.load("Logos/1024.png", Texture.class);
		assets.load("UI/CircleLoad.png", Texture.class);
		assets.load("UI/Filled.png", Texture.class);
		assets.load("UI/FilledChecked.png", Texture.class);
		assets.load("UI/TextField.png", Texture.class);
		assets.load("UI/TextFieldChecked.png", Texture.class);
		assets.load("UI/CheckBox.png", Texture.class);
		assets.load("UI/CheckBoxChecked.png", Texture.class);
		
		assets.update();
		
		
		
	}

	public void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("calibri.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
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
				}
			}
			
		}));
	}

	
		
	

	public void connect(){
		if(!client.isConnected()){
			try {
				
				client.start();
				client.connect(5000, debug ?"localhost":"52.89.96.208", 36693);
				Network.register(client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				error="Cannot connect to server";
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
