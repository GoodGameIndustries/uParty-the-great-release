package com.ggi.uparty.loadbalance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UsagePane extends JPanel{

	public UPLoad u;
	public JTextArea stats = new JTextArea();
	public ScrollPane scroll = new ScrollPane();
	
	public UsagePane(UPLoad u){
		this.u=u;
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		this.setSize(400,400);
		
		stats.setBackground(Color.black);
		stats.setForeground(Color.orange);
		stats.setEditable(false);
		
		scroll.setBackground(Color.black);
		scroll.setForeground(Color.orange);
		scroll.setSize(400, 400);
		
		scroll.add(stats);
		
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
	}
	public void repaint(){
		super.repaint();
		if(u!=null && u.track!=null){
		String sts = "Connections in the last hour: "+u.track.users;
		for(int i = u.track.report.size()-1; i>=0; i--){
			sts+="\n"+u.track.report.get(i).toString();
		}
		stats.setText(sts);
		}
	}
}
