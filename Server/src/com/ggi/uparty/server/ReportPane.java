package com.ggi.uparty.server;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ggi.uparty.network.Event;

public class ReportPane extends JPanel{

	public LeftPane p;
	
	public Event e;
	
	public JButton allow,remove;
	
	public JTextArea info;
	
	public ReportPane(LeftPane p,Event e){
		this.p=p;
		this.e=e;
		
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		
		info = new JTextArea();
		info.setEditable(false);
		info.setText("Name: "+e.name
					+"\nStart: "+e.start.toString()
					+"\nEnd: "+e.end
					+"\nDescription "+e.description
					+"\nLocation "+e.location);
		info.setBackground(Color.black);
		info.setForeground(Color.orange);
		
		allow = new JButton();
			allow.setText("Allow");
			allow.setBackground(Color.black);
			allow.setForeground(Color.orange);
			allow.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg) {
					p.u.world.reported.remove(e);
					p.u.newReport=true;
					//p.u.saveWorld(p.u.world);
					
				}
				
			});
			
		remove = new JButton();
			remove.setText("Remove");
			remove.setBackground(Color.black);
			remove.setForeground(Color.orange);
			remove.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg) {
					p.u.world.reported.remove(e);
					p.u.removeEvent(e,p.u.world);
					p.u.stuffToDo.add(new Remove(e));
					p.u.newReport=true;
					//p.u.saveWorld(p.u.world);
					
				}
				
			});
			
		this.add(info);
		this.add(allow);
		this.add(remove);
	}
	
}
