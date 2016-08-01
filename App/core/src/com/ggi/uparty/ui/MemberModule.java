package com.ggi.uparty.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ggi.uparty.network.LeaveGroup;
import com.ggi.uparty.network.Member;

public class MemberModule extends Module{

	public Member m;
	
	public Texture background;
	
	public TextButton remove;
	
	public Rectangle removeB = new Rectangle();
	
	public List l;
	
	public GlyphLayout layout = new GlyphLayout();
	
	public boolean isOwner = false;
	
	public MemberModule(List l,Member m) {
		this.m=m;
		this.l=l;
		background = l.u.assets.get("UI/EventModule.png");
		
		remove = new TextButton("Remove",l.u.redButtonStyle);
			remove.setBounds(removeB.x, removeB.y, removeB.width, removeB.height);
	}
	
	public void draw(SpriteBatch pic, float fade){
		pic.setColor(1, 1, 1, fade);
		pic.draw(background,bounds.x,bounds.y,bounds.width,bounds.height);
		
		removeB = new Rectangle(bounds.x+.75f*bounds.width,bounds.y,.25f*bounds.width,bounds.height);
		remove.setBounds(removeB.x, removeB.y, removeB.width, removeB.height);
		
		l.u.smallFnt.setColor(247f/255f,148f/255f,29f/255f,fade);
		layout.setText(l.u.smallFnt, m.u+" ("+m.e+")");
		l.u.smallFnt.draw(pic,m.u+" ("+m.e+")", bounds.x+.05f*bounds.width, bounds.y+bounds.height/2+layout.height/2);
		
		layout.setText(l.u.smallFnt, m.rank!=2?"Member":"Owner");
		l.u.smallFnt.draw(pic,m.rank!=2?"Member":"Owner", bounds.x+.95f*bounds.width-layout.width, bounds.y+bounds.height/2+layout.height/2);
		
		MemberModule mod = (MemberModule) l.modules.get(0);
		if(mod.m.e.equals(l.u.myAcc.e)&&m.rank!=2){remove.draw(pic, fade);isOwner=true;}
	}
	
	public void touchDown(Rectangle touch){
		if(Intersector.overlaps(touch, removeB)&&isOwner){remove.toggle();}
	}
	
	public void touchUp(Rectangle touch){
		toggleOff();
		if(Intersector.overlaps(touch, removeB)&&isOwner){
			LeaveGroup l = new LeaveGroup();
			l.e=m.e;
			l.group=this.l.g.name.replace(" ", "")+this.l.g.owner.replace(".", "_").replace("@", "_");
			this.l.u.send(l);
			this.l.modules.remove(this);
		}
	}
	
	public void toggleOff(){
		if(remove.isChecked()){remove.toggle();}
	}

}
