package com.ggi.uparty.server;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LeftPane extends JPanel{
	
	public UPServer u;
	public JTextArea stats = new JTextArea();
	public String sts="";
	public int mb = 1024*1024;
	int repaint = 0;
	
	public ScrollPane scroll = new ScrollPane();
	
	public LeftPane(UPServer u){
		this.u=u;
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		this.setSize(400, 400);
		
		stats.setBackground(Color.black);
		stats.setForeground(Color.orange);
		stats.setSize(400, 200);
		
		scroll.setBackground(Color.black);
		scroll.setForeground(Color.orange);
		scroll.setSize(400, 200);
		
		this.setLayout(new BorderLayout());
		this.add(stats,BorderLayout.NORTH);
		this.add(scroll,BorderLayout.SOUTH);
	}
	
	private void buildStats(){
		Runtime instance = Runtime.getRuntime();
		
		sts="Stats:"
			+ "\nMemory Used: "+ (instance.totalMemory() - instance.freeMemory()) / mb +"MB ("+(int)((instance.totalMemory() - instance.freeMemory())*1.0/instance.totalMemory()*100.0)+"%)"
			+ "\nTime: "+new Date().toString()
			+ "\nUsers Online: "+(u.server!=null?u.server.getConnections().length:0)
			+ "\nLast Response Time: "+u.lastResponse+" ms"
			+ "\nTotal Data Points: " + u.world.points.size()
			+ "\nEvents in Storage: " + u.world.eventsInStorage
			+ "\nReported Events: " + u.world.reported.size();
		//System.out.println((instance.totalMemory() - instance.freeMemory()) / mb);
	}

	public void repaint(){
		repaint++;
		super.repaint();
		if(stats!=null){
			buildStats();
		stats.setText(sts);}
		
		if(scroll!=null && repaint == 5){
			repaint = 0;
		scroll.removeAll();
		GridPanel g = new GridPanel();
		for(int i = 0; i <u.world.reported.size();i++){
			g.add(new ReportPane(this,u.world.reported.get(i)));
		}
		scroll.add(g);
		u.newReport=false;
		}
	}
}
