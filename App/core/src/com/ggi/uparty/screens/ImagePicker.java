package com.ggi.uparty.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ggi.uparty.uParty;
import com.ggi.uparty.ui.ImagePickerModule;

public class ImagePicker implements Screen{

	public uParty u;
	
	public ArrayList<FileHandle> images;
	
	public ArrayList<ImagePickerModule> modules = new ArrayList<ImagePickerModule>();

	private boolean goB;
	
	public float fade=0;

	private Texture background;
	
	public SpriteBatch pic = new SpriteBatch();
	public AssetManager manager;
	
	public ImagePicker(uParty u){
		this.u=u;
		this.images=getPictures(Gdx.files.getExternalStoragePath()+"DCIM/Camera");
		 manager = new AssetManager(new ExternalFileHandleResolver());
		 for(int i = 0; i < 20; i++){
			 manager.load(images.get(i).path().substring(Gdx.files.getExternalStoragePath().length()+1), Texture.class);
		 }
		 manager.update();
		 
		
		
		Thread t =new Thread(new Runnable(){
			

			@Override
			public void run() {
				genModules();
				
			}
		});
		t.run();
		
	}
	
	private void genModules() {
		//images.sort(new ByDateComparator());
		for(int i = 0; i < 20; i++){
			modules.add(new ImagePickerModule(this,images.get(i)));
			modules.get(i).bounds.x =0;
			modules.get(i).bounds.y =u.h-i*modules.get(i).bounds.height;
			
		}
		
	}

	@Override
	public void show() {
		background = u.assets.get("UI/Background.png");
		
	}

	@Override
	public void render(float delta) {
		System.out.println(manager.getAssetNames().random());
		//System.out.println(manager.getLoadedAssets());
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		pic.begin();
		pic.setColor(1, 1, 1, fade);
		pic.draw(background,0,0,u.w,u.h);
		for(int i = 0; i < modules.size(); i++){
			modules.get(i).draw(pic, fade);
		}
		if ((u.nextScreen == null && !goB) && fade < 1f) {
			fade += (1 - fade) / 2;
		} else if ((u.nextScreen != null || goB) && fade > .1f) {
			fade += (0 - fade) / 2;
		} else if (u.nextScreen != null) {
			u.setScreen(u.nextScreen);
		} else if (goB) {
			u.goBack();
		}
		pic.end();
		
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

	
	public ArrayList<FileHandle> getPictures(String start){
		ArrayList<FileHandle> result = new ArrayList<FileHandle>();
		
		for(FileHandle f : Gdx.files.absolute(start).list()){
			if(f.exists()){
				if(f.isDirectory()){
					result.addAll(getPictures(f.path()));
				}
				else{
					if(f.extension().equals("jpg") || f.extension().equals("png")){
						result.add(f);
					}
				}
			}
		}
		
		return result;
		
		
	}
}
