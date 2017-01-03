package com.ggi.uparty.server;




import java.awt.Color;
import java.awt.ScrollPane;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class RightPane extends JPanel{

	public UPServer u;
	public String log = "Server Starting";
	public JTextArea logField = new JTextArea(log);
	public ScrollPane scroll = new ScrollPane();
	
	public RightPane(UPServer u){
		
		this.setBackground(Color.black);
		this.setForeground(Color.orange);
		this.u=u;
		logField.setEditable(false);
		logField.setSize(400, 400);
		logField.setBackground(Color.black);
		logField.setForeground(Color.orange);
		scroll.setSize(400, 350);
		scroll.add(logField);
		scroll.setBackground(Color.black);
		scroll.setForeground(Color.orange);
		this.setSize(400, 400);
		this.add(scroll);
		
	}
	
	public void repaint(){
		super.repaint();
		if(logField!=null){
		logField.setText(log);}
		
		
	}
	
	public void printConsole(String s){
		log=s+"\n"+log;
	}
	
}
