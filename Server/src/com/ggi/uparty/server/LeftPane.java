package com.ggi.uparty.server;


import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LeftPane extends JPanel{
	
	public UPServer u;
	public JTextArea stats = new JTextArea();
	public String sts="";
	public int mb = 1024*1024;
	
	public LeftPane(UPServer u){
		this.u=u;
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		this.setSize(400, 400);
		
		stats.setBackground(Color.black);
		stats.setForeground(Color.orange);
		stats.setSize(400, 200);
		
		this.setLayout(new BorderLayout());
		this.add(stats,BorderLayout.NORTH);
	}
	
	private void buildStats(){
		Runtime instance = Runtime.getRuntime();
		sts="Stats:"
			+ "\nMemory Used: "+ (instance.totalMemory() - instance.freeMemory()) / mb +"MB ("+(int)((instance.totalMemory() - instance.freeMemory())*1.0/instance.totalMemory()*100.0)+"%)"
			+ "\nTime: "+new Date().toString()
			+ "\nUsers Online: "+(u.server!=null?u.server.getConnections().length:0)
			+ "\nLast Response Time: "+u.lastResponse+" ms"
			+ "\nTotal Data Points: " + u.world.points.size()
			+ "\nEvents in Storage: " + u.world.eventsInStorage;
		//System.out.println((instance.totalMemory() - instance.freeMemory()) / mb);
	}

	public void repaint(){
		super.repaint();
		if(stats!=null){
			buildStats();
		stats.setText(sts);}
	}
}
