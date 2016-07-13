package com.ggi.uparty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.ggi.uparty.screens.LoadScreen;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class uParty extends Game {
	
	public AssetManager assets = new AssetManager();
	
	public float w,h;
	public BitmapFont smallFnt,mediumFnt,largeFnt;

	public TextButtonStyle standardButtonStyle;
	public TextButtonStyle linkButtonStyle;
	
	public CheckBoxStyle checkStyle;
	
	/**Colors*/
	public Color orange = new Color(247f/255f,148f/255f,29f/255f,1f);
	public Color dark = new Color(.05f,.05f,.05f,1);
	public Color darkL = new Color(.1f,.1f,.1f,1);

	public TextFieldStyle textFieldStyle;;
	
	@Override
	public void create () {
		load();
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

	
}
