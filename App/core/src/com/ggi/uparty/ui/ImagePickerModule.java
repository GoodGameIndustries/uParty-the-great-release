package com.ggi.uparty.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.ggi.uparty.screens.ImagePicker;

public class ImagePickerModule {

	public ImagePicker p;
	
	public FileHandle fh;
	
	public Texture image = null;
	
	public TextureRegion load,preview=null;
	
	
	public float theta = 0;
	
	public Rectangle bounds = new Rectangle();
	
	
	public ImagePickerModule(ImagePicker p, final FileHandle fh){
		this.fh=fh;
		
		theta = 0;
		bounds.width=.85f*Gdx.graphics.getWidth()/6f;
		bounds.height=bounds.width;
		
		load = new TextureRegion(p.u.assets.get("UI/Load.png", Texture.class));
		
		
		
		
		
	}
	
	public void draw(SpriteBatch pic,float fade){
		try{
		if(p.manager!=null){p.manager.update();}
		if(p.manager!=null&&p.manager.isLoaded(fh.path().substring(Gdx.files.getExternalStoragePath().length()))){
			image = p.manager.get(fh.path().substring(Gdx.files.getExternalStoragePath().length()));
			float sqSize = image.getWidth()<image.getHeight()?image.getWidth():image.getHeight();
			preview = new TextureRegion(image,bounds.x+bounds.width/2-sqSize/2,bounds.y+bounds.height/2-sqSize/2,sqSize,sqSize);
		}
		}catch(Exception e){
			//e.printStackTrace();
		}
		theta++;
		pic.setColor(1, 1, 1, fade);
		if(image == null){
			pic.draw(load, bounds.x+bounds.width / 2 - bounds.height / 4, bounds.y + bounds.height / 4, bounds.height / 4,
					bounds.height / 4, bounds.height / 2, bounds.height / 2, 1, 1, -theta);
		}
		else if(preview !=null){
			pic.draw(preview,bounds.x,bounds.y,bounds.width,bounds.height);
		}
	}
	
}
